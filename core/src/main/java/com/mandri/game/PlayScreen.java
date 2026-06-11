package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mandri.entities.Player;
import com.mandri.storage.MainAssetsManager;

public class PlayScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    private Texture background;
    private Player player;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private final MainAssetsManager manager;
    private final Main game;

    private final float floorHeight = 64f;

    private float playerStartX = 20f;
    private float playerStartY = 250f;

    private ShaderProgram vignetteShader;

    public PlayScreen(Main game ,MainAssetsManager manager){
        this.game = game;
        this.manager = manager;
    }
    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 256);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, 800, 400);

        background = manager.image.spaceBg();
        map = new TmxMapLoader().load("assets/maps/spaceMap/space.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        player = new Player(playerStartX, playerStartY, manager);
        manager.music.playLevelMusic(1);

        vignetteShader = new ShaderProgram(
            Gdx.files.internal("shaders/default.vsh"),
            Gdx.files.internal("shaders/vignette.fsh")
        );

        if (!vignetteShader.isCompiled()) {
            Gdx.app.log("Shader Error", vignetteShader.getLog());
            Gdx.app.exit();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("ground");
        player.update(delta, collisionLayer, 2720f);

        renderer.setView(camera);
        renderer.render();

        float cameraX = MathUtils.clamp(player.bounds.getX(), 300f, 2720f - 300f);

        camera.position.set(cameraX, 128f, 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.setShader(vignetteShader);
        batch.begin();

        player.draw(batch);
        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);

        batch.setShader(null);
        batch.begin();

        float startX = 20f;
        final float startY = 362f;

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

        if (player.isDead()) {
            game.setScreen(new PlayScreen(game, manager));
        }
    }

    @Override
    public void resize(int i, int i1) {
        camera.viewportWidth = 600;
        camera.viewportHeight = 256;
        camera.update();
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
    }
}
