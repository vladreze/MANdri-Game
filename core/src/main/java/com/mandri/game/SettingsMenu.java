package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mandri.storage.MusicManager;
import com.mandri.storage.UIManager;
import com.mandri.ui.ButtonActions;
import com.mandri.ui.FontCreator;
import com.mandri.ui.PixelButton;
import com.mandri.ui.PixelImageButton;

public class SettingsMenu implements Screen {
    private Main game;
    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private Screen previousScreen;

    public SettingsMenu(Main game, Screen previousScreen){
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(320, 180, camera);

        stage = new Stage(viewport);

        Skin skin = UIManager.getInstance().getSkin();

        Table table = new Table();
        table.setFillParent(true);

        BitmapFont textMainFont = FontCreator.generateTextFont(24, 1f);
        Label.LabelStyle styleMainFont = new Label.LabelStyle();
        styleMainFont.font = textMainFont;

        BitmapFont textFont = FontCreator.generateTextFont(16, 1f);
        Label.LabelStyle styleTextFont = new Label.LabelStyle();
        styleTextFont.font = textFont;

        Label titleLabel = new Label("SETTINGS", styleMainFont);
        titleLabel.setFontScale(1.5f);
        table.setFillParent(true);
        table.top().padTop(15);
        table.add(titleLabel).row();

        Texture backIcon = new Texture(Gdx.files.internal("assets/ui/back-button.png"));

        PixelImageButton backButton = new PixelImageButton(backIcon, skin);
        backButton.setSize(20, 20);
        backButton.setPosition(10, 150);
        ButtonActions.backActionDynamic(backButton, game, previousScreen);

        Texture aboutIcon = new Texture(Gdx.files.internal("assets/ui/info-button.png"));

        PixelImageButton aboutButton = new PixelImageButton(aboutIcon , skin);
        aboutButton.setSize(20, 20);
        aboutButton.setPosition(290, 10);
        ButtonActions.aboutScreen(aboutButton, game, this);

        Texture helpIcon = new Texture(Gdx.files.internal("assets/ui/help-button.png"));

        PixelImageButton helpButton = new PixelImageButton(helpIcon, skin);
        helpButton.setSize(20, 20);
        helpButton.setPosition(265, 10);
        ButtonActions.helpScreen(helpButton, game, this);


        Label volumeLabel = new Label("VOLUME", styleTextFont);
        volumeLabel.setPosition(30, 80);

        MusicManager musicManager = game.getManager().getMusic();
        Slider volumeSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        volumeSlider.setSize(110, 15);
        volumeSlider.setPosition(30, 55);
        volumeSlider.setValue(musicManager.getVolume());
        ButtonActions.toggleMusic(volumeSlider, game);

        Label soundEffectsvolumeLabel = new Label("SOUND EFFECTS" , styleTextFont);
        soundEffectsvolumeLabel.setPosition(180, 80);

        Slider soundEffectsvolumeSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        soundEffectsvolumeSlider.setSize(110, 15);
        soundEffectsvolumeSlider.setPosition(180, 55);
        soundEffectsvolumeSlider.setValue(musicManager.getSoundEffectsvolume());
        ButtonActions.toggleSoundEffects(soundEffectsvolumeSlider, game);

        stage.addActor(table);
        stage.addActor(backButton);
        stage.addActor(aboutButton);
        stage.addActor(helpButton);
        stage.addActor(volumeLabel);
        stage.addActor(volumeSlider);
        stage.addActor(soundEffectsvolumeLabel);
        stage.addActor(soundEffectsvolumeSlider);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

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
