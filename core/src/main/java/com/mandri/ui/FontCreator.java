package com.mandri.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontCreator {

    public static BitmapFont generateTextFont(int size, float borderWidth){

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/Jersey25-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.borderWidth = borderWidth;

        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;

        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.Nearest;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }
}
