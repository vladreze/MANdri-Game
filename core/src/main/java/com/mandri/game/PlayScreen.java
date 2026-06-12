package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mandri.entities.Enemy;
import com.mandri.entities.Item;
import com.mandri.entities.Player;
import com.mandri.entities.Rocket;
import com.mandri.storage.MainAssetsManager;

public class PlayScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    private Texture background;
    private Player player;

    //Mobs Rocket Parts
    private Array<Enemy> enemies;
    private Array<Item> rocketParts;
    private Rocket rocket;
    private int partColl;
    private final int TOTAL_PARTS = 3;

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
        enemies = new Array<Enemy>();
        rocketParts=new Array<Item>();
        MapLayer spawnLayer = map.getLayers().get("spawns");
        if(spawnLayer!=null){
            for(MapObject object:spawnLayer.getObjects()){
                String name=object.getName();
                String type=object.getProperties().get("type", String.class);
                String className=object.getProperties().get("class", String.class);

                Float x=object.getProperties().get("x", Float.class);
                Float y=object.getProperties().get("y", Float.class);
                if(x!= null&&y!= null){
                    if("Mob".equals(type)||"Mob".equals(className)){
                        enemies.add(new Enemy(x,y,manager));
                    }
                    else if("RocketPart1".equals(name)||"RocketPart2".equals(name)||"RocketPart3".equals(name)){
                        rocketParts.add(new Item(null,name,x,y));
                    }
                    else if("LevelExit".equals(type)||"LevelExit".equals(className)){
                        rocket=new Rocket(x,y,manager);
                    }
                }
            }
        }
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

        for(Enemy  e:enemies){
            e.update(delta, collisionLayer);
        }
        if(rocket!=null){
            rocket.update(delta);
        }
        renderer.setView(camera);
        renderer.render();

        float cameraX = MathUtils.clamp(player.bounds.getX(), 300f, 2720f - 300f);

        camera.position.set(cameraX, 128f, 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.setShader(vignetteShader);
        batch.begin();

        if(rocket!=null){
            rocket.draw(batch);
        }
        for(Item part:rocketParts){
            part.draw(batch, manager);
        }
        for(Enemy  e:enemies){
            e.draw(batch);
        }

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
