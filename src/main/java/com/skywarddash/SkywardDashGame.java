package com.skywarddash;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.skywarddash.screens.MenuScreen;
import com.skywarddash.utils.AssetManager;

public class SkywardDashGame extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;
    public AssetManager assetManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        assetManager = new AssetManager();

        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        assetManager.dispose();
    }
}