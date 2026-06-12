package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mandri.storage.UIManager;
import com.mandri.ui.ButtonActions;
import com.mandri.ui.FontCreator;
import com.mandri.ui.PixelButton;


public class MainMenuScreen implements Screen {
    private Main game;
    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport viewport;

    public MainMenuScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(320, 180, camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Skin skin = UIManager.getInstance().getSkin();

        Table table = new Table();
        table.setFillParent(true);

        BitmapFont fontForMainLabel = FontCreator.generateTextFont(24, 1f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fontForMainLabel;

        Label titleLabel = new Label("MANDRi", labelStyle);
        titleLabel.setFontScale(1.5f);

        PixelButton playButton = new PixelButton("START GAME", skin);
        ButtonActions.playGame(playButton, game);

        PixelButton settingsButton = new PixelButton("SETTINGS", skin);
        ButtonActions.openSettings(settingsButton , game);

        PixelButton exitButton = new PixelButton("EXIT", skin);
        ButtonActions.exitGame(exitButton);

        table.add(titleLabel).padBottom(15).row();
        table.add(playButton).width(120).padBottom(10).row();
        table.add(settingsButton).width(120).padBottom(10).row();
        table.add(exitButton).width(120);

        stage.addActor(table);
    }

    @Override
    public void show(){
        game.getManager().getMusic().playMenuMusic();
    }

    // Метод render викликається кожен кадр. "delta" — час у секундах від попереднього виклику.
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

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
        stage.dispose();
    }
}
