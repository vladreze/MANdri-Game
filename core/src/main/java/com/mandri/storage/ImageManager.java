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

    public TextureRegion whitePixel;

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

    //MOBS
    public TextureRegion spaceMobAlive;
    public TextureRegion spaceMobDead;
    //Rocket
    public TextureRegion rocketFlying;
    public TextureRegion rocketFixed;
    public TextureRegion rocketBroken;
    //Rocket parts
    public TextureRegion rocketPart1;
    public TextureRegion rocketPart2;
    public TextureRegion rocketPart3;


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
        manager.load("particles/particle.png", Texture.class);
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
        //MOB SPACE
        manager.load("space/space_Mob_Alive.png", Texture.class);
        manager.load("space/space_Mob_Dead.png", Texture.class);
        //Rocket
        manager.load("space/space_Rocket_broken.png", Texture.class);
        manager.load("space/space_Rocket_fixed.png", Texture.class);
        manager.load("space/space_Rocket_flying.png", Texture.class);
        //Rocket parts
        manager.load("space/space_Part_1.png", Texture.class);
        manager.load("space/space_Part_2.png", Texture.class);
        manager.load("space/space_Part_3.png", Texture.class);
//        forest
//        cave

//        CUTSCENE
        manager.load("cutscenes/cutscene1-1.png", Texture.class);
        manager.load("cutscenes/cutscene1-2.png", Texture.class);
        manager.load("cutscenes/cutscene1-3.png", Texture.class);
        manager.load("cutscenes/cutscene1-9.png", Texture.class);
        manager.load("cutscenes/cutscene1-10.png", Texture.class);
        manager.load("cutscenes/cutscene1-11.png", Texture.class);

//        manager.load("cutscenes/cutscene2-1.png", Texture.class);

        manager.load("cutscenes/c1.png", Texture.class);
        manager.load("cutscenes/c1.1.png", Texture.class);
        manager.load("cutscenes/c2.png", Texture.class);
        manager.load("cutscenes/c2.1.png", Texture.class);
        manager.load("cutscenes/c3.png", Texture.class);
        manager.load("cutscenes/c3.1.png", Texture.class);
        manager.load("cutscenes/c3.2.png", Texture.class);
        manager.load("cutscenes/c3.3.png", Texture.class);
        manager.load("cutscenes/c3.4.png", Texture.class);
        manager.load("cutscenes/c-black.png", Texture.class);
    }

    public void initAnimations(){
//      MOBS
        spaceMobAlive=new TextureRegion(manager.get("space/space_Mob_Alive.png", Texture.class));
        spaceMobDead = new TextureRegion(manager.get("space/space_Mob_Dead.png", Texture.class));
//      Rocket
        rocketBroken = new TextureRegion(manager.get("space/space_Rocket_broken.png", Texture.class));
        rocketFixed = new TextureRegion(manager.get("space/space_Rocket_fixed.png", Texture.class));
        rocketFlying = new TextureRegion(manager.get("space/space_Rocket_flying.png", Texture.class));
//      Rocket   PARTS
        rocketPart1 = new TextureRegion(manager.get("space/space_Part_1.png", Texture.class));
        rocketPart2 = new TextureRegion(manager.get("space/space_Part_2.png", Texture.class));
        rocketPart3 = new TextureRegion(manager.get("space/space_Part_3.png", Texture.class));
        Texture tWhitePixel = manager.get("particles/particle.png", Texture.class);
        whitePixel = new TextureRegion(tWhitePixel);

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

    public Texture cutscene1(int frameNum){
        Texture texture =  manager.get("cutscenes/cutscene1-1.png", Texture.class);
        switch (frameNum){
            case 1: texture = manager.get("cutscenes/cutscene1-1.png", Texture.class);
            break;
            case 2: texture = manager.get("cutscenes/cutscene1-2.png", Texture.class);
                break;
            case 3: texture = manager.get("cutscenes/cutscene1-3.png", Texture.class);
                break;
            case 9: texture = manager.get("cutscenes/cutscene1-9.png", Texture.class);
                break;
            case 10: texture = manager.get("cutscenes/cutscene1-10.png", Texture.class);
                break;
            case 11: texture = manager.get("cutscenes/cutscene1-11.png", Texture.class);
                break;
        }
        return texture;
    }

    public Texture cutscene2(int frameNum){
        Texture texture =  manager.get("cutscenes/c1.1.png", Texture.class);
        switch (frameNum){
            case 1: texture = manager.get("cutscenes/c1.png", Texture.class);
                break;
            case 2: texture = manager.get("cutscenes/c1.1.png", Texture.class);
                break;
            case 3: texture = manager.get("cutscenes/c2.png", Texture.class);
                break;
            case 4: texture = manager.get("cutscenes/c2.1.png", Texture.class);
                break;
            case 5: texture = manager.get("cutscenes/c3.png", Texture.class);
                break;
            case 6: texture = manager.get("cutscenes/c3.1.png", Texture.class);
                break;
            case 7: texture = manager.get("cutscenes/c3.2.png", Texture.class);
                break;
            case 8: texture = manager.get("cutscenes/c3.3.png", Texture.class);
                break;
            case 9: texture = manager.get("cutscenes/c3.4.png", Texture.class);
                break;
            case 10: texture = manager.get("cutscenes/c-black.png", Texture.class);
                break;
        }
        return texture;
    }

    public void disposeImages(){
        manager.dispose();
    }

}
