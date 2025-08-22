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
import com.skywarddash.entities.DangerFloor;
import com.skywarddash.entities.Platform;
import com.skywarddash.entities.Player;
import com.skywarddash.systems.CameraController;
import com.skywarddash.systems.CollisionSystem;
import com.skywarddash.systems.InputHandler;
import com.skywarddash.systems.ScoreSystem;
import com.skywarddash.utils.Constants;
import com.skywarddash.utils.PlatformGenerator;

public class GameScreen implements Screen {
    private SkywardDashGame game;
    private OrthographicCamera camera;
    private Viewport viewport;

    // Game entities
    private Player player;
    private DangerFloor dangerFloor;
    private PlatformGenerator platformGenerator;

    // Game systems
    private InputHandler inputHandler;
    private CollisionSystem collisionSystem;
    private ScoreSystem scoreSystem;
    private CameraController cameraController;

    // Game state
    private boolean gameOver;
    private boolean paused;
    private float gameTime;

    public GameScreen(SkywardDashGame game) {
        this.game = game;

        // Setup camera and viewport
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(Constants.WORLD_WIDTH / 2, 200f, 0); // Start closer to ground level

        // Initialize game entities
        player = new Player(Constants.PLAYER_START_X, Constants.PLAYER_START_Y);
        dangerFloor = new DangerFloor(-100.0f); // Start well below ground level
        platformGenerator = new PlatformGenerator();

        // Initialize systems
        inputHandler = new InputHandler();
        collisionSystem = new CollisionSystem();
        scoreSystem = new ScoreSystem();
        cameraController = new CameraController(camera);

        gameOver = false;
        paused = false;
        gameTime = 0;
    }

    @Override
    public void show() {
        // Start background music
        game.assetManager.playMusic(game.assetManager.backgroundMusic, true, 0.3f);
    }

    @Override
    public void render(float delta) {
        if (!gameOver && !paused) {
            update(delta);
        }

        handleInput();
        draw();
    }

    private void update(float delta) {
        // Update game time
        gameTime += delta;

        // Update game entities
        player.update(delta);
        dangerFloor.update(delta, scoreSystem.getScore(), gameTime, player.getPosition().y);
        platformGenerator.update(player);

        // Update all platforms and handle removal
        updatePlatforms(delta);

        // Handle collisions
        collisionSystem.checkPlatformCollisions(player, platformGenerator.getPlatforms(), game.assetManager);

        // Check if player hit the danger floor
        if (collisionSystem.checkDangerFloorCollision(player, dangerFloor)) {
            gameOver();
        }

        // Update score
        scoreSystem.updateScore(player);

        // Update camera
        cameraController.update(player, delta);
    }

    private void updatePlatforms(float delta) {
        try {
            // Update all platforms
            for (Platform platform : platformGenerator.getPlatforms()) {
                platform.update(delta);
            }

            // Remove platforms that are marked for removal AND are far below the player
            float playerY = player.getPosition().y;
            float removalThreshold = playerY - Constants.WORLD_HEIGHT * 2; // Keep platforms 2 screen heights below

            platformGenerator.removePlatforms(platform -> {
                // Only remove if marked for removal OR too far below player
                return platform.shouldRemove() || platform.getPosition().y < removalThreshold;
            });

        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Error updating platforms: " + e.getMessage());
        }
    }

    private void handleInput() {
        if (!gameOver) {
            inputHandler.handleInput(player, Gdx.graphics.getDeltaTime(), game.assetManager);
        }

        // Pause/unpause
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }

        // Restart game
        if (gameOver && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            restartGame();
        }

        // Return to menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    private void draw() {
        ScreenUtils.clear(Constants.BACKGROUND_COLOR[0], Constants.BACKGROUND_COLOR[1],
                Constants.BACKGROUND_COLOR[2], Constants.BACKGROUND_COLOR[3]);

        // Set the projection matrix
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        // Draw background layers using SpriteBatch if textures are available
        if (game.assetManager.isAssetsLoaded() && game.assetManager.backgroundTexture != null) {
            game.batch.begin();

            // Draw main background (fixed gradient)
            game.batch.draw(game.assetManager.backgroundTexture,
                    camera.position.x - Constants.WORLD_WIDTH / 2,
                    camera.position.y - Constants.WORLD_HEIGHT / 2,
                    Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

            // Draw parallax cloud layers if available
            if (game.assetManager.cloudsTexture != null) {
                // Layer 1: Slow moving clouds (far background)
                float cloud1Offset = (camera.position.y * 0.1f) % Constants.WORLD_WIDTH;
                game.batch.draw(game.assetManager.cloudsTexture,
                        camera.position.x - Constants.WORLD_WIDTH / 2 - cloud1Offset,
                        camera.position.y + Constants.WORLD_HEIGHT / 3,
                        Constants.WORLD_WIDTH, 200);

                // Draw second instance for seamless scrolling
                game.batch.draw(game.assetManager.cloudsTexture,
                        camera.position.x - Constants.WORLD_WIDTH / 2 - cloud1Offset + Constants.WORLD_WIDTH,
                        camera.position.y + Constants.WORLD_HEIGHT / 3,
                        Constants.WORLD_WIDTH, 200);

                // Layer 2: Medium speed clouds (mid background)
                float cloud2Offset = (camera.position.y * 0.15f) % Constants.WORLD_WIDTH;
                game.batch.setColor(1.0f, 1.0f, 1.0f, 0.6f); // More transparent
                game.batch.draw(game.assetManager.cloudsTexture,
                        camera.position.x - Constants.WORLD_WIDTH / 2 - cloud2Offset,
                        camera.position.y + Constants.WORLD_HEIGHT / 4,
                        Constants.WORLD_WIDTH, 200);

                game.batch.draw(game.assetManager.cloudsTexture,
                        camera.position.x - Constants.WORLD_WIDTH / 2 - cloud2Offset + Constants.WORLD_WIDTH,
                        camera.position.y + Constants.WORLD_HEIGHT / 4,
                        Constants.WORLD_WIDTH, 200);

                // Reset color for other elements
                game.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            }

            game.batch.end();
        }

        // Draw game entities with textures first
        if (game.assetManager.isAssetsLoaded()) {
            game.batch.begin();

            // Draw platforms with textures
            for (Platform platform : platformGenerator.getPlatforms()) {
                platform.render(game.batch, game.assetManager);
            }

            // Draw player with texture
            player.render(game.batch, game.assetManager);

            game.batch.end();
        }

        // Draw entities that don't have textures or fallback using shapes
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw danger floor (no texture for this)
        dangerFloor.render(game.shapeRenderer);

        // Fallback rendering if assets aren't loaded
        if (!game.assetManager.isAssetsLoaded()) {
            // Draw platforms
            for (Platform platform : platformGenerator.getPlatforms()) {
                platform.render(game.shapeRenderer);
            }

            // Draw player
            player.render(game.shapeRenderer);
        }

        game.shapeRenderer.end();

        // Draw UI
        drawUI();
    }

    private void drawUI() {
        game.batch.begin();

        // Score
        game.font.draw(game.batch, "Score: " + scoreSystem.getScore(),
                camera.position.x - Constants.WORLD_WIDTH / 2 + 20f,
                camera.position.y + Constants.WORLD_HEIGHT / 2 - 20f);

        // Combo counter with speed multiplier
        if (player.getComboCount() >= Constants.COMBO_START_THRESHOLD) {
            game.font.getData().setScale(1.5f);
            game.font.setColor(1.0f, 0.8f, 0.2f, 1.0f); // Gold
            game.font.draw(game.batch, "COMBO x" + player.getComboCount(),
                    camera.position.x - Constants.WORLD_WIDTH / 2 + 20f,
                    camera.position.y + Constants.WORLD_HEIGHT / 2 - 50f);

            game.font.getData().setScale(1.2f);
            game.font.setColor(0.3f, 1.0f, 0.3f, 1.0f); // Bright green
            game.font.draw(game.batch, "Speed: " + String.format("%.1f", player.getCurrentSpeedMultiplier()) + "x",
                    camera.position.x - Constants.WORLD_WIDTH / 2 + 20f,
                    camera.position.y + Constants.WORLD_HEIGHT / 2 - 80f);
            game.font.getData().setScale(1.0f);
        }

        // Floor counter
        game.font.setColor(0.7f, 0.7f, 1.0f, 1.0f); // Light blue
        game.font.draw(game.batch, "Floor: " + platformGenerator.getCurrentFloor(),
                camera.position.x - Constants.WORLD_WIDTH / 2 + 20f,
                camera.position.y + Constants.WORLD_HEIGHT / 2 - 110f);

        // Momentum indicator
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f); // White
        game.font.draw(game.batch, "Momentum: " + String.format("%.1f", player.getMomentum() * 100) + "%",
                camera.position.x - Constants.WORLD_WIDTH / 2 + 20f,
                camera.position.y + Constants.WORLD_HEIGHT / 2 - 140f);

        // Platform type legend (smaller text)
        game.font.getData().setScale(0.8f);
        game.font.setColor(0.9f, 0.9f, 0.9f, 0.8f);
        float legendX = camera.position.x + Constants.WORLD_WIDTH / 2 - 250f;
        float legendY = camera.position.y + Constants.WORLD_HEIGHT / 2 - 20f;

        game.font.draw(game.batch, "Platform Types:", legendX, legendY);
        game.font.setColor(0.4f, 0.8f, 0.2f, 1.0f); // Green
        game.font.draw(game.batch, "■ Normal - Standard platform", legendX, legendY - 25f);
        game.font.setColor(0.6f, 0.6f, 0.6f, 1.0f); // Gray
        game.font.draw(game.batch, "■ Bouncy - Extra jump boost", legendX, legendY - 50f);
        game.font.setColor(0.8f, 0.4f, 0.2f, 1.0f); // Brown
        game.font.draw(game.batch, "■ Breakable - Disappears after use", legendX, legendY - 75f);
        game.font.setColor(0.9f, 0.9f, 0.9f, 1.0f); // White
        game.font.draw(game.batch, "■ Moving - Slides left and right", legendX, legendY - 100f);

        // Reset font
        game.font.getData().setScale(1.0f);
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        // Game over screen
        if (gameOver) {
            game.font.getData().setScale(2.0f);
            game.font.draw(game.batch, "GAME OVER!",
                    camera.position.x - 80f, camera.position.y + 50f);
            game.font.getData().setScale(1.0f);
            game.font.draw(game.batch, "Final Score: " + scoreSystem.getScore(),
                    camera.position.x - 100f, camera.position.y);
            game.font.draw(game.batch, "High Score: " + game.assetManager.getHighScore(),
                    camera.position.x - 100f, camera.position.y - 30f);
            game.font.draw(game.batch, "Press R to restart, ESC for menu",
                    camera.position.x - 150f, camera.position.y - 60f);
        }

        // Pause screen
        if (paused) {
            game.font.getData().setScale(2.0f);
            game.font.draw(game.batch, "PAUSED",
                    camera.position.x - 50f, camera.position.y);
            game.font.getData().setScale(1.0f);
            game.font.draw(game.batch, "Press P to continue",
                    camera.position.x - 80f, camera.position.y - 40f);
        }

        game.batch.end();
    }

    private void gameOver() {
        gameOver = true;
        game.assetManager.saveHighScore(scoreSystem.getScore());

        // Play game over sound
        game.assetManager.playSound(game.assetManager.gameOverSound, 0.7f);
    }

    private void restartGame() {
        player.setPosition(Constants.PLAYER_START_X, Constants.PLAYER_START_Y);
        player.getVelocity().set(0, 0);
        player.resetCombo();

        dangerFloor = new DangerFloor(-100.0f); // Start well below ground level
        platformGenerator.reset();
        scoreSystem.reset();

        camera.position.set(Constants.WORLD_WIDTH / 2, 200f, 0);
        cameraController.reset(); // Reset camera controller state

        gameOver = false;
        paused = false;
        gameTime = 0;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        // Stop background music when leaving game screen
        game.assetManager.stopMusic(game.assetManager.backgroundMusic);
    }

    @Override
    public void dispose() {

    }
}