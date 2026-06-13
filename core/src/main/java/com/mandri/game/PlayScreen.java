package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mandri.entities.Player;
import com.mandri.storage.MainAssetsManager;
import com.mandri.storage.UIManager;
import com.mandri.ui.ButtonActions;
import com.mandri.ui.FontCreator;
import com.mandri.ui.PixelButton;
import com.mandri.ui.PixelImageButton;

public class PlayScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Stage hudStage;

    private Texture background;
    private Player player;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private final MainAssetsManager manager;
    private final Main game;

    private float playerStartX = 160f;
    private float playerStartY = 85f;

    private ShaderProgram vignetteShader;

    private final float playerCameraWidth = 320f;
    private final float playerCameraHeight = 180f;

    private final float hudCameraWidth = 640f;
    private final float hudCameraHeight = 360f;

    private float mapWidth;
    private float mapHeight;

    private boolean isPaused = false;
    private Table pauseTable;
    private Texture dimBackground;
    private boolean isInitialized = false;

    public PlayScreen(Main game ,MainAssetsManager manager){
        this.game = game;
        this.manager = manager;
    }
    @Override
    public void show() {
        if(!isInitialized) {
            batch = new SpriteBatch();

            camera = new OrthographicCamera();
            camera.setToOrtho(false, playerCameraWidth, playerCameraHeight);

            hudCamera = new OrthographicCamera();
            hudCamera.setToOrtho(false, hudCameraWidth, hudCameraHeight);

            background = manager.image.spaceBg();

            map = new TmxMapLoader().load("assets/maps/spaceMap/space.tmx");
            MapProperties properties = map.getProperties();

            int mapWidthInTiles = properties.get("width", Integer.class);
            int tilePixelWidth = properties.get("tilewidth", Integer.class);

            int mapHeightInTiles = properties.get("height", Integer.class);
            int tilePixelHeight = properties.get("tileheight", Integer.class);

            mapWidth = mapWidthInTiles * tilePixelWidth;
            mapHeight = mapHeightInTiles * tilePixelHeight;

            renderer = new OrthogonalTiledMapRenderer(map);
            player = new Player(playerStartX, playerStartY, manager);
            manager.music.playLevelMusic(1);

            vignetteShader = new ShaderProgram(
                Gdx.files.internal("shaders/default.vsh"),
                Gdx.files.internal("shaders/vignette.fsh")
            );

            vignetteShader.pedantic = false;

            if (!vignetteShader.isCompiled()) {
                Gdx.app.log("Shader Error", vignetteShader.getLog());
                Gdx.app.exit();
            }

            renderer.getBatch().setShader(vignetteShader);

            hudStage = new Stage(new com.badlogic.gdx.utils.viewport.FitViewport(hudCameraWidth, hudCameraHeight));

            Skin skin = UIManager.getInstance().getSkin();
            Texture pauseIcon = new Texture(Gdx.files.internal("assets/ui/pause-icon.png"));

            PixelImageButton pauseButton = new PixelImageButton(pauseIcon, skin);
            pauseButton.setSize(30, 30);
            pauseButton.setPosition(hudCameraWidth - 60, hudCameraHeight - 60);

            ButtonActions.pauseScreen(pauseButton, this);
            hudStage.addActor(pauseButton);

            pauseTable = new Table();
            pauseTable.setFillParent(true);
            pauseTable.setVisible(false);

            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(0f, 0f, 0f, 0.8f);
            pixmap.fill();
            dimBackground = new Texture(pixmap);
            pixmap.dispose();

            pauseTable.setBackground(new TextureRegionDrawable(dimBackground));

            BitmapFont fontForPauseLabel = FontCreator.generateTextFont(35, 1f);
            Label.LabelStyle labelPauseTextStyle = new Label.LabelStyle();
            labelPauseTextStyle.font = fontForPauseLabel;
            Label pauseLabel = new Label("PAUSED", labelPauseTextStyle);

            BitmapFont fontText = FontCreator.generateTextFont(24, 1f);
            Label.LabelStyle textStyle = new Label.LabelStyle();
            textStyle.font = fontText;

            Label resumeTextBtn = new Label("RESUME", textStyle);
            Label settingsTextBtn = new Label("SETTINGS", textStyle);
            Label exitTextBtn = new Label("EXIT", textStyle);

            ButtonActions.resumeScreen(resumeTextBtn, this);
            ButtonActions.openSettings(settingsTextBtn, game, this);
            ButtonActions.openMainMenu(exitTextBtn, game);

            ButtonActions.addHover(resumeTextBtn);
            ButtonActions.addHover(settingsTextBtn);
            ButtonActions.addHover(exitTextBtn);


            pauseTable.add(pauseLabel).padBottom(30).row();
            pauseTable.add(resumeTextBtn).padBottom(15).row();
            pauseTable.add(settingsTextBtn).padBottom(15).row();
            pauseTable.add(exitTextBtn).padBottom(15).row();

            hudStage.addActor(pauseTable);
            isInitialized = true;
        }
       Gdx.input.setInputProcessor(hudStage);



    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            if (isPaused) resumeGame();
            else pauseGame();
        }

        if(!isPaused) {
            TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("ground");
            MapLayer objectLayer = (MapLayer) map.getLayers().get("collisions");
            player.update(delta, collisionLayer, objectLayer, 2720f);

            float cameraX = MathUtils.clamp(player.bounds.getX(), (playerCameraWidth / 2), mapWidth - (playerCameraWidth / 2));
            float cameraY = MathUtils.clamp(player.bounds.getY(), (playerCameraHeight / 2), mapHeight);

            camera.position.set(cameraX, cameraY, 0);

            if (player.isShaking()) {
                float shakePower = .5f;

                camera.position.x += MathUtils.random(-shakePower, shakePower);
                camera.position.y += MathUtils.random(-shakePower, shakePower);
            }

            camera.update();
        }

        renderer.setView(camera);
        vignetteShader.bind();
        vignetteShader.setUniformf("u_resolution", Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null);
        batch.begin();
        player.draw(batch);
        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.setShader(null);
        batch.begin();

        float startX = 20f;
        final float startY = hudCameraHeight-40f;

        float heartWidth = 24f;
        float heartHeight = 24f;

        float spacing = 6f;

        int maxLives = 3;

        for (int i = 0; i < maxLives; i++) {
            float currentX = startX + i * (heartWidth + spacing);
            if (i < player.liveCount) {
                batch.draw(manager.image.fullHeart, currentX, startY, heartWidth, heartHeight);
            } else  {
                batch.draw(manager.image.emptyHeart, currentX, startY, heartWidth, heartHeight);
            }
        }

        batch.end();

        hudStage.act(delta);
        hudStage.draw();

        if (player.isDead()) {
            game.setScreen(new MainMenuScreen(game));
            manager.music.stopMusic();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = playerCameraWidth;
        camera.viewportHeight = playerCameraHeight;
        camera.update();
        if (hudStage != null) {
            hudStage.getViewport().update(width, height, true);
        }
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
        batch.dispose();
        map.dispose();
        renderer.dispose();
        vignetteShader.dispose();
        manager.disposeAll();
        if (dimBackground != null) {
            dimBackground.dispose();
        }
    }

    public void pauseGame() {
        isPaused = true;
        pauseTable.setVisible(true);
    }

    public void resumeGame() {
        isPaused = false;
        pauseTable.setVisible(false);
    }
}
