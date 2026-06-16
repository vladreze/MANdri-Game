package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.mandri.ui.PixelButton;
import com.mandri.ui.PixelImageButton;

public class HelpScreen implements Screen {
    private Main game;
    private Stage stage;
    private FitViewport viewport;
    private OrthographicCamera camera;

    private Screen previousScreen;

    public HelpScreen(Main game, Screen previousScreen){
        this.game = game;
        this.previousScreen = previousScreen;

        camera = new OrthographicCamera();
        viewport = new FitViewport(320, 180, camera);

        stage = new Stage(viewport);

        Skin skin = UIManager.getInstance().getSkin();

        Texture backIcon = new Texture(Gdx.files.internal("assets/ui/back-button.png"));

        PixelImageButton backButton = new PixelImageButton(backIcon, skin);
        backButton.setSize(20, 20);
        backButton.setPosition(10, 150);
        ButtonActions.backActionDynamic(backButton, game, previousScreen);


        Table table = new Table();
        table.setFillParent(true);
        table.center();

        BitmapFont textFont = FontCreator.generateTextFont(16, 1f);
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.font = textFont;

        Label titleLabel = new Label("HOW TO PLAY", textStyle);
        titleLabel.setFontScale(1.2f);
        table.add(titleLabel).padBottom(15).row();

        Label moveLabel = new Label("A / D - Move", textStyle);
        moveLabel.setFontScale(0.7f);
        table.add(moveLabel).padBottom(8).row();

        Label jumpLabel = new Label("SPACE - Jump", textStyle);
        jumpLabel.setFontScale(0.7f);
        table.add(jumpLabel).padBottom(8).row();

        Label pauseLabel = new Label("ESC - Pause Game", textStyle);
        pauseLabel.setFontScale(0.7f);
        table.add(pauseLabel).padBottom(8).row();

        Label inventoryLabel = new Label("E -  Open / Close Inventory", textStyle);
        inventoryLabel.setFontScale(0.7f);
        table.add(inventoryLabel).padBottom(8).row();

        Label interactLabel = new Label("F -  Interact with Objects", textStyle);
        interactLabel.setFontScale(0.7f);
        table.add(interactLabel).padBottom(8).row();

        Label tipLabel = new Label("Avoid enemies and collect bonuses to unlock new biomes ( levels )", textStyle);
        tipLabel.setFontScale(0.6f);
        tipLabel.setWrap(true);
        tipLabel.setAlignment(Align.center);
        table.add(tipLabel).width(260).padBottom(10).row();


        stage.addActor(table);
        stage.addActor(backButton);

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
