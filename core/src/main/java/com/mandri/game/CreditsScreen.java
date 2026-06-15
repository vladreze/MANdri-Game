package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mandri.storage.MainAssetsManager;
import com.mandri.ui.FontCreator;

public class CreditsScreen implements Screen {
    private MainAssetsManager manager;
    private Main main;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;
    private GlyphLayout layout;

    private String creditsText;
    private float scrollY;
    private final float SCROLL_SPEED = 60f;

    private Texture bg;

    public CreditsScreen(Main main, MainAssetsManager manager){
        this.main = main;
        this.manager = manager;
    }
    @Override
    public void show() {
        batch = new SpriteBatch();
        font = FontCreator.generateTextFont(30, 2f);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        bg = manager.image.creditsBg();
        creditsText = "MANdri\n\n\n\n" +
            "The game was worked on by...\n\n\n" +
            "Core Developer:                      Ivan Bozhenko \n\n"+
        "Level/Entity Designer:        Daria Radko \n\n" +
            "UI & Flow Flow:                                  Vladyslav Rezanov \n\n" +
            " Cutscenes & Audio Integrator:      Bordachenko Sofiia \n\n\n\n" +
            "Language:                         Java\n\n" +
            "Library:                          LibGDX\n\n\n\n" +

            "Special Thanks:                   to my best team\n\n" +
            "Love u guys!";
        layout = new GlyphLayout();
        layout.setText(font, creditsText, Color.WHITE, 1280, Align.center, false);
        scrollY = 150f;

        manager.music.playCreditsMusic();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            exitToMenu();
            return;
        }

        scrollY += SCROLL_SPEED * delta;

        if (scrollY - layout.height > 720 + 100) {
            exitToMenu();
            return;
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(bg, 0, 0, 1280, 720);
        font.draw(batch, layout, 0, scrollY);

        batch.end();
    }

    private void exitToMenu() {
         manager.music.stopMusic();
        main.setScreen(new MainMenuScreen(main));
    }

    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void dispose() {
        manager.disposeAll();
        batch.dispose();
        font.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}
