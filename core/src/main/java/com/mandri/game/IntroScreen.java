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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mandri.storage.UIManager;
import com.mandri.ui.AnimationActions;
import com.mandri.ui.FontCreator;

public class IntroScreen implements Screen {
    private Main game;
    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport viewport;

    public IntroScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(320, 180, camera);

        Skin skin = UIManager.getInstance().getSkin();

        stage = new Stage(viewport);

        Table table = new Table();
        table.setFillParent(true);

        BitmapFont textMainFont = FontCreator.generateTextFont(28, 1f);
        Label.LabelStyle textMainStyle = new Label.LabelStyle();
        textMainStyle.font = textMainFont;

        BitmapFont textFont = FontCreator.generateTextFont(18, 1f);
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.font = textFont;

        Label teamNameLabel = new Label("something genius team", textMainStyle);
        teamNameLabel.setAlignment(Align.center);

        Label presentLabel = new Label("presents", textStyle);
        presentLabel.setAlignment(Align.center);

        table.add(teamNameLabel).padBottom(10).row();
        table.add(presentLabel).row();

        table.getColor().a = 0f;

        stage.addActor(table);

        AnimationActions.fadeAnimationBetweenScreenAndTable(table, new MainMenuScreen(game), game);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1f);
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
