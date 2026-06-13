package com.mandri.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class AnimationActions {

    public static void gameOverFade(Table heartTable, Table gameOverTable){
        heartTable.addAction(Actions.sequence(
            Actions.fadeIn(0.6f),     // Плавно з'являється за 0.6 сек
            Actions.delay(1.0f),      // Зависає на екрані на 1 секунду
            Actions.fadeOut(0.6f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    heartTable.setVisible(false);
                    gameOverTable.setVisible(true);
                    gameOverTable.addAction(Actions.fadeIn(0.8f));
                }
            })
        ));
    }

}
