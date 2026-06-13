package com.mandri.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

public class SliderStyle {
    public static Slider.SliderStyle sliderStyle(Skin skin, Color knobColor){
        Slider.SliderStyle baseStyle = skin.get("default-horizontal", Slider.SliderStyle.class);
        Slider.SliderStyle customStyle = new Slider.SliderStyle(baseStyle);
        if (baseStyle.knob != null) {
            customStyle.knob = skin.newDrawable(baseStyle.knob, knobColor);
        }

        if (baseStyle.knobDown != null) {
            Color downColor = new Color(knobColor).lerp(Color.BLACK, 0.3f);
            customStyle.knobDown = skin.newDrawable(baseStyle.knobDown, downColor);
        }
        if (baseStyle.knobOver != null) {
            Color overColor = new Color(knobColor).lerp(Color.WHITE, 0.3f);
            customStyle.knobOver = skin.newDrawable(baseStyle.knobOver, overColor);
        }

        return customStyle;
    }
}
