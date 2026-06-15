package com.mandri.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mandri.game.*;
import com.mandri.storage.MusicManager;

public class ButtonActions {

    public static void exitGame(Actor button) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });
    }

    public static void exitGameConfirmaiton(Actor button, Main game){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ConfirmationExitFromGameScreen(game));
            }
        });

    }

    public static void playGame(Actor button, Main game){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getManager().getMusic().stopMusic();
//                game.setScreen(new SpaceScreen(game, game.getManager()));
                game.setScreen(new ForestScreen(game, game.getManager()));
//                game.setScreen(new PlayScreen(game, game.getManager()));
            }
        });
    }

    public static void openSettings(Actor button, Main game, Screen previousScreen){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsMenu(game, previousScreen));
            }
        });
    }

    public static void openSettingsForIconButtons(Actor button, Main game, Screen previousScreen){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsMenu(game, previousScreen));
            }
        });
    }

    public static void backActionDynamic(Actor button, Main game, Screen previousScreen){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(previousScreen);
            }
        });
    }

    public static void openMainMenu(Actor button, Main game){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getManager().getMusic().stopMusic();
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }


    public static void toggleMusic(Slider slider, Main game){

        MusicManager musicManager = game.getManager().getMusic();

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicManager.setVolume(slider.getValue());
            }
        });
    }

    public static void toggleSoundEffects(Slider slider, Main game){
        MusicManager musicManager = game.getManager().getMusic();
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicManager.setSoundEffectsvolume(slider.getValue());
            }
        });
    }

    public static void aboutScreen(Actor button, Main game, Screen previousScreen){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new AboutScreen(game, previousScreen));
            }
        });
    }

    public static void helpScreen(Actor button, Main game, Screen previousScreen){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HelpScreen(game, previousScreen));
            }
        });
    }

    public static void pauseScreen(Actor button, PlayScreen screen) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.pauseGame();
            }
        });
    }

    public static void resumeScreen(Actor button, PlayScreen screen) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.resumeGame();
            }
        });
    }

    public static void exitScreen(Actor button, Table pause, Table confirm){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause.setVisible(false);
                confirm.setVisible(true);
            }
        });
    }

    public static void abortExit(Actor button, Table pause, Table confirm){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause.setVisible(true);
                confirm.setVisible(false);
            }
        });
    }

    public static void addHover(Actor actor){
        actor.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    actor.setColor(Color.YELLOW);
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    actor.setColor(Color.WHITE);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                actor.setColor(Color.GRAY);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (isOver()) {
                    actor.setColor(Color.YELLOW);
                } else {
                    actor.setColor(Color.WHITE);
                }
            }
        });
    }
}
