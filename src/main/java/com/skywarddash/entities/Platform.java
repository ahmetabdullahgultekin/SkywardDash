package com.skywarddash.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.skywarddash.utils.Constants;

public class Platform {
    private static final String TAG = "Platform";
    // Platform behavior constants
    private static final float BOUNCY_MULTIPLIER = 1.5f;
    private static final float ICY_FRICTION_REDUCTION = 0.1f;
    private static final float FALLING_DELAY = 0.5f;
    private static final float FALLING_SPEED = 200f;
    private static final float MOVING_SPEED = 50f;
    private static final float MOVING_RANGE = 100f;
    private final Vector2 position;
    private final Rectangle bounds;
    private final PlatformType type;
    private final float width;
    private final float height;
    // Platform state
    private boolean visited;
    private boolean shouldRemove;
    private float stateTimer;
    private Vector2 velocity;
    private Vector2 originalPosition; // Store original position for respawn
    private boolean canRespawn;

    public Platform(float x, float y, PlatformType type) {
        this(x, y, type, Constants.PLATFORM_WIDTH_MEDIUM, Constants.PLATFORM_THICKNESS);
    }

    public Platform(float x, float y, PlatformType type, float width, float height) {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
        this.type = type;
        this.visited = false;
        this.shouldRemove = false;
        this.stateTimer = 0f;
        this.velocity = new Vector2();
        this.originalPosition = new Vector2(x, y);
        this.canRespawn = (type == PlatformType.BREAKABLE || type == PlatformType.FALLING);

        // Only log special platforms and every 10th platform to reduce spam
        if (type != PlatformType.NORMAL || (int) (x + y) % 500 == 0) {
            Gdx.app.log(TAG, "Created platform: " + type + " at (" + x + ", " + y + ") size: " + width + "x" + height);
        }
    }

    public Platform(float x, float y) {
        this(x, y, PlatformType.NORMAL);
    }

    public void update(float deltaTime) {
        try {
            stateTimer += deltaTime;

            switch (type) {
                case MOVING:
                    updateMovingPlatform(deltaTime);
                    break;
                case FALLING:
                    updateFallingPlatform(deltaTime);
                    break;
                case BREAKABLE:
                    updateBreakablePlatform(deltaTime);
                    break;
                default:
                    // Normal, bouncy, and icy platforms don't need continuous updates
                    break;
            }

            // Apply velocity if any
            if (!velocity.isZero()) {
                position.add(velocity.x * deltaTime, velocity.y * deltaTime);
                bounds.setPosition(position.x, position.y);
            }

        } catch (Exception e) {
            Gdx.app.error(TAG, "Error updating platform: " + e.getMessage());
        }
    }

    private void updateMovingPlatform(float deltaTime) {
        // Oscillate horizontally using sine wave
        float originalX = bounds.x - velocity.x * deltaTime; // Get original position
        float moveOffset = (float) Math.sin(stateTimer * 2.0) * MOVING_SPEED;
        velocity.x = moveOffset;
    }

    private void updateFallingPlatform(float deltaTime) {
        if (visited && stateTimer > FALLING_DELAY) {
            velocity.y = -FALLING_SPEED;
            // Instead of removing completely, respawn after some time
            if (position.y < -height) {
                if (canRespawn) {
                    respawnPlatform();
                } else {
                    shouldRemove = true;
                }
            }
        }
    }

    private void updateBreakablePlatform(float deltaTime) {
        if (visited && stateTimer > 1.0f) { // Give player time to jump off
            if (canRespawn) {
                // Hide platform temporarily, then respawn it
                position.y = -1000; // Move off screen temporarily
                if (stateTimer > 5.0f) { // Respawn after 5 seconds
                    respawnPlatform();
                }
            } else {
                shouldRemove = true;
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        float[] color = getColorForType();

        // Add visual effects based on state
        if (type == PlatformType.FALLING && visited && stateTimer > FALLING_DELAY * 0.5f) {
            // Flashing effect for falling platforms
            float flash = (float) Math.sin(stateTimer * 10) * 0.3f + 0.7f;
            shapeRenderer.setColor(color[0] * flash, color[1] * flash, color[2] * flash, color[3]);
        } else if (type == PlatformType.BREAKABLE && visited) {
            // Fading effect for breakable platforms
            float alpha = Math.max(0.3f, 1.0f - (stateTimer * 0.5f));
            shapeRenderer.setColor(color[0], color[1], color[2], alpha);
        } else {
            shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        }

        shapeRenderer.rect(position.x, position.y, width, height);
    }

    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, com.skywarddash.utils.AssetManager assetManager) {
        try {
            if (assetManager.isAssetsLoaded()) {
                com.badlogic.gdx.graphics.Texture texture = assetManager.getPlatformTexture(type.toString().toLowerCase());
                if (texture != null) {
                    // Apply visual effects based on state
                    if (type == PlatformType.FALLING && visited && stateTimer > FALLING_DELAY * 0.5f) {
                        // Flashing effect for falling platforms
                        float flash = (float) Math.sin(stateTimer * 10) * 0.3f + 0.7f;
                        batch.setColor(flash, flash, flash, 1.0f);
                    } else if (type == PlatformType.BREAKABLE && visited) {
                        // Fading effect for breakable platforms
                        float alpha = Math.max(0.3f, 1.0f - (stateTimer * 0.5f));
                        batch.setColor(1.0f, 1.0f, 1.0f, alpha);
                    } else {
                        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                    }

                    batch.draw(texture, position.x, position.y, width, height);
                    batch.setColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error rendering platform: " + e.getMessage());
        }
    }

    private float[] getColorForType() {
        switch (type) {
            case BOUNCY:
                return new float[]{1.0f, 0.8f, 0.2f, 1.0f}; // Orange
            case BREAKABLE:
                return new float[]{0.8f, 0.4f, 0.2f, 1.0f}; // Brown
            case MOVING:
                return new float[]{0.9f, 0.9f, 0.9f, 1.0f}; // White/gray
            case ICY:
                return new float[]{0.7f, 0.9f, 1.0f, 1.0f}; // Light blue
            case FALLING:
                return new float[]{1.0f, 0.3f, 0.3f, 1.0f}; // Red
            default:
                return Constants.PLATFORM_COLOR; // Green
        }
    }

    public boolean checkCollision(Rectangle playerBounds) {
        return bounds.overlaps(playerBounds);
    }

    public void onPlayerLanded(Player player) {
        try {
            if (!visited) {
                visited = true;
                stateTimer = 0f; // Reset timer when first visited
                // Only log special platform landings
                if (type != PlatformType.NORMAL) {
                    Gdx.app.log(TAG, "Player landed on " + type + " platform");
                }
            }

            switch (type) {
                case BOUNCY:
                    handleBouncyPlatform(player);
                    break;
                case BREAKABLE:
                    handleBreakablePlatform(player);
                    break;
                case ICY:
                    handleIcyPlatform(player);
                    break;
                case FALLING:
                    handleFallingPlatform(player);
                    break;
                case MOVING:
                    handleMovingPlatform(player);
                    break;
                default:
                    // Normal platform behavior
                    break;
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error handling player landing: " + e.getMessage());
        }
    }

    private void handleBouncyPlatform(Player player) {
        // Increase jump velocity for extra bounce
        player.getVelocity().y = Constants.MAX_JUMP_HEIGHT * BOUNCY_MULTIPLIER;
        Gdx.app.log(TAG, "Bouncy platform activated - jump boost applied");
    }

    private void handleBreakablePlatform(Player player) {
        // Platform will break after a delay (handled in update)
        Gdx.app.log(TAG, "Breakable platform activated - will disappear soon");
    }

    private void handleIcyPlatform(Player player) {
        // Much more aggressive icy effect - player slides significantly more
        Vector2 playerVel = player.getVelocity();
        playerVel.x *= 1.3f; // 30% speed boost instead of 10%

        // Add some random sliding effect for unpredictability
        if (Math.abs(playerVel.x) > 100f) {
            float randomSlide = (float) (Math.random() - 0.5) * 50f; // Random slide up to 25 pixels/sec each direction
            playerVel.x += randomSlide;
        }

        Gdx.app.log(TAG, "Icy platform activated - major sliding effect");
    }

    private void handleFallingPlatform(Player player) {
        // Platform will start falling after a delay (handled in update)
        Gdx.app.log(TAG, "Falling platform activated - will fall soon");
    }

    private void handleMovingPlatform(Player player) {
        // Transfer some platform velocity to player
        Vector2 playerVel = player.getVelocity();
        playerVel.x += velocity.x * 0.5f; // Partial momentum transfer
        // Remove excessive logging from moving platforms
    }

    // Getters
    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public PlatformType getType() {
        return type;
    }

    public boolean isVisited() {
        return visited;
    }

    // Setters
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void markForRemoval() {
        this.shouldRemove = true;
    }

    private void respawnPlatform() {
        // Reset platform to original position and state
        position.set(originalPosition);
        bounds.setPosition(originalPosition.x, originalPosition.y);
        velocity.set(0, 0);
        visited = false;
        stateTimer = 0f;
        Gdx.app.log(TAG, "Respawned " + type + " platform at original position");
    }

    public boolean isRespawnable() {
        return canRespawn;
    }

    public enum PlatformType {
        NORMAL("Normal platform"),
        BOUNCY("Extra jump boost"),
        BREAKABLE("Disappears after use"),
        MOVING("Slides left and right"),
        ICY("Slippery surface"),
        FALLING("Falls when stepped on");

        private final String description;

        PlatformType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}