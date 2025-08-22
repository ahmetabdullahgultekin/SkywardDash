package com.skywarddash.utils;

public class Constants {
    // Screen dimensions - Full HD
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;
    public static final float WORLD_WIDTH = 1920.0f;
    public static final float WORLD_HEIGHT = 1080.0f;

    // Physics constants - More responsive and slippery
    public static final float GRAVITY = -600f; // Lighter gravity for better platforming
    public static final float TERMINAL_VELOCITY = -1000f; // Terminal velocity
    public static final float BASE_JUMP_HEIGHT = 400f; // Stronger base jump
    public static final float MAX_JUMP_HEIGHT = 600f; // Much stronger momentum jump
    public static final float HORIZONTAL_ACCELERATION = 1800f; // Much more responsive acceleration
    public static final float MAX_HORIZONTAL_SPEED = 750f; // Higher max speed for more sliding feel
    public static final float FRICTION = 0.90f; // Even less friction for more sliding
    public static final float AIR_CONTROL = 0.8f; // Better air control

    // Player constants - Scaled from original 25-30x40-45 pixels
    public static final float PLAYER_WIDTH = 90f; // 30 pixels * 3 scale
    public static final float PLAYER_HEIGHT = 135f; // 45 pixels * 3 scale
    public static final float PLAYER_START_X = WORLD_WIDTH / 2 - PLAYER_WIDTH / 2;
    public static final float PLAYER_START_Y = 150f + 45f; // Ground level + platform thickness

    // Platform constants - Progressive difficulty system
    public static final float PLATFORM_THICKNESS = 45f; // 15 pixels * 3 scale

    // Platform widths for different difficulties
    public static final float PLATFORM_WIDTH_LARGE = 450f; // 150 pixels * 3 scale
    public static final float PLATFORM_WIDTH_MEDIUM = 300f; // 100 pixels * 3 scale  
    public static final float PLATFORM_WIDTH_SMALL = 180f; // 60 pixels * 3 scale
    public static final float PLATFORM_WIDTH_TINY = 120f; // 40 pixels * 3 scale

    // Vertical spacing between platforms (increased for less crowding)
    public static final float MIN_PLATFORM_SPACING_Y = 140f; // Increased from 100f
    public static final float MAX_PLATFORM_SPACING_Y = 220f; // Increased from 180f

    // Game mechanics - Progressive difficulty
    public static final float RISING_FLOOR_SPEED = 8f; // Slower rising floor for better gameplay
    public static final float COMBO_MULTIPLIER = 2.0f; // x2, x3, x4 per combo level
    public static final int BASE_SCORE_PER_PLATFORM = 10;
    public static final float CAMERA_SMOOTH_SPEED = 8f; // Smooth camera movement

    // Combo system constants
    public static final int COMBO_START_THRESHOLD = 3; // 3 consecutive jumps to start combo
    public static final float COMBO_SPEED_BOOST = 0.25f; // 25% speed increase per combo level
    public static final int MAX_COMBO_LEVELS = 10; // Maximum combo multiplier

    // Progressive difficulty floors (based on score/height)
    public static final int FLOOR_EASY = 10; // Floors 0-10: large platforms
    public static final int FLOOR_MEDIUM = 50; // Floors 10-50: medium platforms  
    public static final int FLOOR_HARD = 100; // Floors 50-100: small platforms
    public static final int FLOOR_EXTREME = 200; // Floors 100+: tiny platforms

    // Camera system
    public static final float CAMERA_SAFE_ZONE = WORLD_HEIGHT * 0.33f; // Bottom 1/3 is safe
    public static final float CAMERA_DANGER_ZONE = 100f; // Reasonable danger zone

    // Colors (for simple graphics)
    public static final float[] PLAYER_COLOR = {0.2f, 0.6f, 1.0f, 1.0f}; // Blue
    public static final float[] PLATFORM_COLOR = {0.4f, 0.8f, 0.2f, 1.0f}; // Green
    public static final float[] DANGER_FLOOR_COLOR = {1.0f, 0.2f, 0.2f, 1.0f}; // Red
    public static final float[] BACKGROUND_COLOR = {0.1f, 0.1f, 0.2f, 1.0f}; // Dark blue
}