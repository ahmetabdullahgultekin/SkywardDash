package com.skywarddash.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.skywarddash.entities.Player;
import com.skywarddash.utils.Constants;

public class CameraController {
    private static final String TAG = "CameraController";

    private final OrthographicCamera camera;
    private float targetY;
    private float highestPlayerY; // Track highest point reached by player

    public CameraController(OrthographicCamera camera) {
        this.camera = camera;
        this.targetY = camera.position.y;
        this.highestPlayerY = camera.position.y;
    }

    public void update(Player player, float deltaTime) {
        try {
            float playerY = player.getPosition().y + Constants.PLAYER_HEIGHT / 2;

            // Update highest player position
            if (playerY > highestPlayerY) {
                highestPlayerY = playerY;
            }

            // Calculate camera boundaries
            float safeZoneBottom = camera.position.y - Constants.CAMERA_SAFE_ZONE;
            float safeZoneTop = camera.position.y + Constants.CAMERA_SAFE_ZONE;

            // Always follow player upward movement immediately
            if (playerY > safeZoneTop) {
                float desiredCameraY = playerY - Constants.CAMERA_SAFE_ZONE;
                targetY = Math.max(targetY, desiredCameraY);
            }

            // Follow player downward movement more aggressively
            if (playerY < safeZoneBottom) {
                float playerVelY = player.getVelocity().y;

                // Follow downward movement much more readily
                if (playerVelY < -50f || playerY < camera.position.y - Constants.CAMERA_SAFE_ZONE * 1.5f) {
                    float desiredCameraY = playerY + Constants.CAMERA_SAFE_ZONE;

                    // Allow camera to go much lower - only limit to ground level
                    float minCameraY = 100f; // Ground level minimum
                    desiredCameraY = Math.max(desiredCameraY, minCameraY);

                    // Always follow down if player is below safe zone
                    if (desiredCameraY < targetY || playerY < safeZoneBottom) {
                        targetY = desiredCameraY;
                    }
                }
            }

            // Much more responsive camera movement
            float currentY = camera.position.y;
            float lerpSpeed;

            if (targetY > currentY) {
                // Moving up - very fast response
                lerpSpeed = Constants.CAMERA_SMOOTH_SPEED * 3.0f * deltaTime;
            } else {
                // Moving down - also fast response now
                lerpSpeed = Constants.CAMERA_SMOOTH_SPEED * 2.5f * deltaTime;
            }

            // Apply smooth movement
            float newY = MathUtils.lerp(currentY, targetY, Math.min(lerpSpeed, 0.8f));
            camera.position.y = newY;

            // Keep horizontal position centered
            camera.position.x = Constants.WORLD_WIDTH / 2;

            camera.update();

        } catch (Exception e) {
            Gdx.app.error(TAG, "Error updating camera: " + e.getMessage());
        }
    }

    public void reset() {
        targetY = camera.position.y;
        highestPlayerY = camera.position.y;
    }
}