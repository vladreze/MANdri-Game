package com.mandri.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mandri.game.Main;
import com.mandri.game.MainMenuScreen;
import com.mandri.game.PlayScreen;
import com.mandri.game.SettingsMenu;
import com.mandri.storage.MusicManager;

public class ButtonActions {

    public static void exitGame(PixelButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });
    }

    public static void playGame(PixelButton button, Main game){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getManager().getMusic().stopMusic();
                game.setScreen(new PlayScreen(game.getManager()));
            }
        });
    }

    public static void openSettings(PixelButton button, Main game){
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               game.setScreen(new SettingsMenu(game));
            }
        });
    }

    public static void backAction(PixelButton button, Main game){
        button.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y){
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
}
