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
    private final String[] menuOptions = {"Start Game", "Settings", "High Score", "Help", "About", "Exit"};
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
            case 1: // Settings
                game.setScreen(new SettingsScreen(game));
                break;
            case 2: // High Score - just display it
                break;
            case 3: // Help
                game.setScreen(new HelpScreen(game));
                break;
            case 4: // About
                game.setScreen(new AboutScreen(game));
                break;
            case 5: // Exit
                Gdx.app.exit();
                break;
        }
    }

    private void draw() {
        // Clear with dark background
        ScreenUtils.clear(0.05f, 0.05f, 0.15f, 1.0f);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw custom background image first - fit to height, center horizontally
        if (game.assetManager.menuBackgroundTexture != null) {
            game.batch.begin();
            
            // Get original texture dimensions
            float textureWidth = game.assetManager.menuBackgroundTexture.getWidth();
            float textureHeight = game.assetManager.menuBackgroundTexture.getHeight();
            
            // Calculate scale to fit height while maintaining aspect ratio
            float scale = Constants.WORLD_HEIGHT / textureHeight;
            float scaledWidth = textureWidth * scale;
            
            // Align the image to the left
            float bgX = 0;
            
            game.batch.draw(game.assetManager.menuBackgroundTexture, 
                           bgX, 0, scaledWidth, Constants.WORLD_HEIGHT);
            game.batch.end();
        }

        // Removed animated background decorations - clean interface

        // Draw semi-transparent overlay behind menu for better readability
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0.0f, 0.0f, 0.1f, 0.6f); // Dark blue semi-transparent
        
        if (game.assetManager.menuBackgroundTexture != null) {
            float textureWidth = game.assetManager.menuBackgroundTexture.getWidth();
            float textureHeight = game.assetManager.menuBackgroundTexture.getHeight();
            float scale = Constants.WORLD_HEIGHT / textureHeight;
            float scaledWidth = textureWidth * scale;
            float bgX = 0; // Background is left-aligned
            
            // Draw overlay only in right empty space where menu is located
            if (scaledWidth < Constants.WORLD_WIDTH - 400f) {
                // Menu on right side - overlay the right empty area
                game.shapeRenderer.rect(scaledWidth, 0, Constants.WORLD_WIDTH - scaledWidth, Constants.WORLD_HEIGHT);
            } else {
                // Menu centered - add overlay around menu area
                game.shapeRenderer.rect(Constants.WORLD_WIDTH * 0.25f, Constants.WORLD_HEIGHT * 0.15f, 
                                      Constants.WORLD_WIDTH * 0.5f, Constants.WORLD_HEIGHT * 0.7f);
            }
        } else {
            // No background image - overlay centered menu area
            game.shapeRenderer.rect(Constants.WORLD_WIDTH * 0.25f, Constants.WORLD_HEIGHT * 0.15f, 
                                  Constants.WORLD_WIDTH * 0.5f, Constants.WORLD_HEIGHT * 0.7f);
        }
        game.shapeRenderer.end();

        // Draw menu with texture-based UI
        game.batch.begin();

        // Calculate menu positioning based on background image (left-aligned)
        float menuCenterX = camera.position.x;
        if (game.assetManager.menuBackgroundTexture != null) {
            float textureWidth = game.assetManager.menuBackgroundTexture.getWidth();
            float textureHeight = game.assetManager.menuBackgroundTexture.getHeight();
            float scale = Constants.WORLD_HEIGHT / textureHeight;
            float scaledWidth = textureWidth * scale;
            float bgX = 0; // Background is left-aligned
            
            // If there's empty space on the right, position menu there
            if (scaledWidth < Constants.WORLD_WIDTH - 400f) {
                menuCenterX = scaledWidth + (Constants.WORLD_WIDTH - scaledWidth) / 2; // Center in right empty space
            }
            // Otherwise keep centered
        }

        // Title with shadow effect - positioned relative to menu center
        game.font.getData().setScale(4.5f);
        // Shadow
        game.font.setColor(0.1f, 0.1f, 0.1f, 0.9f);
        game.font.draw(game.batch, "SKYWARD DASH",
                menuCenterX - 370f, camera.position.y + 300f);
        // Main title with gradient-like effect
        game.font.setColor(0.3f, 0.9f, 1.0f, 1.0f); // Bright cyan
        game.font.draw(game.batch, "SKYWARD DASH",
                menuCenterX - 375f, camera.position.y + 305f);

        // Subtitle with glow effect
        game.font.getData().setScale(1.6f);
        game.font.setColor(0.8f, 1.0f, 0.3f, 1.0f); // Bright yellow-green
        game.font.draw(game.batch, "~ Reach for the Sky! ~",
                menuCenterX - 170f, camera.position.y + 235f);

        // Menu options with button textures - Properly centered vertically
        float buttonWidth = 350f;
        float buttonHeight = 70f;
        float buttonSpacing = 85f;
        
        // Calculate total height of all buttons and center them vertically
        float totalMenuHeight = menuOptions.length * buttonHeight + (menuOptions.length - 1) * buttonSpacing;
        float startY = camera.position.y + (totalMenuHeight / 2) - buttonHeight;

        for (int i = 0; i < menuOptions.length; i++) {
            float buttonX = menuCenterX - buttonWidth / 2;
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

            // Center text on button - properly calculate width and center
            game.font.getData().setScale(1.8f); // Ensure scale is set for measurement
            float textWidth = 0;
            // Measure text width properly
            com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout();
            layout.setText(game.font, menuOptions[i]);
            textWidth = layout.width;
            
            game.font.draw(game.batch, menuOptions[i],
                    menuCenterX - textWidth / 2, buttonY + buttonHeight / 2 + 10f);
        }

        // Info panels - Fixed positioning to avoid overlap and use menu center
        if (selectedOption == 2) {
            // High score panel - positioned below menu buttons
            float panelX = menuCenterX - 200f;
            float panelY = startY - menuOptions.length * buttonSpacing - 50f; // Below all buttons
            float panelWidth = 400f;
            float panelHeight = 80f; // Reduced height

            game.batch.draw(game.assetManager.panelTexture, panelX, panelY, panelWidth, panelHeight);

            game.font.getData().setScale(1.5f);
            game.font.setColor(1.0f, 0.9f, 0.3f, 1.0f); // Gold color
            game.font.draw(game.batch, "High Score: " + game.assetManager.getHighScore(),
                    panelX + 50f, panelY + panelHeight / 2 + 15f);
        }

        if (selectedOption == 3) {
            // Help panel preview
            float panelX = menuCenterX - 250f;
            float panelY = startY - menuOptions.length * buttonSpacing - 50f; // Below all buttons
            float panelWidth = 500f;
            float panelHeight = 100f;

            game.batch.draw(game.assetManager.panelTexture, panelX, panelY, panelWidth, panelHeight);

            game.font.getData().setScale(1.3f);
            game.font.setColor(0.3f, 0.9f, 1.0f, 1.0f); // Cyan
            game.font.draw(game.batch, "Need Help?", panelX + 30f, panelY + panelHeight - 20f);

            game.font.getData().setScale(1.1f);
            game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            game.font.draw(game.batch, "Learn controls, gameplay mechanics,", panelX + 30f, panelY + panelHeight - 50f);
            game.font.draw(game.batch, "tips and strategies for high scores!", panelX + 30f, panelY + panelHeight - 75f);
        }

        if (selectedOption == 4) {
            // About panel preview
            float panelX = menuCenterX - 250f;
            float panelY = startY - menuOptions.length * buttonSpacing - 50f; // Below all buttons
            float panelWidth = 500f;
            float panelHeight = 100f;

            game.batch.draw(game.assetManager.panelTexture, panelX, panelY, panelWidth, panelHeight);

            game.font.getData().setScale(1.3f);
            game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f); // Gold
            game.font.draw(game.batch, "About Skyward Dash", panelX + 30f, panelY + panelHeight - 20f);

            game.font.getData().setScale(1.1f);
            game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            game.font.draw(game.batch, "Version 1.0 - Rolling Cat Software", panelX + 30f, panelY + panelHeight - 50f);
            game.font.draw(game.batch, "Credits, version info, and more!", panelX + 30f, panelY + panelHeight - 75f);
        }

        // Instructions at bottom - positioned below panels
        game.font.getData().setScale(1.2f);
        game.font.setColor(0.7f, 0.9f, 1.0f, 1.0f); // Light blue
        String instructions = "Use ↑↓ Arrow Keys to navigate, Enter to select";
        com.badlogic.gdx.graphics.g2d.GlyphLayout instructionsLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout();
        instructionsLayout.setText(game.font, instructions);
        float textWidth = instructionsLayout.width;
        float instructionY = startY - menuOptions.length * buttonSpacing - 250f; // Well below panels
        game.font.draw(game.batch, instructions,
                menuCenterX - textWidth / 2, instructionY);

        // Reset font color and scale
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        game.font.getData().setScale(1.0f);

        game.batch.end();
    }

    private void drawBackground() {
        // Draw enhanced animated background
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float time = System.currentTimeMillis() * 0.001f;

        // Draw floating platforms with enhanced animation
        for (int i = 0; i < 15; i++) {
            float x = (i % 5) * 300f + 100f + (float) Math.sin(time * 0.7f + i) * 25f;
            float y = (i / 5) * 200f + 100f + (float) Math.cos(time * 0.5f + i) * 15f;

            // Enhanced color variations with glow effect
            float alpha = 0.25f + (float) Math.sin(time * 2f + i) * 0.15f;
            float pulseSize = 1.0f + (float) Math.sin(time * 3f + i * 0.5f) * 0.1f;
            
            if (i % 5 == 0) {
                game.shapeRenderer.setColor(0.2f, 0.6f, 1.0f, alpha); // Bright blue
            } else if (i % 5 == 1) {
                game.shapeRenderer.setColor(0.4f, 0.9f, 0.2f, alpha); // Bright green
            } else if (i % 5 == 2) {
                game.shapeRenderer.setColor(1.0f, 0.7f, 0.2f, alpha); // Orange/gold
            } else if (i % 5 == 3) {
                game.shapeRenderer.setColor(1.0f, 0.3f, 0.6f, alpha); // Pink/magenta
            } else {
                game.shapeRenderer.setColor(0.7f, 0.3f, 1.0f, alpha); // Purple
            }

            float platformWidth = Constants.PLATFORM_WIDTH_MEDIUM * 0.5f * pulseSize;
            float platformHeight = Constants.PLATFORM_THICKNESS * pulseSize;
            game.shapeRenderer.rect(x, y, platformWidth, platformHeight);
        }

        // Add background particle effects
        for (int i = 0; i < 20; i++) {
            float particleX = (float) Math.random() * Constants.WORLD_WIDTH;
            float particleY = ((i * 123f) % Constants.WORLD_HEIGHT) + (float) Math.sin(time + i) * 50f;
            float particleSize = 3f + (float) Math.sin(time * 4f + i) * 2f;
            float particleAlpha = 0.1f + (float) Math.sin(time * 2f + i) * 0.1f;
            
            game.shapeRenderer.setColor(1.0f, 1.0f, 1.0f, particleAlpha);
            game.shapeRenderer.rect(particleX, particleY, particleSize, particleSize);
        }

        // Add animated skyward elements (arrows pointing up)
        for (int i = 0; i < 8; i++) {
            float arrowX = (i % 4) * 400f + 200f;
            float arrowY = (i / 4) * 300f + 300f + (float) Math.sin(time * 1.5f + i) * 20f;
            float arrowAlpha = 0.15f + (float) Math.sin(time + i * 0.7f) * 0.1f;
            
            game.shapeRenderer.setColor(0.3f, 0.9f, 1.0f, arrowAlpha);
            
            // Draw upward arrow shape
            float arrowSize = 30f;
            // Arrow shaft
            game.shapeRenderer.rect(arrowX - 5f, arrowY, 10f, arrowSize);
            // Arrow head (triangular approximation)
            for (int j = 0; j < 6; j++) {
                game.shapeRenderer.rect(arrowX - (6-j) * 2f, arrowY + arrowSize + j * 3f, (6-j) * 4f, 3f);
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
        // Stop menu music when leaving menu screen
        game.assetManager.stopMusic(game.assetManager.menuMusic);
    }

    @Override
    public void dispose() {

    }
}