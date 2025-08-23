package com.skywarddash.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.skywarddash.entities.Platform;
import com.skywarddash.entities.Player;

public class PlatformGenerator {
    private Array<Platform> platforms;
    private float lastPlatformY;
    private float lastPlatformX;
    private int currentFloor;

    public PlatformGenerator() {
        platforms = new Array<>();
        lastPlatformY = 0;
        lastPlatformX = Constants.WORLD_WIDTH / 2;
        currentFloor = 0;

        // Generate initial platforms
        generateInitialPlatforms();
    }

    private void generateInitialPlatforms() {
        // Create multiple ground platforms to form a wide base like Icy Tower (scaled for 1920x1080)
        float groundY = 150f; // Scaled for 1920x1080
        float platformWidth = Constants.PLATFORM_WIDTH_LARGE; // Start with large platforms
        int groundPlatforms = 5; // Create 5 large platforms side by side
        float totalWidth = groundPlatforms * platformWidth;
        float startX = (Constants.WORLD_WIDTH - totalWidth) / 2; // Center the ground

        // Create the ground platforms
        for (int i = 0; i < groundPlatforms; i++) {
            float x = startX + (i * platformWidth);
            platforms.add(new Platform(x, groundY, Platform.PlatformType.NORMAL, platformWidth, Constants.PLATFORM_THICKNESS));
        }

        // Set initial values for platform generation
        lastPlatformY = groundY + Constants.MIN_PLATFORM_SPACING_Y;
        lastPlatformX = Constants.WORLD_WIDTH / 2;
        currentFloor = 0; // Start from floor 0

        // Generate many platforms initially to fill the screen (scaled for 1920x1080)
        // Don't increment floor counter for initial platforms
        for (int i = 0; i < 20; i++) {
            generateInitialPlatform();
        }
    }

    public void update(Player player) {
        // Generate new platforms ahead of player
        generatePlatformsAhead(player);

        // Don't remove platforms here - let GameScreen handle it
        // This prevents platforms from disappearing too early
    }

    public void removePlatforms(java.util.function.Predicate<Platform> shouldRemove) {
        try {
            // LibGDX Array doesn't support Predicate-based removal, so we need to iterate manually
            for (int i = platforms.size - 1; i >= 0; i--) {
                Platform platform = platforms.get(i);
                if (shouldRemove.test(platform)) {
                    platforms.removeIndex(i);
                }
            }
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("PlatformGenerator", "Error removing platforms: " + e.getMessage());
        }
    }

    private void generatePlatformsAhead(Player player) {
        // Generate platforms ahead of the player to ensure there's always something to land on
        float playerY = player.getPosition().y;
        float highestPlatformY = getHighestPlatformY();

        // Generate platforms if player is getting close to the top
        while (highestPlatformY < playerY + Constants.WORLD_HEIGHT * 3) {
            // Check if we need to create a celebration/rest floor
            if ((currentFloor + 1) % 100 == 0 && (currentFloor + 1) <= 300) {
                generateCelebrationFloor();
            } else {
                generateNextPlatform();
            }
            highestPlatformY = getHighestPlatformY();
        }
    }

    private float getHighestPlatformY() {
        float highest = 0;
        for (Platform platform : platforms) {
            if (platform.getPosition().y > highest) {
                highest = platform.getPosition().y;
            }
        }
        return highest;
    }

    private void generateInitialPlatform() {
        // Generate platform WITHOUT incrementing floor counter
        generatePlatformInternal(false);
    }

    private void generateNextPlatform() {
        // Generate platform WITH incrementing floor counter
        generatePlatformInternal(true);
    }

    private void generatePlatformInternal(boolean incrementFloor) {
        // Increment floor counter only if specified
        if (incrementFloor) {
            currentFloor++;
        }

        // Calculate platform width based on current floor (progressive difficulty)
        float basePlatformWidth = getPlatformWidthForFloor(currentFloor);

        // Add random width variation (±20% of base width)
        float widthVariation = MathUtils.random(-0.2f, 0.2f);
        float platformWidth = basePlatformWidth * (1.0f + widthVariation);

        // Ensure minimum width
        platformWidth = Math.max(platformWidth, 90f); // Minimum platform width

        // Calculate vertical spacing (increases with difficulty)
        float minSpacing = Constants.MIN_PLATFORM_SPACING_Y;
        float maxSpacing = Constants.MAX_PLATFORM_SPACING_Y;

        // Make platforms farther apart at higher levels
        if (currentFloor > Constants.FLOOR_MEDIUM) {
            minSpacing *= 1.2f;
            maxSpacing *= 1.3f;
        }
        if (currentFloor > Constants.FLOOR_HARD) {
            minSpacing *= 1.3f;
            maxSpacing *= 1.4f;
        }

        float spacingY = MathUtils.random(minSpacing, maxSpacing);

        // Calculate horizontal spacing (more challenging at higher levels)
        float maxHorizontalGap = platformWidth * 2f; // Platform can be up to 2 platform widths away
        if (currentFloor > Constants.FLOOR_MEDIUM) {
            maxHorizontalGap *= 1.5f;
        }

        float spacingX = MathUtils.random(-maxHorizontalGap, maxHorizontalGap);

        lastPlatformY += spacingY;
        lastPlatformX += spacingX;

        // Keep platforms within screen bounds (accounting for platform width)
        if (lastPlatformX < 0) {
            lastPlatformX = 0;
        } else if (lastPlatformX > Constants.WORLD_WIDTH - platformWidth) {
            lastPlatformX = Constants.WORLD_WIDTH - platformWidth;
        }

        // Determine platform type based on height (higher = more special platforms)
        Platform.PlatformType type = determinePlatformType(currentFloor);

        platforms.add(new Platform(lastPlatformX, lastPlatformY, type, platformWidth, Constants.PLATFORM_THICKNESS));
    }

    private void generateCelebrationFloor() {
        // Create full-width celebration platform every 100 floors (like Icy Tower)
        currentFloor++;
        
        com.badlogic.gdx.Gdx.app.log("PlatformGenerator", "Creating celebration floor " + currentFloor + "!");

        lastPlatformY += Constants.MIN_PLATFORM_SPACING_Y * 3; // Extra spacing for celebration

        // Create multiple large platforms side by side to cover full width - no gaps for safety
        float platformWidth = Constants.PLATFORM_WIDTH_LARGE;
        int platformCount = (int) Math.ceil(Constants.WORLD_WIDTH / platformWidth);
        float startX = 0;

        for (int i = 0; i < platformCount; i++) {
            float x = startX + (i * platformWidth);
            if (x < Constants.WORLD_WIDTH) {
                // Make sure the last platform covers to the edge
                float actualWidth = platformWidth;
                if (i == platformCount - 1) {
                    actualWidth = Constants.WORLD_WIDTH - x;
                }
                platforms.add(new Platform(x, lastPlatformY, Platform.PlatformType.BOUNCY, actualWidth, Constants.PLATFORM_THICKNESS));
            }
        }

        // Update last platform position to center for next generation
        lastPlatformX = Constants.WORLD_WIDTH / 2;
    }
    
    private void generateRestPlatform() {
        // Legacy method - now use generateCelebrationFloor instead
        generateCelebrationFloor();
    }

    private float getPlatformWidthForFloor(int floor) {
        if (floor <= Constants.FLOOR_EASY) {
            return Constants.PLATFORM_WIDTH_LARGE;
        } else if (floor <= Constants.FLOOR_MEDIUM) {
            return Constants.PLATFORM_WIDTH_MEDIUM;
        } else if (floor <= Constants.FLOOR_HARD) {
            return Constants.PLATFORM_WIDTH_SMALL;
        } else {
            return Constants.PLATFORM_WIDTH_TINY;
        }
    }

    private Platform.PlatformType determinePlatformType(int floor) {
        // Higher floors have more chance of being special
        float specialChance = 0.1f; // Base 10% chance

        if (floor > Constants.FLOOR_EASY) {
            specialChance = 0.2f; // 20% chance after floor 10
        }
        if (floor > Constants.FLOOR_MEDIUM) {
            specialChance = 0.3f; // 30% chance after floor 50
        }
        if (floor > Constants.FLOOR_HARD) {
            specialChance = 0.4f; // 40% chance after floor 100
        }

        if (MathUtils.random() < specialChance) {
            float typeRoll = MathUtils.random();
            if (typeRoll < 0.25f) {
                return Platform.PlatformType.BOUNCY; // 25% - Extra jump boost
            } else if (typeRoll < 0.45f) {
                return Platform.PlatformType.MOVING; // 20% - Slides left/right
            } else if (typeRoll < 0.65f) {
                return Platform.PlatformType.BREAKABLE; // 20% - Disappears after use
            } else if (typeRoll < 0.85f) {
                return Platform.PlatformType.ICY; // 20% - Slippery surface
            } else {
                return Platform.PlatformType.FALLING; // 15% - Falls when stepped on
            }
        }

        return Platform.PlatformType.NORMAL;
    }

    public Array<Platform> getPlatforms() {
        return platforms;
    }

    public void reset() {
        platforms.clear();
        lastPlatformY = 150f; // Scaled ground level
        lastPlatformX = Constants.WORLD_WIDTH / 2;
        currentFloor = 0;
        generateInitialPlatforms();
    }

    public int getCurrentFloor() {
        return currentFloor;
    }
    
    public boolean shouldCelebrate(int floor) {
        for (int celebrationFloor : Constants.CELEBRATION_FLOORS) {
            if (floor == celebrationFloor) {
                return true;
            }
        }
        return false;
    }
}