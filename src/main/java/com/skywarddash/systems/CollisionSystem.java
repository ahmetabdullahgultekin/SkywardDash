package com.skywarddash.systems;

import com.badlogic.gdx.utils.Array;
import com.skywarddash.entities.DangerFloor;
import com.skywarddash.entities.Platform;
import com.skywarddash.entities.Player;
import com.skywarddash.utils.AssetManager;

public class CollisionSystem {

    public void checkPlatformCollisions(Player player, Array<Platform> platforms) {
        checkPlatformCollisions(player, platforms, null);
    }

    public void checkPlatformCollisions(Player player, Array<Platform> platforms, AssetManager assetManager) {
        boolean landed = false;
        float playerBottomY = player.getPosition().y;
        float playerLeftX = player.getPosition().x;
        float playerRightX = player.getPosition().x + player.getBounds().width;

        for (Platform platform : platforms) {
            float platformTopY = platform.getPosition().y + platform.getBounds().height;
            float platformBottomY = platform.getPosition().y;
            float platformLeftX = platform.getPosition().x;
            float platformRightX = platform.getPosition().x + platform.getBounds().width;

            // Check if player is horizontally overlapping with platform
            boolean horizontalOverlap = playerRightX > platformLeftX && playerLeftX < platformRightX;

            // Check if player is falling and actually ON TOP of platform
            if (horizontalOverlap && player.getVelocity().y <= 0) {
                // Player must be coming from above and land on platform surface
                // Only trigger if player's bottom is close to platform top (landing from above)
                if (playerBottomY >= platformTopY - 5f && playerBottomY <= platformTopY + 10f) {
                    // Play landing sound if not already on ground
                    boolean wasOnGround = player.isOnGround();

                    // Snap player to platform top when landing from above
                    player.setPosition(player.getPosition().x, platformTopY);
                    player.landOnPlatform();
                    platform.onPlayerLanded(player);

                    // Play landing sound effect
                    if (!wasOnGround && assetManager != null) {
                        if (platform.getType() == Platform.PlatformType.BOUNCY) {
                            assetManager.playSound(assetManager.comboSound, 0.8f);
                        } else {
                            assetManager.playSound(assetManager.landingSound, 0.6f);
                        }
                    }

                    landed = true;
                    break;
                }
            }
        }

        // Only set not on ground if we haven't landed on anything
        if (!landed) {
            player.setOnGround(false);
        }
    }

    public boolean checkDangerFloorCollision(Player player, DangerFloor dangerFloor) {
        // Player dies if they fall below danger floor height
        return player.getPosition().y + player.getBounds().height < dangerFloor.getHeight();
    }
}