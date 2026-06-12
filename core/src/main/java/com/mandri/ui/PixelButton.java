package com.mandri.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PixelButton extends TextButton {
    public PixelButton(String text, Skin skin) {
        super(text, skin);
    }

    public PixelButton(String text, Skin skin, BitmapFont customFont) {
        super(text, skin);

        TextButtonStyle customStyle = new TextButtonStyle(this.getStyle());
        customStyle.font = customFont;

        this.setStyle(customStyle);
    }
}
