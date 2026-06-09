package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mandri.storage.MusicManager;
import com.mandri.storage.UIManager;
import com.mandri.ui.ButtonActions;
import com.mandri.ui.PixelButton;

public class SettingsMenu implements Screen {
    private Main game;
    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport viewport;

    public SettingsMenu(Main game){
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(320, 180, camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Skin skin = UIManager.getInstance().getSkin();

        Table table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label("SETTINGS", skin);
        titleLabel.setFontScale(1.5f);

        MusicManager musicManager = game.getManager().getMusic();
        Label volumeLabel = new Label("VOLUME", skin);
        Slider volumeSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        ButtonActions.toggleMusic(volumeSlider, game);
        volumeSlider.setValue(musicManager.getVolume());
        volumeSlider.setPosition(100, 40);
        volumeSlider.setSize(120, 10);



        PixelButton backButton = new PixelButton("<-", skin);
        backButton.setPosition(10, 120);
        backButton.setSize(20, 20);
        ButtonActions.backAction(backButton, game);

        table.add(titleLabel).padBottom(30).row();
        table.add(volumeLabel).padBottom(20).row();
        stage.addActor(table);
        stage.addActor(backButton);
        stage.addActor(volumeSlider);


    }



    @Override
    public void show() {

    }

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
