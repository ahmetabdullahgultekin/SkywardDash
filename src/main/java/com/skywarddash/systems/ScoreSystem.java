package com.skywarddash.systems;

import com.skywarddash.entities.Player;
import com.skywarddash.utils.Constants;

public class ScoreSystem {
    private int score;
    private int highestPlatform;
    private float multiplier;

    public ScoreSystem() {
        score = 0;
        highestPlatform = 0;
        multiplier = 1.0f;
    }

    public void updateScore(Player player) {
        // Score based on height reached above starting position
        float heightAboveStart = player.getPosition().y - Constants.PLAYER_START_Y;

        // Only calculate score for positive height above starting position
        if (heightAboveStart <= 0) {
            return; // Don't score below or at starting height
        }

        int currentPlatformLevel = (int) (heightAboveStart / Constants.MAX_PLATFORM_SPACING_Y);

        if (currentPlatformLevel > highestPlatform) {
            int newPlatforms = currentPlatformLevel - highestPlatform;
            highestPlatform = currentPlatformLevel;

            // Base score for reaching new heights
            int basePoints = newPlatforms * Constants.BASE_SCORE_PER_PLATFORM;

            // Apply combo multiplier
            int comboBonus = player.getComboCount();
            multiplier = 1.0f + (comboBonus * (Constants.COMBO_MULTIPLIER - 1.0f));

            int points = (int) (basePoints * multiplier);
            score += points;
        }
    }

    public void addComboBonus(int comboCount) {
        if (comboCount > 1) {
            int bonus = comboCount * 5; // 5 points per combo level
            score += bonus;
        }
    }

    public void reset() {
        score = 0;
        highestPlatform = 0;
        multiplier = 1.0f;
    }

    // Getters
    public int getScore() {
        return score;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public int getHighestPlatform() {
        return highestPlatform;
    }
}