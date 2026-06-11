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
//    space
    public Animation<TextureRegion> spaceIdle_F;
//    public Animation<TextureRegion> spaceIdle_L;
//    public Animation<TextureRegion> spaceIdle_R;

    public TextureRegion fullHeart;
    public TextureRegion poisonHeart;
    public TextureRegion shieldHeart;
    public TextureRegion emptyHeart;

    public TextureRegion spaceJump_F;
    public TextureRegion spaceJump_R;
    public TextureRegion spaceJump_L;
    public TextureRegion spaceFall_F;
    public TextureRegion spaceFall_R;
    public TextureRegion spaceFall_L;

    public Animation<TextureRegion> spaceRun_R;
    public Animation<TextureRegion> spaceRun_L;

//    forest
    public Animation<TextureRegion> forestIdle_F;
    public Animation<TextureRegion> forestIdle_L;
    public Animation<TextureRegion> forestIdle_R;

    public TextureRegion forestJump_F;
    public TextureRegion forestJump_R;
    public TextureRegion forestJump_L;
    public TextureRegion forestFall_F;
    public TextureRegion forestFall_R;
    public TextureRegion forestFall_L;

    public Animation<TextureRegion> forestRun_R;
    public Animation<TextureRegion> forestRun_L;

//    cave
    public Animation<TextureRegion> caveIdle_F;
    public Animation<TextureRegion> caveIdle_R;
    public Animation<TextureRegion> caveIdle_L;

    public TextureRegion caveJump_F;
    public TextureRegion caveJump_R;
    public TextureRegion caveJump_L;
    public TextureRegion caveFall_F;
    public TextureRegion caveFall_R;
    public TextureRegion caveFall_L;

    public Animation<TextureRegion> caveRun_R;
    public Animation<TextureRegion> caveRun_L;

    public void loadImages(){
//        backgrounds
//        manager.load("menu_bg.png", Texture.class);

//      hearts
        manager.load("level-ui/hearts.png", Texture.class);

        manager.load("space/space-bg.png", Texture.class);
//        manager.load("forest_bg.png", Texture.class);
//        manager.load("cave_bg.png", Texture.class);

//        space
        manager.load("space/space_front.png", Texture.class);
//        manager.load("space/space_left.png", Texture.class);
//        manager.load("space/space_right.png", Texture.class);
        manager.load("space/space_fall_F.png", Texture.class);
        manager.load("space/space_fall_L.png", Texture.class);
        manager.load("space/space_fall_R.png", Texture.class);
        manager.load("space/space_jump_F.png", Texture.class);
        manager.load("space/space_jump_L.png", Texture.class);
        manager.load("space/space_jump_R.png", Texture.class);
        manager.load("space/space_run_L.png", Texture.class);
        manager.load("space/space_run_R.png", Texture.class);
//        forest
//        cave
    }

    public void initAnimations(){
//      hearts
        Texture tHearts = manager.get("level-ui/hearts.png", Texture.class);

        TextureRegion[][] heartFrames = TextureRegion.split(tHearts, 16, 16);

        fullHeart = heartFrames[0][0];
        poisonHeart = heartFrames[1][0];
        shieldHeart = heartFrames[2][0];
        emptyHeart = heartFrames[3][0];

//        run
        Texture tSpaceRunR = manager.get("space/space_run_R.png", Texture.class);
        TextureRegion[][] texRunR = TextureRegion.split(tSpaceRunR, 32, 32);
        spaceRun_R = new Animation<>(0.15f, texRunR[0]);

        Texture tSpaceRunL = manager.get("space/space_run_L.png", Texture.class);
        TextureRegion[][] texRunL = TextureRegion.split(tSpaceRunL, 32, 32);
        spaceRun_L = new Animation<>(0.15f, texRunL[0]);

//        idle
        Texture tSpaceIdle = manager.get("space/space_front.png", Texture.class);
        TextureRegion[][] texIdle = TextureRegion.split(tSpaceIdle, 32, 32);
        spaceIdle_F = new Animation<>(0.24f, texIdle[0]);

//        jump&fall
        spaceJump_F = new TextureRegion(manager.get("space/space_jump_F.png", Texture.class));
        spaceJump_R = new TextureRegion(manager.get("space/space_jump_R.png", Texture.class));
        spaceJump_L = new TextureRegion(manager.get("space/space_jump_L.png", Texture.class));
        spaceFall_F = new TextureRegion(manager.get("space/space_fall_F.png", Texture.class));
        spaceFall_R = new TextureRegion(manager.get("space/space_fall_R.png", Texture.class));
        spaceFall_L = new TextureRegion(manager.get("space/space_fall_L.png", Texture.class));

    }
    public Texture spaceBg(){
        return  manager.get("space/space-bg.png", Texture.class);
    }

    public void disposeImages(){
        manager.dispose();
    }

}
