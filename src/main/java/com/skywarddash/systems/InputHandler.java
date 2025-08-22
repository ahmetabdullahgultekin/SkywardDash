package com.skywarddash.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.skywarddash.entities.Player;
import com.skywarddash.utils.AssetManager;

public class InputHandler {

    public void handleInput(Player player, float deltaTime) {
        handleInput(player, deltaTime, null);
    }

    public void handleInput(Player player, float deltaTime, AssetManager assetManager) {
        // Handle horizontal movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft(deltaTime);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight(deltaTime);
        }

        // Handle jumping (space or up arrow)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
                Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player.jump(assetManager);
        }
    }
}