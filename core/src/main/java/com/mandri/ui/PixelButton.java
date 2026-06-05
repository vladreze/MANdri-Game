package com.mandri.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PixelButton extends TextButton {
    public PixelButton(String text, Skin skin) {
        super(text, skin);
        this.pad(10, 20, 10, 20);
        this.getLabel().setFontScale(1.0f);
    }
}
