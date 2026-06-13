package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mandri.storage.UIManager;
import com.mandri.ui.ButtonActions;
import com.mandri.ui.FontCreator;

public class GameOverScreen implements Screen {

    private Main game;
    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private Table heartTable;
    private Table gameOverTable;

    private float stateTimer = 0f;
    private boolean isHeartPart = true;
    private final float HEART_DURATION = 1.5f;

    public GameOverScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();

        viewport = new FitViewport(320,180, camera);
        stage = new Stage(viewport);

        Skin skin = UIManager.getInstance().getSkin();

        BitmapFont fontGameOver = FontCreator.generateTextFont(40, 1f);
        Label.LabelStyle gameOverStyle = new Label.LabelStyle();
        gameOverStyle.font = fontGameOver;
        gameOverStyle.fontColor = com.badlogic.gdx.graphics.Color.RED;

        BitmapFont fontButtons = FontCreator.generateTextFont(20, 1f);
        Label.LabelStyle buttonStyle = new Label.LabelStyle();
        buttonStyle.font = fontButtons;

        heartTable = new Table();
        heartTable.setFillParent(true);

        Image emptyHeart = new Image(game.getManager().image.emptyHeart);
        heartTable.add(emptyHeart).size(40, 40).row();

        gameOverTable = new Table();
        gameOverTable.setFillParent(true);
        gameOverTable.setVisible(false);

        Label gameOverLabel = new Label("GAME OVER", gameOverStyle);
        Label returnBtn = new Label("MAIN MENU", buttonStyle);
        Label tryAgainBtn = new Label("TRY AGAIN", buttonStyle);

        ButtonActions.playGame(tryAgainBtn, game);
        ButtonActions.openMainMenu(returnBtn, game);

        ButtonActions.addHover(tryAgainBtn);
        ButtonActions.addHover(returnBtn);

        gameOverTable.add(gameOverLabel).padBottom(30).row();
        gameOverTable.add(tryAgainBtn).padBottom(15).row();
        gameOverTable.add(returnBtn);

        stage.addActor(heartTable);
        stage.addActor(gameOverTable);





    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(isHeartPart){
            stateTimer += delta;
            if(stateTimer >= HEART_DURATION){
                isHeartPart = false;
                heartTable.setVisible(false);
                gameOverTable.setVisible(true);
            }
        }

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);

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
