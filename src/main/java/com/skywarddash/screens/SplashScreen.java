package com.skywarddash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skywarddash.SkywardDashGame;
import com.skywarddash.utils.Constants;

public class SplashScreen implements Screen {
    private static final float SPLASH_DURATION = 3.0f; // Show for 3 seconds
    private SkywardDashGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private float timer;
    private float alpha = 0f;
    private boolean fadingIn = true;

    public SplashScreen(SkywardDashGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
        timer = 0f;
    }

    @Override
    public void show() {
        // Assets should be loading in background
    }

    @Override
    public void render(float delta) {
        timer += delta;

        // Handle fade in/out
        if (fadingIn) {
            alpha += delta * 2f; // Fade in over 0.5 seconds
            if (alpha >= 1f) {
                alpha = 1f;
                fadingIn = false;
            }
        } else if (timer > SPLASH_DURATION - 0.5f) {
            alpha -= delta * 2f; // Fade out over 0.5 seconds
            if (alpha <= 0f) {
                alpha = 0f;
            }
        }

        draw();

        // Skip splash screen on any key press or after duration
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) ||
                Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) ||
                timer > SPLASH_DURATION) {
            game.setScreen(new MenuScreen(game));
        }
    }

    private void draw() {
        // Clear with dark background
        ScreenUtils.clear(0.05f, 0.05f, 0.15f, 1.0f);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw custom splash background image first
        if (game.assetManager.splashBackgroundTexture != null) {
            game.batch.begin();
            game.batch.draw(game.assetManager.splashBackgroundTexture,
                    0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
            game.batch.end();
        }

        // Removed animated decorations - clean splash screen with just the background image

        // Draw text
        game.batch.begin();
        game.font.getData().setScale(4.0f);
        game.font.setColor(1.0f, 1.0f, 1.0f, alpha);

        // Game title - centered on screen
        String title = "SKYWARD DASH";
        float titleWidth = game.font.getSpaceXadvance() * title.length() * 4f;
        game.font.draw(game.batch, title,
                (Constants.WORLD_WIDTH - titleWidth) / 2,
                Constants.WORLD_HEIGHT / 2 + 50f);

        // Subtitle
        game.font.getData().setScale(2.0f);
        String subtitle = "Rolling Cat Software";
        float subtitleWidth = game.font.getSpaceXadvance() * subtitle.length() * 2f;
        game.font.draw(game.batch, subtitle,
                (Constants.WORLD_WIDTH - subtitleWidth) / 2,
                Constants.WORLD_HEIGHT / 2 - 50f);

        // Skip instruction
        game.font.getData().setScale(1.5f);
        game.font.setColor(0.8f, 0.8f, 0.8f, alpha * 0.7f);
        String skipText = "Press any key to continue...";
        float skipWidth = game.font.getSpaceXadvance() * skipText.length() * 1.5f;
        game.font.draw(game.batch, skipText,
                (Constants.WORLD_WIDTH - skipWidth) / 2,
                200f);

        game.batch.end();

        // Reset font scale
        game.font.getData().setScale(1.0f);
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
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
    }

    @Override
    public void dispose() {
    }
}