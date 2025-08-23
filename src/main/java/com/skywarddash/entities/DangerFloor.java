package com.skywarddash.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.skywarddash.utils.Constants;

public class DangerFloor {
    private float height;
    private Rectangle bounds;
    private float speed;

    public DangerFloor(float startHeight) {
        this.height = startHeight;
        this.bounds = new Rectangle(0, startHeight, Constants.WORLD_WIDTH, 1.0f);
        this.speed = Constants.RISING_FLOOR_SPEED;
    }

    public void update(float deltaTime, int score, float gameTime, float playerY) {
        // Longer grace period for better early game experience
        float gracePeriod = 10.0f; // Start rising after 10 seconds (like Icy Tower)

        if (gameTime > gracePeriod) {
            // Progressive speed system based on player height/score
            float baseSpeedMultiplier = 0.8f; // Much slower base speed for early game
            
            // Score-based progression (very gradual)
            float scoreSpeedMultiplier = 1.0f + Math.max(0, (score - 100) / 500.0f) * 0.15f;
            
            // Time-based progression (slower acceleration)
            float timeSpeedMultiplier = 1.0f + ((gameTime - gracePeriod) / 60.0f) * 0.2f;
            
            // Height-based progression - speed up as player gets higher
            float heightBonus = Math.max(0, playerY - 500f) / 2000f; // Bonus after 500px height
            
            float totalSpeedMultiplier = baseSpeedMultiplier * scoreSpeedMultiplier * 
                                       timeSpeedMultiplier * (1.0f + heightBonus);

            // Always rise at calculated speed
            height += speed * totalSpeedMultiplier * deltaTime;

            bounds.setPosition(0, height);
        }
    }

    public void update(float deltaTime, int score, float gameTime) {
        // Fallback method - rises normally if no player position given
        float gracePeriod = 5.0f; // 5 seconds of grace time like Icy Tower

        if (gameTime > gracePeriod) {
            float speedMultiplier = 1.0f + (score / 1000.0f) * 0.1f;
            height += speed * speedMultiplier * deltaTime;
            bounds.setPosition(0, height);
        }
    }

    // Keep the old method for backward compatibility
    public void update(float deltaTime, int score) {
        update(deltaTime, score, 0);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Constants.DANGER_FLOOR_COLOR[0], Constants.DANGER_FLOOR_COLOR[1],
                Constants.DANGER_FLOOR_COLOR[2], Constants.DANGER_FLOOR_COLOR[3]);
        shapeRenderer.rect(0, height, Constants.WORLD_WIDTH, 1.0f);

        // Add a gradient effect by drawing multiple layers
        for (int i = 1; i <= 5; i++) {
            float alpha = 1.0f - (i * 0.15f);
            if (alpha > 0) {
                shapeRenderer.setColor(Constants.DANGER_FLOOR_COLOR[0], Constants.DANGER_FLOOR_COLOR[1],
                        Constants.DANGER_FLOOR_COLOR[2], alpha);
                shapeRenderer.rect(0, height - i * 0.1f, Constants.WORLD_WIDTH, 0.1f);
            }
        }
    }

    public boolean checkCollision(Rectangle playerBounds) {
        return playerBounds.y <= height;
    }

    // Getters
    public float getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}