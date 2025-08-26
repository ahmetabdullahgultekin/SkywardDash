package com.skywarddash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skywarddash.SkywardDashGame;
import com.skywarddash.utils.Constants;

public class SettingsScreen implements Screen {
    private final String[] settingOptions = {
            "Master Volume", "Music Volume", "SFX Volume",
            "Resolution", "Fullscreen", "Controls", "Back to Menu"
    };
    private final String[] resolutions = {"1920x1080", "1600x900", "1366x768", "1280x720"};
    private SkywardDashGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private int selectedOption = 0;
    // Settings values
    private float masterVolume = 1.0f;
    private float musicVolume = 0.7f;
    private float sfxVolume = 0.8f;
    private int selectedResolution = 0;
    private boolean fullscreen = false;

    public SettingsScreen(SkywardDashGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);

        // Load current settings
        loadSettings();
    }

    private void loadSettings() {
        masterVolume = game.assetManager.getPreferences().getFloat("masterVolume", 1.0f);
        musicVolume = game.assetManager.getPreferences().getFloat("musicVolume", 0.7f);
        sfxVolume = game.assetManager.getPreferences().getFloat("sfxVolume", 0.8f);
        selectedResolution = game.assetManager.getPreferences().getInteger("resolution", 0);
        fullscreen = game.assetManager.getPreferences().getBoolean("fullscreen", false);
    }

    private void saveSettings() {
        game.assetManager.getPreferences().putFloat("masterVolume", masterVolume);
        game.assetManager.getPreferences().putFloat("musicVolume", musicVolume);
        game.assetManager.getPreferences().putFloat("sfxVolume", sfxVolume);
        game.assetManager.getPreferences().putInteger("resolution", selectedResolution);
        game.assetManager.getPreferences().putBoolean("fullscreen", fullscreen);
        game.assetManager.getPreferences().flush();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        handleInput();
        draw();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            selectedOption = (selectedOption - 1 + settingOptions.length) % settingOptions.length;
            game.assetManager.playSound(game.assetManager.buttonClickSound, 0.3f * sfxVolume * masterVolume);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            selectedOption = (selectedOption + 1) % settingOptions.length;
            game.assetManager.playSound(game.assetManager.buttonClickSound, 0.3f * sfxVolume * masterVolume);
        }

        // Left/Right for changing values
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            adjustSetting(-1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            adjustSetting(1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            selectOption();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            saveSettings();
            game.setScreen(new MenuScreen(game));
        }
    }

    private void adjustSetting(int direction) {
        switch (selectedOption) {
            case 0: // Master Volume
                masterVolume = Math.max(0f, Math.min(1f, masterVolume + direction * 0.1f));
                game.assetManager.playSound(game.assetManager.buttonClickSound, 0.5f * sfxVolume * masterVolume);
                break;
            case 1: // Music Volume
                musicVolume = Math.max(0f, Math.min(1f, musicVolume + direction * 0.1f));
                // Update current music volume
                if (game.assetManager.menuMusic.isPlaying()) {
                    game.assetManager.menuMusic.setVolume(musicVolume * masterVolume);
                }
                break;
            case 2: // SFX Volume
                sfxVolume = Math.max(0f, Math.min(1f, sfxVolume + direction * 0.1f));
                game.assetManager.playSound(game.assetManager.buttonClickSound, 0.5f * sfxVolume * masterVolume);
                break;
            case 3: // Resolution
                selectedResolution = (selectedResolution + direction + resolutions.length) % resolutions.length;
                break;
            case 4: // Fullscreen toggle
                if (direction != 0) {
                    fullscreen = !fullscreen;
                    // Apply fullscreen change immediately
                    applyDisplaySettings();
                }
                break;
        }
    }

    private void selectOption() {
        switch (selectedOption) {
            case 5: // Controls
                game.setScreen(new ControlsScreen(game));
                break;
            case 6: // Back to Menu
                saveSettings();
                game.setScreen(new MenuScreen(game));
                break;
        }
    }

    private void applyDisplaySettings() {
        String[] resParts = resolutions[selectedResolution].split("x");
        int width = Integer.parseInt(resParts[0]);
        int height = Integer.parseInt(resParts[1]);

        if (fullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(width, height);
        }
    }

    private void draw() {
        ScreenUtils.clear(0.05f, 0.05f, 0.15f, 1.0f);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw animated background
        drawBackground();

        game.batch.begin();

        // Title
        game.font.getData().setScale(3.5f);
        game.font.setColor(0.3f, 0.9f, 1.0f, 1.0f);
        String title = "SETTINGS";
        float titleWidth = game.font.getSpaceXadvance() * title.length() * 3.5f;
        game.font.draw(game.batch, title,
                (Constants.WORLD_WIDTH - titleWidth) / 2,
                camera.position.y + 250f);

        // Settings options
        float optionWidth = 500f;
        float optionHeight = 60f;
        float optionSpacing = 80f;
        float startY = camera.position.y + 100f;

        for (int i = 0; i < settingOptions.length; i++) {
            float optionX = camera.position.x - optionWidth / 2;
            float optionY = startY - i * optionSpacing;

            // Highlight selected option
            if (i == selectedOption) {
                game.batch.end(); // End batch before shape rendering
                game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                game.shapeRenderer.setColor(0.2f, 0.6f, 1.0f, 0.3f);
                game.shapeRenderer.rect(optionX - 10f, optionY - 10f, optionWidth + 20f, optionHeight + 10f);
                game.shapeRenderer.end();
                game.batch.begin(); // Resume batch drawing
                game.font.setColor(1.0f, 1.0f, 0.2f, 1.0f); // Bright yellow
            } else {
                game.font.setColor(0.8f, 0.8f, 0.8f, 1.0f); // Gray
            }

            game.font.getData().setScale(1.8f);
            game.font.draw(game.batch, settingOptions[i],
                    optionX, optionY + optionHeight / 2 + 10f);

            // Draw current values
            String valueText = "";
            switch (i) {
                case 0: // Master Volume
                    valueText = String.format("%.0f%%", masterVolume * 100);
                    break;
                case 1: // Music Volume
                    valueText = String.format("%.0f%%", musicVolume * 100);
                    break;
                case 2: // SFX Volume
                    valueText = String.format("%.0f%%", sfxVolume * 100);
                    break;
                case 3: // Resolution
                    valueText = resolutions[selectedResolution];
                    break;
                case 4: // Fullscreen
                    valueText = fullscreen ? "ON" : "OFF";
                    break;
                case 5: // Controls
                    valueText = "Configure";
                    break;
                case 6: // Back
                    valueText = "";
                    break;
            }

            if (!valueText.isEmpty()) {
                float valueWidth = game.font.draw(game.batch, valueText, 0, 0).width;
                game.font.draw(game.batch, valueText,
                        optionX + optionWidth - valueWidth, optionY + optionHeight / 2 + 10f);
            }
        }

        // Instructions
        game.font.getData().setScale(1.2f);
        game.font.setColor(0.7f, 0.9f, 1.0f, 1.0f);
        String instructions = "↑↓ Navigate  ←→ Adjust  Enter Select  ESC Back";
        float instructionWidth = game.font.draw(game.batch, instructions, 0, 0).width;
        game.font.draw(game.batch, instructions,
                (Constants.WORLD_WIDTH - instructionWidth) / 2,
                camera.position.y - 300f);

        // Reset font
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        game.font.getData().setScale(1.0f);

        game.batch.end();
    }

    private void drawBackground() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float time = System.currentTimeMillis() * 0.001f;

        // Draw animated settings-themed background elements
        for (int i = 0; i < 8; i++) {
            float x = (i % 4) * 400f + 200f + (float) Math.sin(time + i * 0.5f) * 30f;
            float y = (i / 4) * 300f + 200f + (float) Math.cos(time * 0.7f + i) * 20f;

            float alpha = 0.2f + (float) Math.sin(time + i) * 0.1f;
            game.shapeRenderer.setColor(0.4f, 0.6f, 0.9f, alpha);

            // Draw gear-like shapes
            for (int j = 0; j < 8; j++) {
                float angle = (float) (j * Math.PI / 4 + time * 0.5f);
                float gearX = x + (float) Math.cos(angle) * 40f;
                float gearY = y + (float) Math.sin(angle) * 40f;
                game.shapeRenderer.rect(gearX, gearY, 10f, 10f);
            }
        }

        game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        saveSettings();
    }

    @Override
    public void dispose() {
    }
}