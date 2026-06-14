package com.mandri.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mandri.storage.MainAssetsManager;
import com.mandri.ui.FontCreator;

public class CutsceneScreen implements Screen {
    private String[] storyText;
    private Texture[] cutsceneImg;
    private int currentSlide = 0;

    private Texture textBg;

    private final Main main;
    private final MainAssetsManager manager;

    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    private boolean isTyping = true;
    private String displayedText = "";
    private float timer = 0f;
    private final float TYPING_DELAY = 0.1f;
    private int charIndex = 0;

    public CutsceneScreen(Main main, MainAssetsManager manager) {
        this.main = main;
        this.manager = manager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = FontCreator.generateTextFont(20, 2f);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320f, 180f);

        //bg opacity
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.7f);
        pixmap.fill();
        textBg = new Texture(pixmap);
        pixmap.dispose();

        cutsceneImg = new Texture[]{
          manager.image.cutscene1(1),
            manager.image.cutscene1(2),
            manager.image.cutscene1(3),
            manager.image.cutscene1(2),
            manager.image.cutscene1(1),
            manager.image.cutscene1(2),
            manager.image.cutscene1(3),
            manager.image.cutscene1(2),
            manager.image.cutscene1(9),
            manager.image.cutscene1(10),
            manager.image.cutscene1(11),

            //2
            //3
            manager.image.cutscene2(1),
            manager.image.cutscene2(2),
            manager.image.cutscene2(10),
            manager.image.cutscene2(4),
            manager.image.cutscene2(3),
            manager.image.cutscene2(4),
            manager.image.cutscene2(10),
            manager.image.cutscene2(6),
            manager.image.cutscene2(5),
            manager.image.cutscene2(6),
            manager.image.cutscene2(7),
            manager.image.cutscene2(8),
            manager.image.cutscene2(9),
            manager.image.cutscene2(10)
        };

        storyText = new String[]{
          "[System]:Warning! An unknown object is approaching the ship at high speed.\n Collision is imminent. Warning!...",
            "[]",
            "",
            ""
        };

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    @Override
    public void dispose() {
        manager.disposeAll();
        batch.dispose();
        font.dispose();
        textBg.dispose();
    }
}
