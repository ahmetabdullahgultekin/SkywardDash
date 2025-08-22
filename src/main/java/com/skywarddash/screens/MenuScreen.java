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

public class MenuScreen implements Screen {
    private final String[] menuOptions = {"Start Game", "High Score", "Controls", "Exit"};
    private SkywardDashGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private int selectedOption = 0;

    public MenuScreen(SkywardDashGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void show() {
        // Start menu music
        game.assetManager.playMusic(game.assetManager.menuMusic, true, 0.2f);
    }

    @Override
    public void render(float delta) {
        handleInput();
        draw();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
            game.assetManager.playSound(game.assetManager.buttonClickSound, 0.3f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            selectedOption = (selectedOption + 1) % menuOptions.length;
            game.assetManager.playSound(game.assetManager.buttonClickSound, 0.3f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.assetManager.playSound(game.assetManager.buttonClickSound, 0.5f);
            selectOption();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void selectOption() {
        switch (selectedOption) {
            case 0: // Start Game
                game.setScreen(new GameScreen(game));
                break;
            case 1: // High Score - just display it
                break;
            case 2: // Controls - just display them
                break;
            case 3: // Exit
                Gdx.app.exit();
                break;
        }
    }

    private void draw() {
        // Gradient background
        ScreenUtils.clear(0.05f, 0.05f, 0.15f, 1.0f); // Darker blue background

        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw background decoration
        drawBackground();

        // Draw menu with texture-based UI
        game.batch.begin();

        // Title with shadow effect
        game.font.getData().setScale(4.5f);
        // Shadow
        game.font.setColor(0.1f, 0.1f, 0.1f, 0.9f);
        game.font.draw(game.batch, "SKYWARD DASH",
                camera.position.x - 370f, camera.position.y + 165f);
        // Main title with gradient-like effect
        game.font.setColor(0.3f, 0.9f, 1.0f, 1.0f); // Bright cyan
        game.font.draw(game.batch, "SKYWARD DASH",
                camera.position.x - 375f, camera.position.y + 170f);

        // Subtitle with glow effect
        game.font.getData().setScale(1.6f);
        game.font.setColor(0.8f, 1.0f, 0.3f, 1.0f); // Bright yellow-green
        game.font.draw(game.batch, "~ Reach for the Sky! ~",
                camera.position.x - 170f, camera.position.y + 100f);

        // Menu options with button textures
        float buttonWidth = 350f;
        float buttonHeight = 70f;
        float buttonSpacing = 90f;
        float startY = camera.position.y + 20f;

        for (int i = 0; i < menuOptions.length; i++) {
            float buttonX = camera.position.x - buttonWidth / 2;
            float buttonY = startY - i * buttonSpacing;

            // Draw button background with highlight effect
            if (i == selectedOption) {
                // Draw glow effect for selected button
                game.batch.setColor(1.0f, 1.0f, 0.5f, 0.8f); // Yellow glow
                game.batch.draw(game.assetManager.buttonSelectedTexture,
                        buttonX - 5f, buttonY - 5f, buttonWidth + 10f, buttonHeight + 10f);
                game.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
                game.batch.draw(game.assetManager.buttonSelectedTexture,
                        buttonX, buttonY, buttonWidth, buttonHeight);
            } else {
                game.batch.setColor(0.7f, 0.7f, 0.7f, 1.0f); // Dimmed for unselected
                game.batch.draw(game.assetManager.buttonTexture,
                        buttonX, buttonY, buttonWidth, buttonHeight);
                game.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
            }
        }

        // Draw text on buttons
        game.font.getData().setScale(1.8f);
        for (int i = 0; i < menuOptions.length; i++) {
            float buttonY = startY - i * buttonSpacing;

            if (i == selectedOption) {
                // Highlighted option - bright and glowing
                game.font.setColor(1.0f, 1.0f, 0.2f, 1.0f); // Bright yellow
            } else {
                // Normal option - subdued
                game.font.setColor(0.6f, 0.6f, 0.6f, 1.0f); // Gray
            }

            // Center text on button
            float textWidth = game.font.draw(game.batch, menuOptions[i], 0, 0).width;
            game.font.draw(game.batch, menuOptions[i],
                    camera.position.x - textWidth / 2, buttonY + buttonHeight / 2 + 10f);
        }

        // Info panels
        if (selectedOption == 1) {
            // High score panel
            float panelX = camera.position.x - 200f;
            float panelY = camera.position.y - 340f;
            float panelWidth = 400f;
            float panelHeight = 100f;

            game.batch.draw(game.assetManager.panelTexture, panelX, panelY, panelWidth, panelHeight);

            game.font.getData().setScale(1.5f);
            game.font.setColor(1.0f, 0.9f, 0.3f, 1.0f); // Gold color
            game.font.draw(game.batch, "High Score: " + game.assetManager.getHighScore(),
                    panelX + 50f, panelY + panelHeight / 2 + 15f);
        }

        if (selectedOption == 2) {
            // Controls panel
            float panelX = camera.position.x - 250f;
            float panelY = camera.position.y - 380f;
            float panelWidth = 500f;
            float panelHeight = 180f;

            game.batch.draw(game.assetManager.panelTexture, panelX, panelY, panelWidth, panelHeight);

            game.font.getData().setScale(1.3f);
            game.font.setColor(0.3f, 0.9f, 1.0f, 1.0f); // Cyan
            game.font.draw(game.batch, "Controls:", panelX + 30f, panelY + panelHeight - 20f);

            game.font.getData().setScale(1.1f);
            game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            game.font.draw(game.batch, "Move: Arrow Keys / WASD", panelX + 30f, panelY + panelHeight - 50f);
            game.font.draw(game.batch, "Jump: Space / Up / W (Double jump available!)", panelX + 30f, panelY + panelHeight - 80f);
            game.font.draw(game.batch, "Pause: P       Menu: ESC", panelX + 30f, panelY + panelHeight - 110f);
            game.font.setColor(0.8f, 1.0f, 0.3f, 1.0f); // Yellow-green
            game.font.draw(game.batch, "Build momentum to jump higher!", panelX + 30f, panelY + panelHeight - 140f);
        }

        // Instructions at bottom
        game.font.getData().setScale(1.2f);
        game.font.setColor(0.7f, 0.9f, 1.0f, 1.0f); // Light blue
        String instructions = "Use ↑↓ Arrow Keys to navigate, Enter to select";
        float textWidth = game.font.draw(game.batch, instructions, 0, 0).width;
        game.font.draw(game.batch, instructions,
                camera.position.x - textWidth / 2, camera.position.y - 320f);

        // Reset font color and scale
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        game.font.getData().setScale(1.0f);

        game.batch.end();
    }

    private void drawBackground() {
        // Draw animated decorative platforms in the background
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float time = System.currentTimeMillis() * 0.001f;

        // Draw floating platforms with animation
        for (int i = 0; i < 12; i++) {
            float x = (i % 4) * 300f + 100f + (float) Math.sin(time + i) * 20f;
            float y = (i / 4) * 150f + 100f + (float) Math.cos(time * 0.5f + i) * 10f;

            // Vary colors
            float alpha = 0.3f + (float) Math.sin(time + i) * 0.1f;
            if (i % 3 == 0) {
                game.shapeRenderer.setColor(0.2f, 0.6f, 0.8f, alpha); // Blue platforms
            } else if (i % 3 == 1) {
                game.shapeRenderer.setColor(0.4f, 0.8f, 0.2f, alpha); // Green platforms  
            } else {
                game.shapeRenderer.setColor(0.8f, 0.6f, 0.2f, alpha); // Orange platforms
            }

            game.shapeRenderer.rect(x, y, Constants.PLATFORM_WIDTH_MEDIUM * 0.6f, Constants.PLATFORM_THICKNESS);
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
        // Stop menu music when leaving menu screen
        game.assetManager.stopMusic(game.assetManager.menuMusic);
    }

    @Override
    public void dispose() {

    }
}