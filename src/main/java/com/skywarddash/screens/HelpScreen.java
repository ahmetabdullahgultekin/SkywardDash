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

public class HelpScreen implements Screen {
    private SkywardDashGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private int currentPage = 0;
    private final int totalPages = 3;
    private float animationTimer = 0f;

    public HelpScreen(SkywardDashGame game) {
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (currentPage > 0) {
                currentPage--;
                game.assetManager.playSound(game.assetManager.buttonClickSound, 0.5f);
            }
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (currentPage < totalPages - 1) {
                currentPage++;
                game.assetManager.playSound(game.assetManager.buttonClickSound, 0.5f);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    private void draw() {
        ScreenUtils.clear(0.05f, 0.05f, 0.15f, 1.0f);
        
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        
        // Draw animated background
        drawHelpBackground();
        
        game.batch.begin();
        
        // Title
        game.font.getData().setScale(3.5f);
        game.font.setColor(0.3f, 0.9f, 1.0f, 1.0f);
        String title = "HELP & GUIDE";
        float titleWidth = game.font.getSpaceXadvance() * title.length() * 3.5f;
        game.font.draw(game.batch, title, 
                      (Constants.WORLD_WIDTH - titleWidth) / 2, 
                      camera.position.y + 300f);
        
        // Page indicator
        game.font.getData().setScale(1.5f);
        game.font.setColor(0.7f, 0.7f, 0.7f, 1.0f);
        String pageInfo = "Page " + (currentPage + 1) + " of " + totalPages;
        float pageWidth = game.font.draw(game.batch, pageInfo, 0, 0).width;
        game.font.draw(game.batch, pageInfo, 
                      (Constants.WORLD_WIDTH - pageWidth) / 2, 
                      camera.position.y + 250f);
        
        // Draw current page content
        switch (currentPage) {
            case 0:
                drawGameplayPage();
                break;
            case 1:
                drawPlatformsPage();
                break;
            case 2:
                drawScoringPage();
                break;
        }
        
        // Navigation instructions
        game.font.getData().setScale(1.2f);
        game.font.setColor(0.7f, 0.9f, 1.0f, 1.0f);
        String instructions = "← → Navigate Pages    ESC Back to Menu";
        float instructionWidth = game.font.draw(game.batch, instructions, 0, 0).width;
        game.font.draw(game.batch, instructions,
                      (Constants.WORLD_WIDTH - instructionWidth) / 2, 
                      camera.position.y - 350f);
        
        game.batch.end();
    }
    
    private void drawGameplayPage() {
        float contentY = camera.position.y + 150f;
        float lineSpacing = 50f;
        
        // Page title
        game.font.getData().setScale(2.5f);
        game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f);
        String pageTitle = "HOW TO PLAY";
        float pageTitleWidth = game.font.draw(game.batch, pageTitle, 0, 0).width;
        game.font.draw(game.batch, pageTitle, 
                      (Constants.WORLD_WIDTH - pageTitleWidth) / 2, contentY);
        
        contentY -= 80f;
        
        // Content
        game.font.getData().setScale(1.6f);
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        String[] lines = {
            "🎯 OBJECTIVE: Climb as high as possible!",
            "",
            "🏃 Build horizontal speed by moving left/right",
            "⬆️ Jump higher with more momentum",
            "🦘 Double jump available - use wisely!",
            "",
            "⚡ Chain jumps without touching ground for combos",
            "📈 Higher combos = faster movement speed",
            "🔥 Red danger floor rises - stay above it!",
            "",
            "🏆 Every 100 platforms = new floor celebration!"
        };
        
        for (String line : lines) {
            if (!line.isEmpty()) {
                game.font.draw(game.batch, line, camera.position.x - 400f, contentY);
            }
            contentY -= lineSpacing;
        }
    }
    
    private void drawPlatformsPage() {
        float contentY = camera.position.y + 150f;
        float lineSpacing = 45f;
        
        // Page title
        game.font.getData().setScale(2.5f);
        game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f);
        String pageTitle = "PLATFORM TYPES";
        float pageTitleWidth = game.font.draw(game.batch, pageTitle, 0, 0).width;
        game.font.draw(game.batch, pageTitle, 
                      (Constants.WORLD_WIDTH - pageTitleWidth) / 2, contentY);
        
        contentY -= 80f;
        
        // Platform types with colors
        game.font.getData().setScale(1.5f);
        
        String[][] platformInfo = {
            {"🟢 NORMAL", "Standard platforms - solid and reliable", "0.4f, 0.8f, 0.2f"},
            {"🟠 BOUNCY", "Extra jump boost - reach higher!", "1.0f, 0.8f, 0.2f"},
            {"🟤 BREAKABLE", "Disappears after use - jump quickly!", "0.8f, 0.4f, 0.2f"},
            {"⚪ MOVING", "Slides left/right - transfers momentum", "0.9f, 0.9f, 0.9f"},
            {"❄️ ICY", "Slippery surface - slide with style!", "0.7f, 0.9f, 1.0f"},
            {"🔴 FALLING", "Falls when stepped on - don't linger!", "1.0f, 0.3f, 0.3f"}
        };
        
        for (String[] info : platformInfo) {
            String[] colorParts = info[2].split(", ");
            float r = Float.parseFloat(colorParts[0].replace("f", ""));
            float g = Float.parseFloat(colorParts[1].replace("f", ""));
            float b = Float.parseFloat(colorParts[2].replace("f", ""));
            
            game.font.setColor(r, g, b, 1.0f);
            game.font.draw(game.batch, info[0], camera.position.x - 400f, contentY);
            
            game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            game.font.draw(game.batch, info[1], camera.position.x - 200f, contentY);
            
            contentY -= lineSpacing;
        }
    }
    
    private void drawScoringPage() {
        float contentY = camera.position.y + 150f;
        float lineSpacing = 50f;
        
        // Page title
        game.font.getData().setScale(2.5f);
        game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f);
        String pageTitle = "SCORING SYSTEM";
        float pageTitleWidth = game.font.draw(game.batch, pageTitle, 0, 0).width;
        game.font.draw(game.batch, pageTitle, 
                      (Constants.WORLD_WIDTH - pageTitleWidth) / 2, contentY);
        
        contentY -= 80f;
        
        // Scoring info
        game.font.getData().setScale(1.6f);
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        String[] lines = {
            "📊 SCORE CALCULATION:",
            "",
            "• Base: 10 points per platform reached",
            "• Height bonus: Higher = more points",
            "• Combo multiplier: x2, x3, x4...",
            "",
            "🎯 HIGH SCORE TIPS:",
            "",
            "• Chain jumps for massive combo multipliers",
            "• Use momentum to skip difficult platforms",
            "• Bouncy platforms are your best friend",
            "• Don't rush - plan your path carefully",
            "",
            "🏅 Celebrate reaching floors 100, 200, 300!"
        };
        
        for (String line : lines) {
            if (line.startsWith("🎯") || line.startsWith("📊") || line.startsWith("🏅")) {
                game.font.setColor(0.2f, 1.0f, 0.4f, 1.0f); // Bright green
            } else if (line.startsWith("•")) {
                game.font.setColor(0.8f, 1.0f, 0.3f, 1.0f); // Yellow-green
            } else {
                game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f); // White
            }
            
            if (!line.isEmpty()) {
                game.font.draw(game.batch, line, camera.position.x - 400f, contentY);
            }
            contentY -= lineSpacing;
        }
    }
    
    private void drawHelpBackground() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        float time = animationTimer;
        
        // Draw floating question marks and info symbols
        for (int i = 0; i < 6; i++) {
            float x = (i % 3) * 600f + 300f + (float) Math.sin(time + i * 1.2f) * 50f;
            float y = (i / 3) * 400f + 200f + (float) Math.cos(time * 0.8f + i) * 30f;
            
            float alpha = 0.15f + (float) Math.sin(time + i) * 0.1f;
            
            if (i % 2 == 0) {
                // Question mark shape (simplified as rectangles)
                game.shapeRenderer.setColor(0.3f, 0.7f, 1.0f, alpha);
                game.shapeRenderer.rect(x, y + 20f, 20f, 40f); // Vertical part
                game.shapeRenderer.rect(x, y + 40f, 40f, 20f); // Top horizontal
                game.shapeRenderer.rect(x + 20f, y + 20f, 20f, 20f); // Right vertical
                game.shapeRenderer.rect(x + 10f, y, 20f, 10f); // Dot
            } else {
                // Info symbol (i)
                game.shapeRenderer.setColor(0.9f, 0.6f, 0.2f, alpha);
                game.shapeRenderer.rect(x + 15f, y, 10f, 40f); // Vertical line
                game.shapeRenderer.rect(x + 12f, y + 45f, 16f, 10f); // Top dot
            }
        }
        
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