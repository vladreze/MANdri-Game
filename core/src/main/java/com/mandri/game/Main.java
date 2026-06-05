package com.mandri.game;

import com.badlogic.gdx.Game;
import com.mandri.storage.MainAssetsManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private MainAssetsManager manager;
    @Override
    public void create() {
        setScreen(new FirstScreen());
        manager = new MainAssetsManager();
        manager.loadResources();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        manager.disposeAll();
    }
}
