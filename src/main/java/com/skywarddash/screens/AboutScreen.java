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

public class AboutScreen implements Screen {
    private SkywardDashGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private float animationTimer = 0f;
    private float sparkleTimer = 0f;

    public AboutScreen(SkywardDashGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        animationTimer += delta;
        sparkleTimer += delta;
        handleInput();
        draw();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
                Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    private void draw() {
        ScreenUtils.clear(0.05f, 0.05f, 0.15f, 1.0f);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw animated background
        drawAboutBackground();

        game.batch.begin();

        // Main title with glow effect
        game.font.getData().setScale(4.0f);
        game.font.setColor(0.3f, 0.9f, 1.0f, 1.0f);
        String title = "SKYWARD DASH";
        float titleWidth = game.font.draw(game.batch, title, 0, 0).width;
        game.font.draw(game.batch, title,
                (Constants.WORLD_WIDTH - titleWidth) / 2,
                camera.position.y + 280f);

        // Version info
        game.font.getData().setScale(1.8f);
        game.font.setColor(0.8f, 1.0f, 0.3f, 1.0f);
        String version = "Version 1.0.0";
        float versionWidth = game.font.draw(game.batch, version, 0, 0).width;
        game.font.draw(game.batch, version,
                (Constants.WORLD_WIDTH - versionWidth) / 2,
                camera.position.y + 220f);

        // Developer section
        float contentY = camera.position.y + 150f;
        float lineSpacing = 50f;

        game.font.getData().setScale(2.2f);
        game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f);
        String devTitle = "DEVELOPED BY";
        float devTitleWidth = game.font.draw(game.batch, devTitle, 0, 0).width;
        game.font.draw(game.batch, devTitle,
                (Constants.WORLD_WIDTH - devTitleWidth) / 2, contentY);

        contentY -= 60f;

        game.font.getData().setScale(2.5f);
        game.font.setColor(1.0f, 0.4f, 0.8f, 1.0f); // Pink/magenta
        String company = "Rolling Cat Software";
        float companyWidth = game.font.draw(game.batch, company, 0, 0).width;
        game.font.draw(game.batch, company,
                (Constants.WORLD_WIDTH - companyWidth) / 2, contentY);

        // Description
        contentY -= 80f;
        game.font.getData().setScale(1.6f);
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        String[] description = {
                "An Icy Tower-inspired vertical platformer",
                "where momentum is everything!",
                "",
                "Built with LibGDX framework",
                "Featuring procedural level generation,",
                "combo systems, and endless climbing action."
        };

        for (String line : description) {
            if (!line.isEmpty()) {
                float lineWidth = game.font.draw(game.batch, line, 0, 0).width;
                game.font.draw(game.batch, line,
                        (Constants.WORLD_WIDTH - lineWidth) / 2, contentY);
            }
            contentY -= lineSpacing;
        }

        // Credits section
        contentY -= 40f;
        game.font.getData().setScale(2.0f);
        game.font.setColor(0.2f, 1.0f, 0.4f, 1.0f);
        String creditsTitle = "SPECIAL THANKS";
        float creditsTitleWidth = game.font.draw(game.batch, creditsTitle, 0, 0).width;
        game.font.draw(game.batch, creditsTitle,
                (Constants.WORLD_WIDTH - creditsTitleWidth) / 2, contentY);

        contentY -= 60f;
        game.font.getData().setScale(1.4f);
        game.font.setColor(0.8f, 0.8f, 0.8f, 1.0f);

        String[] credits = {
                "Kenney.nl - UI and graphic assets",
                "LibGDX - Amazing game development framework",
                "Original Icy Tower - Inspiration and gameplay",
                "",
                "Thank you for playing!"
        };

        for (String line : credits) {
            if (!line.isEmpty()) {
                if (line.startsWith("Thank you")) {
                    game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f + (float) Math.sin(animationTimer * 2f) * 0.3f);
                } else {
                    game.font.setColor(0.8f, 0.8f, 0.8f, 1.0f);
                }
                float lineWidth = game.font.draw(game.batch, line, 0, 0).width;
                game.font.draw(game.batch, line,
                        (Constants.WORLD_WIDTH - lineWidth) / 2, contentY);
            }
            contentY -= 40f;
        }

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

    private void drawAboutBackground() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float time = animationTimer;

        // Draw animated logo elements in background
        for (int i = 0; i < 8; i++) {
            float x = (i % 4) * 400f + 200f + (float) Math.sin(time * 0.5f + i) * 100f;
            float y = (i / 4) * 300f + 150f + (float) Math.cos(time * 0.3f + i) * 50f;

            float alpha = 0.1f + (float) Math.sin(time + i) * 0.05f;

            // Draw company logo elements (cat-inspired shapes)
            if (i % 3 == 0) {
                // Cat ear triangles
                game.shapeRenderer.setColor(1.0f, 0.4f, 0.8f, alpha);
                // Simple triangle approximation with rectangles
                for (int j = 0; j < 5; j++) {
                    game.shapeRenderer.rect(x + j * 6f, y + j * 8f, 30f - j * 6f, 8f);
                }
            } else if (i % 3 == 1) {
                // Rolling elements (circles approximated with small rectangles)
                game.shapeRenderer.setColor(0.3f, 0.7f, 1.0f, alpha);
                float radius = 40f;
                for (int j = 0; j < 16; j++) {
                    float angle = (float) (j * Math.PI / 8 + time);
                    float px = x + (float) Math.cos(angle) * radius;
                    float py = y + (float) Math.sin(angle) * radius;
                    game.shapeRenderer.rect(px, py, 8f, 8f);
                }
            } else {
                // Software elements (geometric patterns)
                game.shapeRenderer.setColor(0.2f, 1.0f, 0.4f, alpha);
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        if ((row + col) % 2 == 0) {
                            game.shapeRenderer.rect(x + col * 15f, y + row * 15f, 12f, 12f);
                        }
                    }
                }
            }
        }

        // Sparkle effects
        if (sparkleTimer > 0.1f) {
            sparkleTimer = 0f;

            for (int i = 0; i < 5; i++) {
                float sparkleX = (float) Math.random() * Constants.WORLD_WIDTH;
                float sparkleY = (float) Math.random() * Constants.WORLD_HEIGHT;
                float sparkleSize = (float) Math.random() * 8f + 4f;
                float sparkleAlpha = (float) Math.random() * 0.5f + 0.2f;

                game.shapeRenderer.setColor(1.0f, 1.0f, 1.0f, sparkleAlpha);
                game.shapeRenderer.rect(sparkleX, sparkleY, sparkleSize, sparkleSize);

                // Cross sparkle pattern
                game.shapeRenderer.rect(sparkleX - sparkleSize, sparkleY, sparkleSize * 3f, 2f);
                game.shapeRenderer.rect(sparkleX, sparkleY - sparkleSize, 2f, sparkleSize * 3f);
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
    }

    @Override
    public void dispose() {
    }
}