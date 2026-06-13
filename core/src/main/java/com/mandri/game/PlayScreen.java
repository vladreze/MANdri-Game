package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mandri.entities.Enemy;
import com.mandri.entities.Item;
import com.mandri.entities.Player;
import com.mandri.entities.Rocket;
import com.mandri.storage.MainAssetsManager;
import com.mandri.storage.UIManager;
import com.mandri.ui.ButtonActions;
import com.mandri.ui.PixelButton;

public class PlayScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Stage hudStage;

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

    private float playerStartX = 170f;
    private float playerStartY = 105f;

    private ShaderProgram vignetteShader;

    private int displayedLives = 3;
    private final float HEART_DELAY = 0.5f;
    private float heartTimer = 0f;

    private final float playerCameraWidth = 320f;
    private final float playerCameraHeight = 180f;

    private final float hudCameraWidth = 640f;
    private final float hudCameraHeight = 360f;

    private float mapWidth;
    private float mapHeight;

    public PlayScreen(Main game ,MainAssetsManager manager){
        this.game = game;
        this.manager = manager;
    }
    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, playerCameraWidth, playerCameraHeight);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, hudCameraWidth, hudCameraHeight);

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
        enemies = new Array<Enemy>();
        rocketParts=new Array<Item>();
        MapLayer spawnLayer = map.getLayers().get("spawns");
        if(spawnLayer!=null){
            for(MapObject object:spawnLayer.getObjects()){
                String type=object.getProperties().get("type", String.class);
                String className=object.getProperties().get("class", String.class);

                Float x=object.getProperties().get("x", Float.class);
                Float y=object.getProperties().get("y", Float.class);

                if(x!= null&&y!= null){
                    if("Mob".equals(type)||"Mob".equals(className)){
                        enemies.add(new Enemy(x,y,manager));
                    }
                    else if("RocketPart1".equals(type)||"RocketPart2".equals(type)||"RocketPart3".equals(type)){
                        rocketParts.add(new Item(manager, type, x, y));
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

        vignetteShader.pedantic = false;

        if (!vignetteShader.isCompiled()) {
            Gdx.app.log("Shader Error", vignetteShader.getLog());
            Gdx.app.exit();
        }

        renderer.getBatch().setShader(vignetteShader);

        hudStage = new Stage(new com.badlogic.gdx.utils.viewport.FitViewport(hudCameraWidth, hudCameraHeight));
        Gdx.input.setInputProcessor(hudStage);

        Skin skin = UIManager.getInstance().getSkin();
        PixelButton menuBtn = new PixelButton("MENU", skin);
        ButtonActions.openMainMenu(menuBtn, game);

        menuBtn.setSize(40, 40);

        menuBtn.setPosition(hudCameraWidth - 60, hudCameraHeight - 60);

        hudStage.addActor(menuBtn);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (player.liveCount < displayedLives) {
            heartTimer += delta;
            if (heartTimer >= HEART_DELAY) {
                displayedLives--;
                heartTimer = 0f;
            }
        } else {
            displayedLives = player.liveCount;
            heartTimer = 0f;
        }

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("ground");
        MapLayer objectLayer = (MapLayer) map.getLayers().get("collisions");
        player.update(delta, collisionLayer, objectLayer,2720f);

        float cameraX = MathUtils.clamp(player.bounds.getX(), (playerCameraWidth / 2), mapWidth - (playerCameraWidth / 2));
        float cameraY = MathUtils.clamp(player.bounds.getY(), (playerCameraHeight / 2), mapHeight);

        float currentCameraX = camera.position.x;
        float targetX = cameraX;

        float currentCameraY = camera.position.y;
        float targetY = cameraY;

        float alpha = 7.5f * delta;

        float smoothCameraX = MathUtils.lerp(currentCameraX, targetX, alpha);
        float smoothCameraY = MathUtils.lerp(currentCameraY, targetY, alpha);

        camera.position.set(smoothCameraX, smoothCameraY, 0);

        if (player.isShaking()) {
            float shakePower = .5f;

            camera.position.x += MathUtils.random(-shakePower, shakePower);
            camera.position.y += MathUtils.random(-shakePower, shakePower);
        }

        camera.update();

        for(Enemy  e:enemies){
            e.update(delta, collisionLayer);
        }
        if(rocket!=null){
            rocket.update(delta);
        }
        renderer.setView(camera);
        vignetteShader.bind();
        vignetteShader.setUniformf("u_resolution", Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null);
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
        final float startY = hudCameraHeight-40f;

        float heartWidth = 24f;
        float heartHeight = 24f;

        float spacing = 6f;

        int maxLives = 3;



        for (int i = 0; i < maxLives; i++) {
            float currentX = startX + i * (heartWidth + spacing);
            if (i < player.liveCount) {
                batch.draw(manager.image.fullHeart, currentX, startY, heartWidth, heartHeight);
            } else if (i < displayedLives) {
                batch.draw(manager.image.poisonHeart, currentX, startY, heartWidth, heartHeight);
            } else {
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
        player.dispose();
    }
}
