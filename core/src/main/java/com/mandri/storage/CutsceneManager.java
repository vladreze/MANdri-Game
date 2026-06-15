package com.mandri.storage;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mandri.game.*;

public class CutsceneManager {
    private Main main;
    private MainAssetsManager manager;
    private CutsceneScreen cutsceneScreen;
    public CutsceneManager(Main main, MainAssetsManager manager){
        this.main = main;
        this.manager = manager;
    }

    public Screen cs1(){
        Texture[] texture = new Texture[]{
            manager.image.cutscene1(2),
            manager.image.cutscene1(9),
            manager.image.cutscene1(10),
            manager.image.cutscene1(11),
            manager.image.cutscene1(12),
            manager.image.cutscene1(12),
            manager.image.cutscene3(3),
            manager.image.cutscene1(13)
            };

        String[] text = new String[]{
            "[System]: Warning!",
            "[System]: An unknown object is approaching the ship at high speed. \n Collision is imminent.",
            "[System]: Warni...",
            "...",
            "[You]: Aaah...What happened?\n Where am I?",
            "[You]:Oh no, my spaceship broke down! \n I need to find the parts that fell off.",
            "And so my journey began...",
            ""
            };

        Screen screen = new SpaceScreen(main, manager);
        Vector2[] textPositions = new Vector2[] {
            new Vector2(50, 200),
            new Vector2(50, 200),
            new Vector2(50, 200),

            new Vector2(600, 200),

            new Vector2(300, 200),
            new Vector2(300, 200),
            new Vector2(500, 360),
            new Vector2(0, 0),

        };

        Animation<TextureRegion> anim = manager.image.cs1Anim;;

        int id =1;
        cutsceneScreen = new CutsceneScreen(main, manager, texture,text, screen, anim, textPositions, id);
        return cutsceneScreen;
    }
    public Screen cs2(){
    //3
////            manager.image.cutscene3(1),
////            manager.image.cutscene3(2),
////            manager.image.cutscene3(3),
////            manager.image.cutscene3(5),
////            manager.image.cutscene3(4),
////            manager.image.cutscene3(5),
////            manager.image.cutscene3(3),
////            manager.image.cutscene3(6),
////            manager.image.cutscene3(5),
////            manager.image.cutscene3(6),
////            manager.image.cutscene3(7),
////            manager.image.cutscene3(8),
////            manager.image.cutscene3(9),
////            manager.image.cutscene3(3)
//        };

//            //2
//            "[You]:Finally I'm flying home...",
//            //3
//            "[You]:Damn, I've fallen somewhere again. \n Well, let's see where this road leads..."
//        };
//
        Texture[] texture = new Texture[]{

        };

        String[] text = new String[]{

        };

        Screen screen = new ForestScreen(main, manager);
        Vector2[] textPositions = new Vector2[] {


        };

        Animation<TextureRegion> anim = manager.image.cs1Anim;;

        int id =2;
        cutsceneScreen = new CutsceneScreen(main, manager, texture,text, screen, anim, textPositions, id);
        return cutsceneScreen;
    }
    public Screen cs3(){
        Texture[] texture = new Texture[]{

        };

        String[] text = new String[]{

        };

        Screen screen = new SpaceScreen(main, manager);
        Vector2[] textPositions = new Vector2[] {


        };

        Animation<TextureRegion> anim = manager.image.cs1Anim;;

        int id =3;
        cutsceneScreen = new CutsceneScreen(main, manager, texture,text, screen, anim, textPositions, id);
        return cutsceneScreen;
    }
    public Screen cs4(){
        Texture[] texture = new Texture[]{

        };

        String[] text = new String[]{

        };

        Screen screen = new SpaceScreen(main, manager);
        Vector2[] textPositions = new Vector2[] {


        };

        Animation<TextureRegion> anim = manager.image.cs1Anim;;

        int id =4;
        cutsceneScreen = new CutsceneScreen(main, manager, texture,text, screen, anim, textPositions, id);
        return cutsceneScreen;
    }
}
