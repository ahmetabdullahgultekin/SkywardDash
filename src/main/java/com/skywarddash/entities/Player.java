package com.skywarddash.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.skywarddash.utils.Constants;

public class Player {
    private static final float MAX_COYOTE_TIME = 0.1f;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle bounds;
    private boolean onGround;
    private float momentum;
    private int comboCount;
    private boolean wasOnGround;
    private boolean hasAirJump;
    private float coyoteTime;
    
    // Animation state
    private float animationTime = 0f;
    private PlayerAnimationState currentAnimation = PlayerAnimationState.IDLE;
    
    public enum PlayerAnimationState {
        IDLE, RUNNING, JUMPING, FALLING
    }

    public Player(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bounds = new Rectangle(x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
        onGround = true; // Start on ground
        momentum = 0;
        comboCount = 0;
        wasOnGround = true;
        hasAirJump = true;
        coyoteTime = 0;
    }

    public void update(float deltaTime) {
        wasOnGround = onGround;
        animationTime += deltaTime;

        // Update coyote time
        if (wasOnGround && !onGround) {
            coyoteTime = MAX_COYOTE_TIME;
        } else if (coyoteTime > 0) {
            coyoteTime -= deltaTime;
        }
        
        // Update animation state
        updateAnimationState();

        // Apply gravity only if not on ground
        if (!onGround) {
            velocity.y += Constants.GRAVITY * deltaTime;
            // Apply terminal velocity limit
            if (velocity.y < Constants.TERMINAL_VELOCITY) {
                velocity.y = Constants.TERMINAL_VELOCITY;
            }
        }

        // Apply horizontal friction when on ground - much better sliding
        if (onGround) {
            velocity.x *= 0.98f; // Very low friction for excellent sliding
        } else {
            // Even less friction in air for more momentum preservation
            velocity.x *= 0.995f; // Almost no air friction
        }

        // Update momentum based on horizontal speed and movement consistency
        float speedRatio = Math.abs(velocity.x) / (Constants.MAX_HORIZONTAL_SPEED * 0.6f); // Use 60% of max speed as reference
        float comboBonus = Math.min(comboCount / 5.0f, 0.4f); // Faster combo bonus (every 5 combos = 40% bonus)

        // Base momentum calculation
        momentum = Math.min(speedRatio + comboBonus, 1.0f);

        // Boost momentum when moving at decent speed
        if (Math.abs(velocity.x) > Constants.MAX_HORIZONTAL_SPEED * 0.4f) {
            momentum = Math.min(momentum + 0.3f, 1.0f); // 30% boost for moderate speed
        }

        // Extra boost for high speed
        if (Math.abs(velocity.x) > Constants.MAX_HORIZONTAL_SPEED * 0.7f) {
            momentum = Math.min(momentum + 0.2f, 1.0f); // Additional 20% boost for high speed
        }

        // Update position
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

        // Keep player within screen bounds horizontally - invisible walls
        if (position.x < 0) {
            position.x = 0;
            velocity.x = 0; // Stop horizontal movement when hitting wall
        } else if (position.x > Constants.WORLD_WIDTH - Constants.PLAYER_WIDTH) {
            position.x = Constants.WORLD_WIDTH - Constants.PLAYER_WIDTH;
            velocity.x = 0; // Stop horizontal movement when hitting wall
        }

        // Update bounds
        bounds.setPosition(position.x, position.y);
    }
    
    private void updateAnimationState() {
        // Determine animation state based on player state
        if (!onGround) {
            if (velocity.y > 50f) {
                currentAnimation = PlayerAnimationState.JUMPING;
            } else {
                currentAnimation = PlayerAnimationState.FALLING;
            }
        } else if (Math.abs(velocity.x) > 50f) {
            currentAnimation = PlayerAnimationState.RUNNING;
        } else {
            currentAnimation = PlayerAnimationState.IDLE;
        }
    }

    public void moveLeft(float deltaTime) {
        float acceleration = Constants.HORIZONTAL_ACCELERATION;
        // Reduce air control when not on ground
        if (!onGround) {
            acceleration *= Constants.AIR_CONTROL;
        }

        // Apply combo speed boost
        acceleration *= getComboSpeedMultiplier();

        velocity.x -= acceleration * deltaTime;

        float maxSpeed = Constants.MAX_HORIZONTAL_SPEED * getComboSpeedMultiplier();
        if (velocity.x < -maxSpeed) {
            velocity.x = -maxSpeed;
        }
    }

    public void moveRight(float deltaTime) {
        float acceleration = Constants.HORIZONTAL_ACCELERATION;
        // Reduce air control when not on ground
        if (!onGround) {
            acceleration *= Constants.AIR_CONTROL;
        }

        // Apply combo speed boost
        acceleration *= getComboSpeedMultiplier();

        velocity.x += acceleration * deltaTime;

        float maxSpeed = Constants.MAX_HORIZONTAL_SPEED * getComboSpeedMultiplier();
        if (velocity.x > maxSpeed) {
            velocity.x = maxSpeed;
        }
    }

    private float getComboSpeedMultiplier() {
        if (comboCount < Constants.COMBO_START_THRESHOLD) {
            return 1.0f; // No bonus
        }

        int comboLevels = Math.min(comboCount - Constants.COMBO_START_THRESHOLD + 1, Constants.MAX_COMBO_LEVELS);
        return 1.0f + (comboLevels * Constants.COMBO_SPEED_BOOST);
    }

    public void jump() {
        jump(null);
    }

    public void jump(com.skywarddash.utils.AssetManager assetManager) {
        // Allow jumping: on ground, coyote time, or one air jump
        if (onGround || coyoteTime > 0 || hasAirJump) {
            // Jump height based on momentum
            float jumpHeight = Constants.BASE_JUMP_HEIGHT +
                    (Constants.MAX_JUMP_HEIGHT - Constants.BASE_JUMP_HEIGHT) * momentum;
            velocity.y = jumpHeight;

            // Play jump sound
            if (assetManager != null) {
                assetManager.playSound(assetManager.jumpSound, 0.7f);
            }

            // Increment combo if we weren't on ground last frame
            if (!wasOnGround) {
                comboCount++;
            } else {
                comboCount = 1;
            }

            // Use air jump if not on ground and no coyote time
            if (!onGround && coyoteTime <= 0) {
                hasAirJump = false;
            }

            // Set not on ground after jumping and clear coyote time
            onGround = false;
            coyoteTime = 0;
        }
    }

    public void landOnPlatform() {
        onGround = true;
        velocity.y = 0;
        hasAirJump = true; // Reset air jump when landing
        coyoteTime = 0;
        
        // Stabilize horizontal movement when landing on platform if player isn't moving
        if (Math.abs(velocity.x) < 50f) {
            velocity.x *= 0.8f; // Quick stabilization for very slow movements
        }
    }

    public void resetCombo() {
        comboCount = 0;
    }

    public void render(ShapeRenderer shapeRenderer) {
        // Animate color based on current state
        float[] baseColor = Constants.PLAYER_COLOR;
        float colorPulse = (float) Math.sin(animationTime * 4f) * 0.1f + 1.0f;
        
        switch (currentAnimation) {
            case JUMPING:
                // Brighter blue when jumping
                shapeRenderer.setColor(baseColor[0] + 0.2f, baseColor[1] + 0.2f, baseColor[2], baseColor[3]);
                break;
            case RUNNING:
                // Pulsing effect when running
                shapeRenderer.setColor(baseColor[0] * colorPulse, baseColor[1] * colorPulse, baseColor[2] * colorPulse, baseColor[3]);
                break;
            case FALLING:
                // Slightly red tint when falling
                shapeRenderer.setColor(baseColor[0] + 0.1f, baseColor[1] - 0.1f, baseColor[2] - 0.1f, baseColor[3]);
                break;
            default: // IDLE
                shapeRenderer.setColor(baseColor[0], baseColor[1], baseColor[2], baseColor[3]);
                break;
        }
        
        // Draw player with animation effects
        float width = Constants.PLAYER_WIDTH;
        float height = Constants.PLAYER_HEIGHT;
        
        // Scale effect based on animation
        if (currentAnimation == PlayerAnimationState.JUMPING) {
            height *= 1.1f; // Stretch when jumping
            width *= 0.95f;
        } else if (currentAnimation == PlayerAnimationState.RUNNING) {
            // Squash effect when running
            width *= 1.0f + (float) Math.sin(animationTime * 8f) * 0.05f;
            height *= 1.0f - (float) Math.sin(animationTime * 8f) * 0.03f;
        }
        
        shapeRenderer.rect(position.x, position.y, width, height);
        
        // Add trail effect for high-speed movement
        if (Math.abs(velocity.x) > 300f) {
            float trailAlpha = Math.min(Math.abs(velocity.x) / 1000f, 0.8f);
            shapeRenderer.setColor(baseColor[0], baseColor[1], baseColor[2], trailAlpha * 0.5f);
            
            // Draw trail behind player
            float trailOffset = velocity.x > 0 ? -20f : 20f;
            shapeRenderer.rect(position.x + trailOffset, position.y, width * 0.8f, height * 0.6f);
        }
    }

    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, com.skywarddash.utils.AssetManager assetManager) {
        if (assetManager.isAssetsLoaded()) {
            com.badlogic.gdx.graphics.g2d.TextureRegion currentFrame = getCurrentAnimationFrame(assetManager);
            
            if (currentFrame != null) {
                // Determine if we should flip the sprite based on movement direction
                boolean flipX = velocity.x < 0; // Flip when moving left
                
                // Apply any scale effects based on animation state
                float width = Constants.PLAYER_WIDTH;
                float height = Constants.PLAYER_HEIGHT;
                
                if (currentAnimation == PlayerAnimationState.JUMPING) {
                    height *= 1.05f; // Slight stretch when jumping
                    width *= 0.98f;
                } else if (currentAnimation == PlayerAnimationState.RUNNING) {
                    // Subtle squash effect when running
                    width *= 1.0f + (float) Math.sin(animationTime * 12f) * 0.03f;
                    height *= 1.0f - (float) Math.sin(animationTime * 12f) * 0.02f;
                }
                
                // Draw the sprite with proper scaling and flipping
                batch.draw(currentFrame, 
                          flipX ? position.x + width : position.x, position.y, 
                          flipX ? -width : width, height);
                
                // Add speed trail effect for high-speed movement using texture
                if (Math.abs(velocity.x) > 400f && currentFrame != null) {
                    float trailAlpha = Math.min(Math.abs(velocity.x) / 1000f, 0.6f);
                    com.badlogic.gdx.graphics.Color oldColor = batch.getColor();
                    batch.setColor(1.0f, 1.0f, 1.0f, trailAlpha * 0.4f);
                    
                    // Draw trail behind player
                    float trailOffset = velocity.x > 0 ? -25f : 25f;
                    batch.draw(currentFrame, 
                              flipX ? position.x + trailOffset + width * 0.8f : position.x + trailOffset, 
                              position.y, 
                              flipX ? -width * 0.8f : width * 0.8f, height * 0.7f);
                    
                    batch.setColor(oldColor); // Restore original color
                }
                
            } else {
                // Fallback - no animation available, just draw idle frame
                batch.draw(assetManager.playerIdle, position.x, position.y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
            }
        }
    }
    
    private com.badlogic.gdx.graphics.g2d.TextureRegion getCurrentAnimationFrame(com.skywarddash.utils.AssetManager assetManager) {
        switch (currentAnimation) {
            case IDLE:
                return assetManager.playerIdle;
                
            case RUNNING:
                // Alternate between walk1 and walk2 for running animation
                float runAnimSpeed = 8f; // Higher speed = faster animation
                if (assetManager.playerRun1 != null && assetManager.playerRun2 != null) {
                    return ((int)(animationTime * runAnimSpeed) % 2 == 0) ? assetManager.playerRun1 : assetManager.playerRun2;
                } else {
                    return assetManager.playerIdle; // Fallback
                }
                
            case JUMPING:
                return assetManager.playerJump != null ? assetManager.playerJump : assetManager.playerIdle;
                
            case FALLING:
                return assetManager.playerFalling != null ? assetManager.playerFalling : assetManager.playerIdle;
                
            default:
                return assetManager.playerIdle;
        }
    }

    // Getters
    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public float getMomentum() {
        return momentum;
    }

    public int getComboCount() {
        return comboCount;
    }

    public float getCurrentSpeedMultiplier() {
        return getComboSpeedMultiplier();
    }

    // Setters
    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x, y);
    }
    
    public PlayerAnimationState getCurrentAnimation() {
        return currentAnimation;
    }
    
    public float getAnimationTime() {
        return animationTime;
    }
}
