package com.mandri.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
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

    public static void playGame(Actor button, Main game){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getManager().getMusic().stopMusic();
                game.setScreen(new PlayScreen(game, game.getManager()));
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


}
