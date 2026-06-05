package com.mandri.game;

import com.badlogic.gdx.Game;
import com.mandri.storage.UIManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {


    @Override
    public void create() {
        UIManager.getInstance().loadUI();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    public void dispose(){
        super.dispose();
        UIManager.getInstance().dispose();
    }

}
