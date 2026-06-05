package com.mandri.storage;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageManager {
    private final AssetManager manager;

    public ImageManager(AssetManager manager) {
        this.manager = manager;
    }
    SpriteBatch batch;
    public Animation<TextureRegion> spaceIdle_F;
    public Animation<TextureRegion> spaceIdle_L;
    public Animation<TextureRegion> spaceIdle_R;

    public Animation<TextureRegion> forestIdle_F;
    public Animation<TextureRegion> forestIdle_L;
    public Animation<TextureRegion> forestIdle_R;

    public Animation<TextureRegion> caveIdle_F;
    public Animation<TextureRegion> caveIdle_R;
    public Animation<TextureRegion> caveIdle_L;

    public Animation<TextureRegion> spaceRun_R;
    public Animation<TextureRegion> spaceRun_L;

    public Animation<TextureRegion> forestRun_R;
    public Animation<TextureRegion> forestRun_L;

    public Animation<TextureRegion> caveRun_R;
    public Animation<TextureRegion> caveRun_L;

    public void loadImages(){
//        manager.load("menu_bg.png", Texture.class);
//        manager.load("space_bg.png", Texture.class);
//        manager.load("forest_bg.png", Texture.class);
//        manager.load("cave_bg.png", Texture.class);

        manager.load("space/space_front.png", Texture.class);
        manager.load("space/space_left.png", Texture.class);
        manager.load("space/space_right.png", Texture.class);
        manager.load("space/space_fall_F.png", Texture.class);
        manager.load("space/space_fall_L.png", Texture.class);
        manager.load("space/space_fall_R.png", Texture.class);
        manager.load("space/space_jump_F.png", Texture.class);
        manager.load("space/space_jump_L.png", Texture.class);
        manager.load("space/space_jump_R.png", Texture.class);
        manager.load("space/space_run_L.png", Texture.class);
        manager.load("space/space_run_R.png", Texture.class);

        manager.finishLoading();

    }

    public void disposeImages(){
        manager.dispose();
    }

}
