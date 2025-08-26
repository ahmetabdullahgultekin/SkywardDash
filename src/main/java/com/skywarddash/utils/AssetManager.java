package com.skywarddash.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class AssetManager implements Disposable {

    // Player textures and animations
    public Texture playerSpriteSheet;
    public TextureRegion playerIdle;
    public TextureRegion playerRun1, playerRun2;
    public TextureRegion playerJump;
    public TextureRegion playerFalling;
    // Platform textures
    public Texture platformNormalTexture;
    public Texture platformBouncyTexture;
    public Texture platformBreakableTexture;
    public Texture platformMovingTexture;
    public Texture platformIcyTexture;
    public Texture platformFallingTexture;
    // Background textures
    public Texture backgroundTexture;
    public Texture cloudsTexture;
    public Texture menuBackgroundTexture;
    public Texture splashBackgroundTexture;
    // UI textures
    public Texture buttonTexture;
    public Texture buttonSelectedTexture;
    public Texture panelTexture;
    // Sounds
    public Sound jumpSound;
    public Sound landingSound;
    public Sound comboSound;
    public Sound gameOverSound;
    public Sound buttonClickSound;
    // Music
    public Music backgroundMusic;
    public Music menuMusic;
    private Preferences preferences;
    // Asset loading status
    private boolean assetsLoaded = false;

    public AssetManager() {
        preferences = Gdx.app.getPreferences("SkywardDash");
        loadAssets();
    }

    private void loadAssets() {
        try {
            loadTextures();
            loadSounds();
            loadMusic();
            assetsLoaded = true;
            Gdx.app.log("AssetManager", "All assets loaded successfully!");
        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Failed to load some assets: " + e.getMessage());
            createFallbackAssets();
        }
    }

    private void loadTextures() {
        try {
            // Player textures (will be created as colored rectangles for now)
            createPlayerTextures();

            // Platform textures
            createPlatformTextures();

            // Background textures
            createBackgroundTextures();

            // UI textures
            createUITextures();

        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Texture loading failed: " + e.getMessage());
        }
    }

    private void createPlayerTextures() {
        try {
            // Load all player animation sprites from Kenney pack
            String basePath = "assets/kenney_platformer-pack-redux/PNG/Players/Variable sizes/Blue/";

            if (Gdx.files.internal(basePath + "alienBlue_stand.png").exists()) {
                playerIdle = new TextureRegion(new Texture(Gdx.files.internal(basePath + "alienBlue_stand.png")));
                Gdx.app.log("AssetManager", "Player idle animation loaded");
            }

            if (Gdx.files.internal(basePath + "alienBlue_walk1.png").exists()) {
                playerRun1 = new TextureRegion(new Texture(Gdx.files.internal(basePath + "alienBlue_walk1.png")));
                Gdx.app.log("AssetManager", "Player walk1 animation loaded");
            }

            if (Gdx.files.internal(basePath + "alienBlue_walk2.png").exists()) {
                playerRun2 = new TextureRegion(new Texture(Gdx.files.internal(basePath + "alienBlue_walk2.png")));
                Gdx.app.log("AssetManager", "Player walk2 animation loaded");
            }

            if (Gdx.files.internal(basePath + "alienBlue_jump.png").exists()) {
                playerJump = new TextureRegion(new Texture(Gdx.files.internal(basePath + "alienBlue_jump.png")));
                Gdx.app.log("AssetManager", "Player jump animation loaded");
            }

            if (Gdx.files.internal(basePath + "alienBlue_front.png").exists()) {
                playerFalling = new TextureRegion(new Texture(Gdx.files.internal(basePath + "alienBlue_front.png")));
                Gdx.app.log("AssetManager", "Player falling animation loaded");
            }

            // Fallback if any animations are missing
            if (playerIdle == null && Gdx.files.internal(basePath + "alienBlue_front.png").exists()) {
                playerIdle = new TextureRegion(new Texture(Gdx.files.internal(basePath + "alienBlue_front.png")));
            }

            Gdx.app.log("AssetManager", "Player animations loaded from Kenney pack");
            return;

        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Failed to load player animations: " + e.getMessage());
        }

        // Fallback to colored texture - scaled for 1920x1080
        playerIdle = createColoredTexture((int) Constants.PLAYER_WIDTH, (int) Constants.PLAYER_HEIGHT, 0.2f, 0.6f, 1.0f, 1.0f); // Blue player
        Gdx.app.log("AssetManager", "Using fallback player texture");
    }

    private void createPlatformTextures() {
        try {
            // Load platform textures from Kenney pack
            if (Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Grass/grassMid.png").exists()) {
                platformNormalTexture = new Texture(Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Grass/grassMid.png"));
                Gdx.app.log("AssetManager", "Normal platform texture loaded");
            } else {
                platformNormalTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 0.4f, 0.8f, 0.2f, 1.0f); // Green fallback
            }

            if (Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Stone/stoneMid.png").exists()) {
                platformBouncyTexture = new Texture(Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Stone/stoneMid.png"));
                Gdx.app.log("AssetManager", "Bouncy platform texture loaded");
            } else {
                platformBouncyTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 1.0f, 0.8f, 0.2f, 1.0f); // Orange fallback
            }

            if (Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Sand/sandMid.png").exists()) {
                platformBreakableTexture = new Texture(Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Sand/sandMid.png"));
                Gdx.app.log("AssetManager", "Breakable platform texture loaded");
            } else {
                platformBreakableTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 0.8f, 0.4f, 0.2f, 1.0f); // Brown fallback
            }

            if (Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Snow/snowMid.png").exists()) {
                platformMovingTexture = new Texture(Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Snow/snowMid.png"));
                Gdx.app.log("AssetManager", "Moving platform texture loaded");
            } else {
                platformMovingTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 0.9f, 0.9f, 0.9f, 1.0f); // White/gray moving platform
            }

            // ICY platform texture (light blue/cyan)
            if (Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Ice/iceMid.png").exists()) {
                platformIcyTexture = new Texture(Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Ice/iceMid.png"));
                Gdx.app.log("AssetManager", "Icy platform texture loaded");
            } else {
                platformIcyTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 0.7f, 0.9f, 1.0f, 1.0f); // Light blue icy platform
            }

            // FALLING platform texture (red/orange danger color)
            if (Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Cake/cakeMid.png").exists()) {
                platformFallingTexture = new Texture(Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Ground/Cake/cakeMid.png"));
                Gdx.app.log("AssetManager", "Falling platform texture loaded");
            } else {
                platformFallingTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 1.0f, 0.3f, 0.3f, 1.0f); // Red falling platform
            }

        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Failed to load platform textures: " + e.getMessage());
            // Use fallback textures - scaled for 1920x1080
            platformNormalTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 0.4f, 0.8f, 0.2f, 1.0f);
            platformBouncyTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 1.0f, 0.8f, 0.2f, 1.0f);
            platformBreakableTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 0.8f, 0.4f, 0.2f, 1.0f);
            platformMovingTexture = createColoredTextureAsTexture((int) Constants.PLATFORM_WIDTH_MEDIUM, (int) Constants.PLATFORM_THICKNESS, 0.6f, 0.2f, 1.0f, 1.0f);
        }
    }

    private void createBackgroundTextures() {
        try {
            // Load custom menu background first
            if (Gdx.files.internal("assets/images/mainmenu.png").exists()) {
                menuBackgroundTexture = new Texture(Gdx.files.internal("assets/images/mainmenu.png"));
                Gdx.app.log("AssetManager", "Menu background texture loaded");
            } else {
                menuBackgroundTexture = createGradientBackground();
            }

            // Load custom splash background
            if (Gdx.files.internal("assets/images/splash.png").exists()) {
                splashBackgroundTexture = new Texture(Gdx.files.internal("assets/images/splash.png"));
                Gdx.app.log("AssetManager", "Splash background texture loaded");
            } else {
                splashBackgroundTexture = createGradientBackground();
            }

            // Try to load background from Kenney pack for game
            if (Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Backgrounds/blue_desert.png").exists()) {
                backgroundTexture = new Texture(Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Backgrounds/blue_desert.png"));
                Gdx.app.log("AssetManager", "Background texture loaded from Kenney pack");
            } else if (Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Backgrounds/blue_grass.png").exists()) {
                backgroundTexture = new Texture(Gdx.files.internal("assets/kenney_platformer-pack-redux/PNG/Backgrounds/blue_grass.png"));
                Gdx.app.log("AssetManager", "Background texture loaded from Kenney pack");
            } else {
                // Create a more detailed gradient background programmatically
                backgroundTexture = createGradientBackground();
                Gdx.app.log("AssetManager", "Using custom gradient background");
            }

            // Create cloud texture for parallax effect
            cloudsTexture = createCloudTexture();

        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Failed to load background textures: " + e.getMessage());
            backgroundTexture = createGradientBackground();
            cloudsTexture = createCloudTexture();
        }
    }

    private void createUITextures() {
        try {
            // Load UI textures from Kenney pack
            if (Gdx.files.internal("assets/kenney_ui-pack/PNG/Blue/Default/button_rectangle_depth_flat.png").exists()) {
                buttonTexture = new Texture(Gdx.files.internal("assets/kenney_ui-pack/PNG/Blue/Default/button_rectangle_depth_flat.png"));
                Gdx.app.log("AssetManager", "Button texture loaded");
            } else {
                buttonTexture = createColoredTextureAsTexture(200, 50, 0.3f, 0.3f, 0.6f, 1.0f); // Blue button fallback
            }

            if (Gdx.files.internal("assets/kenney_ui-pack/PNG/Blue/Default/button_rectangle_depth_gradient.png").exists()) {
                buttonSelectedTexture = new Texture(Gdx.files.internal("assets/kenney_ui-pack/PNG/Blue/Default/button_rectangle_depth_gradient.png"));
                Gdx.app.log("AssetManager", "Selected button texture loaded");
            } else {
                buttonSelectedTexture = createColoredTextureAsTexture(200, 50, 0.5f, 0.5f, 0.8f, 1.0f); // Brighter blue fallback
            }

            // Panel texture (simple colored background)
            panelTexture = createColoredTextureAsTexture(300, 200, 0.2f, 0.2f, 0.2f, 0.8f); // Dark panel

        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Failed to load UI textures: " + e.getMessage());
            // Use fallback textures
            buttonTexture = createColoredTextureAsTexture(200, 50, 0.3f, 0.3f, 0.6f, 1.0f);
            buttonSelectedTexture = createColoredTextureAsTexture(200, 50, 0.5f, 0.5f, 0.8f, 1.0f);
            panelTexture = createColoredTextureAsTexture(300, 200, 0.2f, 0.2f, 0.2f, 0.8f);
        }
    }

    private TextureRegion createColoredTexture(int width, int height, float r, float g, float b, float a) {
        return new TextureRegion(createColoredTextureAsTexture(width, height, r, g, b, a));
    }

    private Texture createColoredTextureAsTexture(int width, int height, float r, float g, float b, float a) {
        // Create a simple colored texture programmatically
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(width, height, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(r, g, b, a);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createGradientBackground() {
        // Create a vertical gradient from light blue (top) to darker blue (bottom)
        int width = (int) Constants.WORLD_WIDTH;
        int height = (int) Constants.WORLD_HEIGHT;
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(width, height, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);

        for (int y = 0; y < height; y++) {
            float ratio = (float) y / height;
            // Top: Light sky blue (0.6, 0.8, 1.0) -> Bottom: Darker blue (0.1, 0.2, 0.4)
            float r = 0.6f - (ratio * 0.5f);
            float g = 0.8f - (ratio * 0.6f);
            float b = 1.0f - (ratio * 0.6f);
            pixmap.setColor(r, g, b, 1.0f);
            pixmap.drawLine(0, y, width, y);
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createCloudTexture() {
        // Create simple white cloud shapes for parallax scrolling
        int width = (int) Constants.WORLD_WIDTH;
        int height = 200; // Clouds layer height
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(width, height, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);

        pixmap.setColor(0, 0, 0, 0); // Transparent background
        pixmap.fill();

        // Draw some simple cloud shapes
        pixmap.setColor(1.0f, 1.0f, 1.0f, 0.7f); // Semi-transparent white

        // Cloud 1
        pixmap.fillCircle(200, 100, 30);
        pixmap.fillCircle(220, 110, 25);
        pixmap.fillCircle(240, 105, 20);
        pixmap.fillCircle(260, 100, 25);

        // Cloud 2
        pixmap.fillCircle(600, 150, 35);
        pixmap.fillCircle(625, 155, 30);
        pixmap.fillCircle(650, 150, 28);

        // Cloud 3
        pixmap.fillCircle(1000, 80, 25);
        pixmap.fillCircle(1020, 85, 22);
        pixmap.fillCircle(1040, 82, 20);

        // Cloud 4
        pixmap.fillCircle(1400, 130, 40);
        pixmap.fillCircle(1430, 135, 35);
        pixmap.fillCircle(1460, 130, 30);
        pixmap.fillCircle(1485, 125, 25);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void loadSounds() {
        try {
            // Load jump sound
            if (Gdx.files.internal("assets/751699__el_boss__game-jump-sound-boing-2-of-2.wav").exists()) {
                jumpSound = Gdx.audio.newSound(Gdx.files.internal("assets/751699__el_boss__game-jump-sound-boing-2-of-2.wav"));
                Gdx.app.log("AssetManager", "Jump sound loaded successfully");
            }

            // Load UI sounds from Kenney pack
            if (Gdx.files.internal("assets/kenney_interface-sounds/Audio/click_001.ogg").exists()) {
                buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("assets/kenney_interface-sounds/Audio/click_001.ogg"));
                Gdx.app.log("AssetManager", "Button click sound loaded");
            }

            if (Gdx.files.internal("assets/kenney_interface-sounds/Audio/confirmation_001.ogg").exists()) {
                comboSound = Gdx.audio.newSound(Gdx.files.internal("assets/kenney_interface-sounds/Audio/confirmation_001.ogg"));
                Gdx.app.log("AssetManager", "Combo sound loaded");
            }

            if (Gdx.files.internal("assets/kenney_interface-sounds/Audio/error_001.ogg").exists()) {
                gameOverSound = Gdx.audio.newSound(Gdx.files.internal("assets/kenney_interface-sounds/Audio/error_001.ogg"));
                Gdx.app.log("AssetManager", "Game over sound loaded");
            }

            if (Gdx.files.internal("assets/kenney_interface-sounds/Audio/drop_001.ogg").exists()) {
                landingSound = Gdx.audio.newSound(Gdx.files.internal("assets/kenney_interface-sounds/Audio/drop_001.ogg"));
                Gdx.app.log("AssetManager", "Landing sound loaded");
            }

        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Error loading sounds: " + e.getMessage());
        }
    }

    private void loadMusic() {
        try {
            // Use ambient interface sounds as background music (looped)
            if (Gdx.files.internal("assets/kenney_interface-sounds/Audio/confirmation_002.ogg").exists()) {
                backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/kenney_interface-sounds/Audio/confirmation_002.ogg"));
                backgroundMusic.setLooping(true);
                Gdx.app.log("AssetManager", "Background music loaded");
            }

            if (Gdx.files.internal("assets/kenney_interface-sounds/Audio/select_002.ogg").exists()) {
                menuMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/kenney_interface-sounds/Audio/select_002.ogg"));
                menuMusic.setLooping(true);
                Gdx.app.log("AssetManager", "Menu music loaded");
            }
        } catch (Exception e) {
            Gdx.app.log("AssetManager", "Music files not found, continuing without music");
        }
    }

    private void createFallbackAssets() {
        // Create basic fallback assets if loading fails
        createPlayerTextures();
        createPlatformTextures();
        createBackgroundTextures();
        createUITextures();
    }

    public void saveHighScore(int score) {
        int currentHigh = getHighScore();
        if (score > currentHigh) {
            preferences.putInteger("highScore", score);
            preferences.flush();
        }
    }

    public int getHighScore() {
        return preferences.getInteger("highScore", 0);
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void playSound(Sound sound) {
        if (sound != null) {
            sound.play(0.5f);
        }
    }

    public boolean isAssetsLoaded() {
        return assetsLoaded;
    }

    public Texture getPlatformTexture(String type) {
        switch (type.toLowerCase()) {
            case "bouncy":
                return platformBouncyTexture;
            case "breakable":
                return platformBreakableTexture;
            case "moving":
                return platformMovingTexture;
            case "icy":
                return platformIcyTexture;
            case "falling":
                return platformFallingTexture;
            default:
                return platformNormalTexture;
        }
    }

    public void playSound(Sound sound, float volume) {
        if (sound != null) {
            sound.play(volume);
        }
    }

    public void playMusic(Music music, boolean loop, float volume) {
        if (music != null) {
            music.setLooping(loop);
            music.setVolume(volume);
            if (!music.isPlaying()) {
                music.play();
            }
        }
    }

    public void stopMusic(Music music) {
        if (music != null && music.isPlaying()) {
            music.stop();
        }
    }

    @Override
    public void dispose() {
        // Dispose sounds
        if (jumpSound != null) jumpSound.dispose();
        if (landingSound != null) landingSound.dispose();
        if (comboSound != null) comboSound.dispose();
        if (gameOverSound != null) gameOverSound.dispose();
        if (buttonClickSound != null) buttonClickSound.dispose();

        // Dispose music
        if (backgroundMusic != null) backgroundMusic.dispose();
        if (menuMusic != null) menuMusic.dispose();

        // Dispose textures
        if (playerSpriteSheet != null) playerSpriteSheet.dispose();
        if (platformNormalTexture != null) platformNormalTexture.dispose();
        if (platformBouncyTexture != null) platformBouncyTexture.dispose();
        if (platformBreakableTexture != null) platformBreakableTexture.dispose();
        if (platformMovingTexture != null) platformMovingTexture.dispose();
        if (platformIcyTexture != null) platformIcyTexture.dispose();
        if (platformFallingTexture != null) platformFallingTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (cloudsTexture != null) cloudsTexture.dispose();
        if (menuBackgroundTexture != null) menuBackgroundTexture.dispose();
        if (splashBackgroundTexture != null) splashBackgroundTexture.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
        if (buttonSelectedTexture != null) buttonSelectedTexture.dispose();
        if (panelTexture != null) panelTexture.dispose();
    }
}