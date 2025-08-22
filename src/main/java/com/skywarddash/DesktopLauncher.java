package com.skywarddash;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Skyward Dash");

        // Enable fullscreen mode
        config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        config.setResizable(true);

        // Fallback to windowed mode if fullscreen fails
        // config.setWindowedMode(1280, 720);
        // config.setWindowIcon("assets/images/icon.png"); // Icon disabled for now

        new Lwjgl3Application(new SkywardDashGame(), config);
    }
}