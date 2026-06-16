package com.mandri.game;

import com.badlogic.gdx.Game;
import com.mandri.storage.CutsceneManager;
import com.mandri.storage.MainAssetsManager;
import com.mandri.storage.UIManager;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private MainAssetsManager manager;
    @Override
    public void create() {
        manager = new MainAssetsManager();
        manager.loadResources();
        UIManager.getInstance().loadUI();
        this.setScreen(new IntroScreen(this));
//        this.setScreen(new CaveScreen(this ,manager));
    }

    @Override
    public void render() {
        super.render();
    }

    public MainAssetsManager getManager() {
        return manager;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        manager.disposeAll();
        UIManager.getInstance().dispose();
    }
}
