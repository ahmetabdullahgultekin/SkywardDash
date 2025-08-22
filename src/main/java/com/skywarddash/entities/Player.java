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

        // Update coyote time
        if (wasOnGround && !onGround) {
            coyoteTime = MAX_COYOTE_TIME;
        } else if (coyoteTime > 0) {
            coyoteTime -= deltaTime;
        }

        // Apply gravity only if not on ground
        if (!onGround) {
            velocity.y += Constants.GRAVITY * deltaTime;
            // Apply terminal velocity limit
            if (velocity.y < Constants.TERMINAL_VELOCITY) {
                velocity.y = Constants.TERMINAL_VELOCITY;
            }
        }

        // Apply horizontal friction when on ground - reduced for more sliding
        if (onGround) {
            velocity.x *= 0.95f; // Much less friction (was Constants.FRICTION)
        } else {
            // Even less friction in air for more momentum preservation
            velocity.x *= 0.98f;
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
    }

    public void resetCombo() {
        comboCount = 0;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Constants.PLAYER_COLOR[0], Constants.PLAYER_COLOR[1],
                Constants.PLAYER_COLOR[2], Constants.PLAYER_COLOR[3]);
        shapeRenderer.rect(position.x, position.y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
    }

    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, com.skywarddash.utils.AssetManager assetManager) {
        if (assetManager.isAssetsLoaded() && assetManager.playerIdle != null) {
            // Use texture rendering
            batch.draw(assetManager.playerIdle, position.x, position.y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
        } else {
            // Fallback to colored rectangle (will need to convert batch to shapeRenderer)
            // For now, this method should be used when texture is available
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
}
