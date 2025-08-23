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

public class ControlsScreen implements Screen {
    private SkywardDashGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private float animationTimer = 0f;

    public ControlsScreen(SkywardDashGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        animationTimer += delta;
        handleInput();
        draw();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || 
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new SettingsScreen(game));
        }
    }

    private void draw() {
        ScreenUtils.clear(0.05f, 0.05f, 0.15f, 1.0f);
        
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        
        // Draw animated background
        drawControlsBackground();
        
        game.batch.begin();
        
        // Title
        game.font.getData().setScale(3.5f);
        game.font.setColor(0.3f, 0.9f, 1.0f, 1.0f);
        String title = "CONTROLS";
        float titleWidth = game.font.getSpaceXadvance() * title.length() * 3.5f;
        game.font.draw(game.batch, title, 
                      (Constants.WORLD_WIDTH - titleWidth) / 2, 
                      camera.position.y + 300f);
        
        // Controls sections
        float sectionY = camera.position.y + 200f;
        float sectionSpacing = 160f;
        
        // Movement section
        game.font.getData().setScale(2.2f);
        game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f);
        game.font.draw(game.batch, "MOVEMENT", camera.position.x - 400f, sectionY);
        
        game.font.getData().setScale(1.6f);
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        game.font.draw(game.batch, "← → Arrow Keys  or  A D", camera.position.x - 350f, sectionY - 40f);
        game.font.draw(game.batch, "Move left and right", camera.position.x - 350f, sectionY - 70f);
        
        // Jumping section
        sectionY -= sectionSpacing;
        game.font.getData().setScale(2.2f);
        game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f);
        game.font.draw(game.batch, "JUMPING", camera.position.x - 400f, sectionY);
        
        game.font.getData().setScale(1.6f);
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        game.font.draw(game.batch, "↑ Up Arrow  or  W  or  Spacebar", camera.position.x - 350f, sectionY - 40f);
        game.font.draw(game.batch, "Jump (double jump available!)", camera.position.x - 350f, sectionY - 70f);
        
        // Game controls section
        sectionY -= sectionSpacing;
        game.font.getData().setScale(2.2f);
        game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f);
        game.font.draw(game.batch, "GAME CONTROLS", camera.position.x - 400f, sectionY);
        
        game.font.getData().setScale(1.6f);
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        game.font.draw(game.batch, "P - Pause/Resume game", camera.position.x - 350f, sectionY - 40f);
        game.font.draw(game.batch, "ESC - Return to menu", camera.position.x - 350f, sectionY - 70f);
        game.font.draw(game.batch, "R - Restart (when game over)", camera.position.x - 350f, sectionY - 100f);
        
        // Tips section
        sectionY -= 200f;
        game.font.getData().setScale(2.2f);
        game.font.setColor(0.2f, 1.0f, 0.4f, 1.0f); // Bright green
        game.font.draw(game.batch, "PRO TIPS", camera.position.x - 400f, sectionY);
        
        game.font.getData().setScale(1.4f);
        game.font.setColor(0.8f, 1.0f, 0.3f, 1.0f); // Yellow-green
        game.font.draw(game.batch, "• Build horizontal speed to jump higher", camera.position.x - 350f, sectionY - 40f);
        game.font.draw(game.batch, "• Chain jumps for combo multipliers", camera.position.x - 350f, sectionY - 70f);
        game.font.draw(game.batch, "• Use momentum to reach distant platforms", camera.position.x - 350f, sectionY - 100f);
        game.font.draw(game.batch, "• Watch out for special platform types!", camera.position.x - 350f, sectionY - 130f);
        
        // Back instruction
        game.font.getData().setScale(1.2f);
        game.font.setColor(0.7f, 0.9f, 1.0f, 1.0f);
        String instructions = "Press ESC or Enter to go back";
        float instructionWidth = game.font.draw(game.batch, instructions, 0, 0).width;
        game.font.draw(game.batch, instructions,
                      (Constants.WORLD_WIDTH - instructionWidth) / 2, 
                      camera.position.y - 350f);
        
        // Reset font
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        game.font.getData().setScale(1.0f);
        
        game.batch.end();
    }
    
    private void drawControlsBackground() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Draw keyboard key representations
        float time = animationTimer;
        
        // Arrow keys visualization
        float keySize = 40f;
        float keySpacing = 50f;
        float keysX = Constants.WORLD_WIDTH * 0.75f;
        float keysY = Constants.WORLD_HEIGHT * 0.6f;
        
        // Up arrow (with pulsing effect for jump)
        float upPulse = 0.8f + (float) Math.sin(time * 3f) * 0.2f;
        game.shapeRenderer.setColor(0.3f, 0.7f, 1.0f, upPulse);
        game.shapeRenderer.rect(keysX, keysY + keySpacing, keySize, keySize);
        
        // Left and right arrows (with movement animation)
        float sidePulse = 0.6f + (float) Math.sin(time * 2f) * 0.3f;
        game.shapeRenderer.setColor(0.2f, 0.9f, 0.3f, sidePulse);
        game.shapeRenderer.rect(keysX - keySpacing, keysY, keySize, keySize); // Left
        game.shapeRenderer.rect(keysX + keySpacing, keysY, keySize, keySize); // Right
        
        // WASD keys
        float wasdY = keysY - 200f;
        game.shapeRenderer.setColor(0.8f, 0.4f, 1.0f, 0.5f + (float) Math.sin(time * 1.5f) * 0.2f);
        game.shapeRenderer.rect(keysX, wasdY + keySpacing, keySize, keySize); // W
        game.shapeRenderer.rect(keysX - keySpacing, wasdY, keySize, keySize); // A
        game.shapeRenderer.rect(keysX + keySpacing, wasdY, keySize, keySize); // D
        
        // Spacebar
        game.shapeRenderer.setColor(1.0f, 0.7f, 0.2f, 0.4f + (float) Math.sin(time * 2.5f) * 0.3f);
        game.shapeRenderer.rect(keysX - keySpacing, wasdY - keySpacing * 2, keySize * 3 + keySpacing, keySize * 0.6f);
        
        game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}