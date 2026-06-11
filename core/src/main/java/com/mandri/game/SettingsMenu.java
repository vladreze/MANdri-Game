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
        table.setFillParent(true);
        table.top().padTop(15);
        table.add(titleLabel).row();

        PixelButton backButton = new PixelButton("<-", skin);
        backButton.setSize(20, 20);
        backButton.setPosition(10, 150);
        ButtonActions.backAction(backButton, game);

        PixelButton aboutButton = new PixelButton("i" , skin);
        aboutButton.setSize(20, 20);
        aboutButton.setPosition(290, 10);
        ButtonActions.aboutScreen(aboutButton, game);

        Label volumeLabel = new Label("VOLUME", skin);
        volumeLabel.setPosition(30, 100);

        MusicManager musicManager = game.getManager().getMusic();
        Slider volumeSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        volumeSlider.setSize(110, 15);
        volumeSlider.setPosition(30, 75);
        volumeSlider.setValue(musicManager.getVolume());
        ButtonActions.toggleMusic(volumeSlider, game);

        Label soundEffectsvolumeLabel = new Label("SOUND EFFECTS" , skin);
        soundEffectsvolumeLabel.setPosition(180, 100);

        Slider soundEffectsvolumeSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        soundEffectsvolumeSlider.setSize(110, 15);
        soundEffectsvolumeSlider.setPosition(180, 75);
        soundEffectsvolumeSlider.setValue(musicManager.getSoundEffectsvolume());
        ButtonActions.toggleSoundEffects(soundEffectsvolumeSlider, game);

        stage.addActor(table);
        stage.addActor(backButton);
        stage.addActor(aboutButton);
        stage.addActor(volumeLabel);
        stage.addActor(volumeSlider);
        stage.addActor(soundEffectsvolumeLabel);
        stage.addActor(soundEffectsvolumeSlider);


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
