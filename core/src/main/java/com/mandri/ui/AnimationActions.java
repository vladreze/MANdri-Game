package com.mandri.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mandri.game.Main;
import com.mandri.game.MainMenuScreen;

public class AnimationActions {

    public static void fadeAnimationBetweenTable(Table tableOne, Table tableTwo){
        tableOne.addAction(Actions.sequence(
            Actions.fadeIn(0.6f),
            Actions.delay(2.0f),
            Actions.fadeOut(0.6f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    tableOne.setVisible(false);
                    tableTwo.setVisible(true);
                    tableTwo.addAction(Actions.fadeIn(0.8f));
                }
            })
        ));
    }

    public static void fadeAnimationBetweenScreenAndTable(Table table, Screen screenToSet, Main game){
        table.addAction(Actions.sequence(
            Actions.delay(0.5f),
            Actions.fadeIn(1.5f),
            Actions.delay(2.0f),
            Actions.fadeOut(1.5f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(screenToSet);
                }
            })
        ));
    }

}
