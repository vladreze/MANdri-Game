package com.mandri.storage;

import com.badlogic.gdx.assets.AssetManager;

public class MainAssetsManager {
    public final AssetManager manager= new AssetManager();

    public final MusicManager music = new MusicManager(manager);
    public final ImageManager image = new ImageManager(manager);

    public void loadResources(){
        music.loadMusic();
        image.loadImages();
        manager.finishLoading();
        image.initAnimations();
    }

    public MusicManager getMusic() {
        return music;
    }

    public void disposeAll(){
        music.disposeMusic();
        image.disposeImages();

    }
}
