package com.mandri.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PixelImageButton extends ImageButton {

    public PixelImageButton(Texture iconTexture, Skin skin) {
        super(createStyle(iconTexture, skin));
    }

    private static ImageButtonStyle createStyle(Texture iconTexture, Skin skin) {
        ImageButtonStyle style = new ImageButtonStyle();
        com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle baseStyle =
            skin.get(com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class);
        style.up = baseStyle.up;
        style.over = baseStyle.over;
        style.down = baseStyle.down;

        style.imageUp = new TextureRegionDrawable(new TextureRegion(iconTexture));

        return style;
    }

}
