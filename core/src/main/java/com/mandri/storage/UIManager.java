package com.mandri.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UIManager {
    private static UIManager instance;
    private Skin skin;

    private UIManager() {}

    public static UIManager getInstance(){
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }

    public void loadUI(){
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    }

    public Skin getSkin(){
        return skin;
    }

    public void dispose(){
        if (skin != null) {
            skin.dispose();
        }
    }

}
