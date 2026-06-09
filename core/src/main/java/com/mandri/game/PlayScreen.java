package com.mandri.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private Texture background;
    private Player player;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private final MainAssetsManager manager;

    private final float floorHeight = 64f;

    public PlayScreen(MainAssetsManager manager){
        this.manager = manager;
    }
    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 256);
        background = manager.image.spaceBg();
        map = new TmxMapLoader().load("assets/maps/spaceMap/space.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        player = new Player(400, 400, manager);
        manager.music.playLevelMusic(1);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Прошарок плиток 1");
        player.update(delta, collisionLayer, 1120);

        renderer.setView(camera);
        renderer.render();

        float cameraX = MathUtils.clamp(player.bounds.getX(), 300f, 1120f - 300f);

        camera.position.set(cameraX, 128f, 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw(batch);
        batch.end();
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
    }
}
