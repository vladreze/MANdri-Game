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
import com.mandri.ui.ButtonActions;
import com.mandri.ui.FontCreator;

public class ConfirmationExitFromGameScreen implements Screen {

    private Main game;
    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport viewport;


    public ConfirmationExitFromGameScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(320,180, camera);

        stage = new Stage(viewport);
        Skin skin = UIManager.getInstance().getSkin();

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        BitmapFont fontExitText = FontCreator.generateTextFont(18, 1f);
        Label.LabelStyle exitStyle = new Label.LabelStyle();
        exitStyle.font = fontExitText;

        BitmapFont fontButtons = FontCreator.generateTextFont(20, 1f);
        Label.LabelStyle buttonStyle = new Label.LabelStyle();
        buttonStyle.font = fontButtons;

        Label exitLabel = new Label("ARE YOU SURE\n YOU WANT TO EXIT THIS GAME?", exitStyle);
        exitLabel.setAlignment(Align.center);


        Label yesLabel = new Label("YES", buttonStyle );
        Label noLabel = new Label("NO", buttonStyle);

        ButtonActions.openMainMenu(noLabel, game);
        ButtonActions.exitGame(yesLabel);

        ButtonActions.addHover(yesLabel);
        ButtonActions.addHover(noLabel);

        table.add(exitLabel).colspan(2).padBottom(30).row();
        table.add(yesLabel).padRight(40);
        table.add(noLabel);

        stage.addActor(table);


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
