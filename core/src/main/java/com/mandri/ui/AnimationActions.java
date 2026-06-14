package com.mandri.ui;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class AnimationActions {

    public static void fadeAnimation(Table tableOne, Table tableTwo){
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

}
