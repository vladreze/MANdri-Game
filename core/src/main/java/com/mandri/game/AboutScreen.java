package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mandri.storage.UIManager;
import com.mandri.ui.ButtonActions;
import com.mandri.ui.FontCreator;
import com.mandri.ui.PixelButton;
import com.mandri.ui.PixelImageButton;

public class AboutScreen implements Screen {
    private Main game;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Stage stage;

    public AboutScreen(Main game){
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(320, 180, camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Skin skin = UIManager.getInstance().getSkin();

        BitmapFont textMainFont = FontCreator.generateTextFont(24, 1f);
        Label.LabelStyle textMainStyle = new Label.LabelStyle();
        textMainStyle.font = textMainFont;

        BitmapFont textFont = FontCreator.generateTextFont(16, 1f);
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.font = textFont;

        BitmapFont textCopyRightFont = FontCreator.generateTextFont(12, 1f);
        Label.LabelStyle textCopyRightStyle = new Label.LabelStyle();
        textCopyRightStyle.font = textCopyRightFont;

        Texture backIcon = new Texture(Gdx.files.internal("assets/ui/back-button.png"));

        PixelImageButton backButton = new PixelImageButton(backIcon, skin);
        backButton.setSize(20, 20);
        backButton.setPosition(10, 150);
        ButtonActions.openSettingsForIconButtons(backButton, game);

        Table scrollContainer = new Table();
        scrollContainer.top();
        Label titleLabel = new Label("MANdri Game", textMainStyle);
        titleLabel.setFontScale(1.2f);
        scrollContainer.add(titleLabel).padBottom(10).row();

        Label aboutLabel = new Label("ABOUT THE GAME", textStyle);
        aboutLabel.setFontScale(0.8f);
        scrollContainer.add(aboutLabel).padBottom(2).row();

        Label descriptionLabel = new Label("MANDRi is an interactive 2D platformer. Travel through three unique biomes (Space, Forest, Cave), overcome obstacles and collect bonuses to reveal the final plot of the game.", textStyle);
        descriptionLabel.setFontScale(0.65f);
        descriptionLabel.setWrap(true);
        scrollContainer.add(descriptionLabel).width(260).padBottom(15).row();

        Label teamLabel = new Label("DEVELOPMENT TEAM:\n" +
            "- Ivan Bozhenko - Core Developer (Physics, Game Loop, Collisions)\n" +
            "- Daria Radko - Level Designer (Maps, Enemies, Bonuses)\n" +
            "- Vladyslav Rezanov - UI & Flow (Menus, Screens, HUD)\n" +
            "- Sofiia Bordachenko - Cutscenes & Audio Integrator", textStyle);
        teamLabel.setFontScale(0.65f);
        teamLabel.setWrap(true);
        scrollContainer.add(teamLabel).width(260).padBottom(15).row();

        Label technicalStackLabel = new Label("TECHNICAL STACK:\nJava | LibGDX | Gradle", textStyle);
        technicalStackLabel.setFontScale(0.65f);
        technicalStackLabel.setWrap(true);
        scrollContainer.add(technicalStackLabel).width(260).padBottom(15).row();

        Label copyrightLabel = new Label("© 2026 MANDRi Team. All rights reserved.", textCopyRightStyle);
        copyrightLabel.setFontScale(0.55f);
        scrollContainer.add(copyrightLabel).padBottom(5).row();

        ScrollPane scrollPane = new ScrollPane(scrollContainer, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsVisible(false);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        mainTable.add(scrollPane).width(305).height(140).padTop(25);

        stage.addActor(mainTable);
        stage.addActor(backButton);

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
    public void dispose() {stage.dispose();}
}
