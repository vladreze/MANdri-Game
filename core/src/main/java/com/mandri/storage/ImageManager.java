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

    //bat
    public TextureRegion forestBatAlive;
    public TextureRegion forestBatDead;
    //tree
    public TextureRegion forestTree;
    //bee
    public TextureRegion forestBeeAlive;
    public TextureRegion forestBeeDead;
    public TextureRegion forestBeeAngr;
    //fox
    public TextureRegion forestFox;
    //mushroom
    public TextureRegion forestMushroom;
    //axe
    public TextureRegion forestAxe;
    //acorn
    public TextureRegion forestAcorn;
    //hive
    public TextureRegion forestHive;


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

    public TextureRegion caveEmerald;
    public TextureRegion caveGeyser;
    public TextureRegion caveMonster;
    public TextureRegion caveNumber0;
    public TextureRegion caveNumber1;
    public TextureRegion caveNumber3;
    public TextureRegion caveNumber5;
    public TextureRegion caveSpider;
    public TextureRegion caveStalactite;
    public TextureRegion caveWaterExit;

    public Animation<TextureRegion> caveRun_R;
    public Animation<TextureRegion> caveRun_L;
//cs1
    public Animation<TextureRegion> cs1Anim;
    public Animation<TextureRegion> cs2w;
    public Animation<TextureRegion>cs2e;
    public Animation<TextureRegion>cs3One;
    public Animation<TextureRegion>cs3Two;

    public void loadImages(){
        manager.load("particles/particle.png", Texture.class);

//      hearts
        manager.load("level-ui/hearts.png", Texture.class);

        manager.load("space/space-bg.png", Texture.class);


//        space
        manager.load("space/space_front.png", Texture.class);
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
        manager.load("forest/forest_fall_F.png", Texture.class);
        manager.load("forest/forest_fall_L.png", Texture.class);
        manager.load("forest/forest_fall_R.png", Texture.class);
        manager.load("forest/forest_front.png", Texture.class);
        manager.load("forest/forest_jump_F.png", Texture.class);
        manager.load("forest/forest_jump_L.png", Texture.class);
        manager.load("forest/forest_jump_R.png", Texture.class);
        manager.load("forest/forest_left.png", Texture.class);
        manager.load("forest/forest_right.png", Texture.class);
        manager.load("forest/forest_run_L.png", Texture.class);
        manager.load("forest/forest_run_R.png", Texture.class);
        //Blocks
        manager.load("forest/forest_acorn.png", Texture.class);
        manager.load("forest/forest_axe.png", Texture.class);
        manager.load("forest/forest_bat_alive.png", Texture.class);
        manager.load("forest/forest_bat_dead.png", Texture.class);
        manager.load("forest/forest_bee_angre.png", Texture.class);
        manager.load("forest/forest_bee_dead.png", Texture.class);
        manager.load("forest/forest_bee_norm.png", Texture.class);
        manager.load("forest/forest_fox.png", Texture.class);
        manager.load("forest/forest_hive.png", Texture.class);
        manager.load("forest/forest_mushroom_1.png", Texture.class);
        manager.load("forest/forest_tree.png", Texture.class);

//        cave

//        CUTSCENE
        manager.load("cutscenes/cutscene1.png", Texture.class);
        manager.load("cutscenes/cutscene1-1.png", Texture.class);
        manager.load("cutscenes/cutscene1-2.png", Texture.class);
        manager.load("cutscenes/cutscene1-3.png", Texture.class);
        manager.load("cutscenes/cutscene1-9.png", Texture.class);
        manager.load("cutscenes/cutscene1-10.png", Texture.class);
        manager.load("cutscenes/cutscene1-11.png", Texture.class);
        manager.load("cutscenes/dialogue window.png", Texture.class);
        manager.load("cutscenes/logo.png", Texture.class);

        manager.load("cutscenes/ship window.png", Texture.class);
        manager.load("cutscenes/ship window-2.png", Texture.class);
        manager.load("cutscenes/The Earth.png", Texture.class);
        manager.load("cutscenes/The Earth-2.png", Texture.class);

//        manager.load("cutscenes/cutscene3-1.png", Texture.class);
//        manager.load("cutscenes/cutscene3-2.png", Texture.class);
        manager.load("cutscenes/cutscene3-3.png", Texture.class);
//        manager.load("cutscenes/cutscene3-4.png", Texture.class);
//        manager.load("cutscenes/cutscene3-5.png", Texture.class);
//        manager.load("cutscenes/cutscene3-8.png", Texture.class);
//        manager.load("cutscenes/cutscene3-9.png", Texture.class);
//        manager.load("cutscenes/cutscene3-10.png", Texture.class);
//        manager.load("cutscenes/cutscene3-12.png", Texture.class);
//        manager.load("cutscenes/cutscene3-13.png", Texture.class);
        manager.load("cutscenes/dialogue window-2.png", Texture.class);
        manager.load("cutscenes/cutscene3(1).png", Texture.class);
        manager.load("cutscenes/cutscene3(2).png", Texture.class);

        manager.load("cave/emerald.png", Texture.class);
        manager.load("cave/geyser.png", Texture.class);
        manager.load("cave/monster.png", Texture.class);
        manager.load("cave/number0.png", Texture.class);
        manager.load("cave/number1.png", Texture.class);
        manager.load("cave/number3.png", Texture.class);
        manager.load("cave/number5.png", Texture.class);
        manager.load("cave/spider.png", Texture.class);
        manager.load("cave/stalactite.png", Texture.class);
        manager.load("cave/waterExit.png", Texture.class);
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
        //forest
        forestAcorn = new TextureRegion(manager.get("forest/forest_acorn.png", Texture.class));
        forestAxe = new TextureRegion(manager.get("forest/forest_axe.png", Texture.class));
        forestBatAlive = new TextureRegion(manager.get("forest/forest_bat_alive.png", Texture.class));
        forestBatDead = new TextureRegion(manager.get("forest/forest_bat_dead.png", Texture.class));
        forestBeeAngr = new TextureRegion(manager.get("forest/forest_bee_angre.png", Texture.class));
        forestBeeDead = new TextureRegion(manager.get("forest/forest_bee_dead.png", Texture.class));
        forestBeeAlive = new TextureRegion(manager.get("forest/forest_bee_norm.png", Texture.class));
        forestFox = new TextureRegion(manager.get("forest/forest_fox.png", Texture.class));
        forestHive = new TextureRegion(manager.get("forest/forest_hive.png", Texture.class));
        forestMushroom = new TextureRegion(manager.get("forest/forest_mushroom_1.png", Texture.class));
        forestTree = new TextureRegion(manager.get("forest/forest_tree.png", Texture.class));
//cave
        caveEmerald = new TextureRegion(manager.get("cave/emerald.png", Texture.class));
        caveGeyser = new TextureRegion(manager.get("cave/geyser.png", Texture.class));
        caveMonster = new TextureRegion(manager.get("cave/monster.png", Texture.class));
        caveNumber0 = new TextureRegion(manager.get("cave/number0.png", Texture.class));
        caveNumber1 = new TextureRegion(manager.get("cave/number1.png", Texture.class));
        caveNumber3 = new TextureRegion(manager.get("cave/number3.png", Texture.class));
        caveNumber5 = new TextureRegion(manager.get("cave/number5.png", Texture.class));
        caveSpider = new TextureRegion(manager.get("cave/spider.png", Texture.class));
        caveStalactite = new TextureRegion(manager.get("cave/stalactite.png", Texture.class));
        caveWaterExit = new TextureRegion(manager.get("cave/waterExit.png", Texture.class));
//      hearts
        Texture tHearts = manager.get("level-ui/hearts.png", Texture.class);

        TextureRegion[][] heartFrames = TextureRegion.split(tHearts, 16, 16);

        fullHeart = heartFrames[0][0];
        poisonHeart = heartFrames[1][0];
        shieldHeart = heartFrames[2][0];
        emptyHeart = heartFrames[3][0];

//        cutscene
        Texture cs1 = manager.get("cutscenes/cutscene1.png", Texture.class);
        TextureRegion[][] texCs1 = TextureRegion.split(cs1, 1280, 720);
        cs1Anim = new Animation<>(0.5f, texCs1[0]);

        Texture cs2W = manager.get("cutscenes/ship window.png", Texture.class);
        TextureRegion[][] texCs2w = TextureRegion.split(cs2W, 1280, 720);
        cs2w = new Animation<>(0.5f, texCs2w[0]);

        Texture cs2E = manager.get("cutscenes/The Earth.png", Texture.class);
        TextureRegion[][] texCs2e = TextureRegion.split(cs2E, 1280, 720);
        cs2e = new Animation<>(0.5f, texCs2e[0]);

        Texture cs31 = manager.get("cutscenes/cutscene3(1).png", Texture.class);
        TextureRegion[][] texCs31 = TextureRegion.split(cs31, 1280, 720);
        cs3One = new Animation<>(0.5f, texCs31[0]);

        Texture cs32 = manager.get("cutscenes/cutscene3(2).png", Texture.class);
        TextureRegion[][] texCs32 = TextureRegion.split(cs32, 1280, 720);
        cs3Two = new Animation<>(0.5f, texCs32[0]);

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

        // run forest
        Texture tForestRunR = manager.get("forest/forest_run_R.png", Texture.class);
        TextureRegion[][] texForestRunR = TextureRegion.split(tForestRunR, 32, 32);
        forestRun_R = new Animation<>(0.15f, texForestRunR[0]);

        Texture tForestRunL = manager.get("forest/forest_run_L.png", Texture.class);
        TextureRegion[][] texForestRunL = TextureRegion.split(tForestRunL, 32, 32);
        forestRun_L = new Animation<>(0.15f, texForestRunL[0]);

        // idle forest
        Texture tForestIdle = manager.get("forest/forest_front.png", Texture.class);
        TextureRegion[][] texForestIdle = TextureRegion.split(tForestIdle, 32, 32);
        forestIdle_F = new Animation<>(0.24f, texForestIdle[0]);

        // jump & fall forest
        forestJump_F = new TextureRegion(manager.get("forest/forest_jump_F.png", Texture.class));
        forestJump_R = new TextureRegion(manager.get("forest/forest_jump_R.png", Texture.class));
        forestJump_L = new TextureRegion(manager.get("forest/forest_jump_L.png", Texture.class));
        forestFall_F = new TextureRegion(manager.get("forest/forest_fall_F.png", Texture.class));
        forestFall_R = new TextureRegion(manager.get("forest/forest_fall_R.png", Texture.class));
        forestFall_L = new TextureRegion(manager.get("forest/forest_fall_L.png", Texture.class));

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
            case 12 : texture = manager.get("cutscenes/dialogue window.png", Texture.class);
                break;
            case 13: texture = manager.get("cutscenes/logo.png", Texture.class);
            break;
            case 14: texture = manager.get("cutscenes/cutscene3-3.png", Texture.class);
                break;
        }
        return texture;
    }
    public Texture cutscene2(int frameNum){
        Texture texture =  manager.get("cutscenes/ship window-2.png", Texture.class);
        switch (frameNum) {
            case 1:
                texture = manager.get("cutscenes/ship window-2.png", Texture.class);
                break;
            case 2:
                texture = manager.get("cutscenes/The Earth-2.png", Texture.class);
                break;
        }
        return  texture;
    }

    public Texture cutscene3(){
        return manager.get("cutscenes/dialogue window-2.png", Texture.class);
    }

    public void disposeImages(){
        manager.dispose();
    }

}
