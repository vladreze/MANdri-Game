package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mandri.entities.*;
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

    private Texture bgTexture;
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
    private ShaderProgram shadowShader;

    private int displayedLives = 3;
    private final float HEART_DELAY = .75f;
    private float heartTimer = 0f;

    private Input input;

    private float HEART_ANIMATION_DELAY = 5f;
    private float heartAnimationTimer = 0f;

    private final float playerCameraWidth = 320f;
    private final float playerCameraHeight = 180f;

    private final float hudCameraWidth = 640f;
    private final float hudCameraHeight = 360f;

    private float mapWidth;
    private float mapHeight;

    private float transitionAlpha = 1f;

    private boolean isFadingIn = true;
    private boolean isFadingOut = false;

    private boolean isPaused = false;
    private Table pauseTable;
    private Texture dimBackground;
    private boolean isInitialized = false;
    private Texture pauseIcon;

    private InventoryLogic inventory;
    private Table hotbarTable;
    private Table mainInventoryTable;
    private boolean isInventoryOpen = false;
    private TextureRegionDrawable emptySlotDrawable;

    public PlayScreen(Main game, MainAssetsManager manager) {
        this.game = game;
        this.manager = manager;
    }

    @Override
    public void show() {
        if (!isInitialized) {
            batch = new SpriteBatch();

            camera = new OrthographicCamera();
            camera.setToOrtho(false, playerCameraWidth, playerCameraHeight);

            hudCamera = new OrthographicCamera();
            hudCamera.setToOrtho(false, hudCameraWidth, hudCameraHeight);

            bgTexture = new Texture(Gdx.files.internal("assets/maps/spaceMap/bgSpace.png"));
            bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

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
            rocketParts = new Array<Item>();
            MapLayer spawnLayer = map.getLayers().get("spawns");
            if (spawnLayer != null) {
                for (MapObject object : spawnLayer.getObjects()) {
                    String type = object.getProperties().get("type", String.class);
                    String className = object.getProperties().get("class", String.class);

                    Float x = object.getProperties().get("x", Float.class);
                    Float y = object.getProperties().get("y", Float.class);

                    if (x != null && y != null) {
                        if ("Mob".equals(type) || "Mob".equals(className)) {
                            enemies.add(new Enemy(x, y, manager));
                        } else if ("RocketPart1".equals(type) || "RocketPart2".equals(type) || "RocketPart3".equals(type)) {
                            rocketParts.add(new Item(manager, type, x, y));
                        } else if ("LevelExit".equals(type) || "LevelExit".equals(className)) {
                            rocket = new Rocket(x, y, manager);
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

            shadowShader = new ShaderProgram(
                Gdx.files.internal("shaders/default.vsh"),
                Gdx.files.internal("shaders/shadow.fsh")
            );

            shadowShader.pedantic = false;

            if  (!shadowShader.isCompiled()) {
                Gdx.app.log("Shader Error", shadowShader.getLog());
                Gdx.app.exit();
            }

            renderer.getBatch().setShader(vignetteShader);

            hudStage = new Stage(new com.badlogic.gdx.utils.viewport.FitViewport(hudCameraWidth, hudCameraHeight));

            Skin skin = UIManager.getInstance().getSkin();

            BitmapFont fontPauseText = FontCreator.generateTextFont(24, 1f);
            Label.LabelStyle textPauseStyle = new Label.LabelStyle();
            textPauseStyle.font = fontPauseText;

            Label pauseButton = new Label("||", textPauseStyle);
            pauseButton.setSize(30, 30);
            pauseButton.setPosition(hudCameraWidth - 40, hudCameraHeight - 40);
            ButtonActions.pauseScreen(pauseButton, this);
            ButtonActions.addHover(pauseButton);
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

            inventory = new InventoryLogic();

            Pixmap slotPixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
            slotPixmap.setColor(0.1f, 0.1f, 0.1f, 0.6f);
            slotPixmap.fill();
            slotPixmap.setColor(0.6f, 0.6f, 0.6f, 1f);
            slotPixmap.drawRectangle(0, 0, 40, 40);
            emptySlotDrawable = new TextureRegionDrawable(new Texture(slotPixmap));
            slotPixmap.dispose();

            hotbarTable = new Table();
            hotbarTable.setFillParent(true);
            hotbarTable.bottom().padBottom(15);

            for (int i = 0; i < 4; i++) {
                ImageButton.ImageButtonStyle slotStyle = new ImageButton.ImageButtonStyle();
//                Button.ButtonStyle slotStyle = new Button.ButtonStyle();
                slotStyle.up = emptySlotDrawable;

//                Button button = new Button(slotStyle);

                ImageButton button = new ImageButton(slotStyle);
                hotbarTable.add(button).width(40).height(40).pad(2);
            }

            mainInventoryTable = new Table();
            mainInventoryTable.setFillParent(true);
            mainInventoryTable.center();
            mainInventoryTable.setVisible(false);

            for (int i = 0; i < 16; i++) {
                if (i > 0 && i % 4 == 0) mainInventoryTable.row();

                ImageButton.ImageButtonStyle slotStyle = new ImageButton.ImageButtonStyle();
//                Button.ButtonStyle slotStyle = new Button.ButtonStyle();
                slotStyle.up = emptySlotDrawable;

//                Button button = new Button(slotStyle);

                ImageButton button = new ImageButton(slotStyle);
                mainInventoryTable.add(button).width(40).height(40).pad(2);
            }

            hudStage.addActor(hotbarTable);
            hudStage.addActor(mainInventoryTable);

            hudStage.addActor(pauseTable);



            isInitialized = true;
        }

        Gdx.input.setInputProcessor(hudStage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            if (isPaused) resumeGame();
            else pauseGame();
        }

        if(!isPaused) {
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

            switch (player.liveCount) {
                case 3:
                    HEART_ANIMATION_DELAY = 5f;
                    break;
                case 2:
                    HEART_ANIMATION_DELAY = 2.5f;
                    break;
                case 1:
                    HEART_ANIMATION_DELAY = .7f;
                    break;
                default:
                    HEART_ANIMATION_DELAY = 5f;
                    break;
            }

            if (heartTimer == 0) {
                heartAnimationTimer += delta;
                if (heartAnimationTimer >= HEART_ANIMATION_DELAY) {
                    heartAnimationTimer = 0f;
                }
            } else {
                heartAnimationTimer = 0f;
            }

            TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("ground");
            MapLayer objectLayer = (MapLayer) map.getLayers().get("collisions");
            player.update(delta, collisionLayer, objectLayer, 2720f);

            float lookAheadOffset = player.isRunningRight() ? 45f : -45f;

            float desiredX = player.bounds.getX() + lookAheadOffset;
            float desiredY = player.bounds.getY();

            float targetX = MathUtils.clamp(desiredX, (playerCameraWidth / 2), mapWidth - (playerCameraWidth / 2));
            float targetY = MathUtils.clamp(desiredY, (playerCameraHeight / 2), mapHeight);

            float currentCameraX = camera.position.x;
            float currentCameraY = camera.position.y;

            float alpha = 3.5f * delta;

            float smoothCameraX = MathUtils.lerp(currentCameraX, targetX, alpha);
            float smoothCameraY = MathUtils.lerp(currentCameraY, targetY, alpha);

            camera.update();

            for(int i = enemies.size - 1; i >= 0; i--) {
                Enemy e = enemies.get(i);

                if (e.isDead && e.deathTimer >= e.DEATH_TIME) {
                    enemies.removeIndex(i);
                    continue;
                }

                e.update(delta, collisionLayer);

                if (player.bounds.overlaps(e.bounds) && !e.isDead) {
                    if (player.currentState == Player.State.FALLING && player.bounds.y > e.bounds.y) {
                        manager.music.playBonusSound();
                        player.bounce();
                        e.isDead = true;
                        e.velocityY = player.JUMP_FORCE * 1.05f;
                        e.currentState = Enemy.State.DEAD;
                    }
                   else player.takeDamage("mob");
                }
            }

            if(rocket!=null){
                rocket.update(delta);
            }

            for (int i = rocketParts.size - 1; i >= 0; i--) {
                Item part = rocketParts.get(i);
                if (player.bounds.overlaps(part.bounds)) {
                    boolean added = inventory.addItem(part);
                    manager.music.playBigBonusSound();
                    if (added){
                        rocketParts.removeIndex(i);
                        updateInventoryUI();
                    }
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                isInventoryOpen = !isInventoryOpen;
                mainInventoryTable.setVisible(isInventoryOpen);
                hotbarTable.setVisible(!isInventoryOpen);
                player.isInventoryOpen = isInventoryOpen;
                if (isInventoryOpen) {
                    updateInventoryUI();
                }
                else isPaused = false;
            }

            camera.position.set(smoothCameraX, smoothCameraY, 0);

            if (player.isShaking()) {
                float shakePower = .5f;

                camera.position.x += MathUtils.random(-shakePower, shakePower);
                camera.position.y += MathUtils.random(-shakePower, shakePower);
            }

            camera.update();
        }
        renderer.setView(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float parallaxSpeed = 0.2f;
        float bgU = (camera.position.x * parallaxSpeed) / bgTexture.getWidth();
        float bgHeight = playerCameraHeight;
        float bgWidth = playerCameraWidth;
        float backgroundStartX = camera.position.x - (playerCameraWidth / 2);
        float backgroundStartY = camera.position.y - (playerCameraHeight / 2);

        batch.draw(bgTexture,
            backgroundStartX, backgroundStartY, bgWidth, bgHeight,
            bgU, 0, bgU + (bgWidth / bgTexture.getWidth()), 1);

        batch.end();

        vignetteShader.bind();
        vignetteShader.setUniformf("u_resolution", Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null);
        batch.begin();

        float shadowOffset = 2.0f;
        batch.setShader(shadowShader);

        if (rocket != null) {
            Matrix4 originalTransform = batch.getTransformMatrix().cpy();
            batch.setTransformMatrix(originalTransform.cpy().translate(shadowOffset, -shadowOffset, 0));

            rocket.draw(batch);

            for (Item part : rocketParts) {
                part.draw(batch, manager);
            }
            batch.setTransformMatrix(originalTransform);
        }
        for (Enemy e : enemies) {
            e.drawShadow(batch, shadowOffset, -shadowOffset);
        }
        player.drawShadow(batch, shadowOffset, -shadowOffset);
        batch.setShader(null);

        if (rocket != null) {
            rocket.draw(batch);
        }
        for (Item part : rocketParts) {
            part.draw(batch, manager);
        }
        for (Enemy e : enemies) {
            e.draw(batch);
        }

        player.draw(batch);
        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.setShader(null);
        batch.begin();

        float startX = 20f;
        final float startY = hudCameraHeight-40f;

        float spacing = 6f;

        int maxLives = 3;

        float baseSize = 24f;
        float currentHeartSize = baseSize;

        if (heartAnimationTimer < 0.2f) {
            float popFactor = MathUtils.sin(heartAnimationTimer * (MathUtils.PI / 0.2f));
            currentHeartSize = baseSize + (6f * popFactor);
        }

        for (int i = 0; i < maxLives; i++) {
            float currentX = startX + i * (baseSize + spacing);
            float drawX = currentX - (currentHeartSize - baseSize) / 2f;
            float drawY = startY - (currentHeartSize - baseSize) / 2f;

            if (i < player.liveCount) {
                batch.draw(manager.image.fullHeart, drawX, drawY, currentHeartSize, currentHeartSize);
            } else if (i < displayedLives) {
                batch.draw(manager.image.poisonHeart, drawX, drawY, currentHeartSize, currentHeartSize);
            } else {
                batch.draw(manager.image.emptyHeart, drawX, drawY, baseSize, baseSize);
            }
        }

        batch.end();

        if (isInventoryOpen) {
            Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
            batch.begin();
            batch.draw(dimBackground, 0, 0, hudCameraWidth, hudCameraHeight);
            batch.end();
        }

        hudStage.act(delta);
        hudStage.draw();

        if (player.isDead() && !isFadingOut) {
            manager.music.playHurtSound(3);
            isFadingOut = true;
            manager.getMusic().stopMusic();
            manager.music.stopMusic();
        }

        if (isFadingIn) {
            transitionAlpha -= delta * .5f;
            if (transitionAlpha <= 0) {
                transitionAlpha = 0;
                isFadingIn = false;
            }
        } else if (isFadingOut) {
            transitionAlpha += delta * .5f;
            if (transitionAlpha >= 1f) {
                transitionAlpha = 1f;
                isFadingOut = false;
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }

        if (transitionAlpha > 0) {
            Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
            batch.begin();
            batch.setColor(0, 0, 0, transitionAlpha);
            batch.draw(manager.image.whitePixel, 0, 0, hudCameraWidth, hudCameraHeight);
            batch.setColor(1, 1, 1, 1);
            batch.end();
        }
    }

    @Override
    public void resize (int width, int height) {
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

    private void updateInventoryUI() {
        Item[] items = inventory.getSlots();
        for (int i = 0; i < 4; i++) {
            ImageButton button = (ImageButton) hotbarTable.getCells().get(i).getActor();
            Item item = items[i];
            if (item != null) {
                TextureRegionDrawable drawable = new TextureRegionDrawable(getTextureForItem(item));
                drawable.setMinWidth(32);
                drawable.setMinHeight(32);
                button.getStyle().imageUp = drawable;
            } else button.getStyle().imageUp = emptySlotDrawable;
        }
        for (int i = 0; i < 16; i++) {
            ImageButton button = (ImageButton) mainInventoryTable.getCells().get(i).getActor();
            Item item = items[i];
            if (item != null) {
                TextureRegionDrawable drawable = new TextureRegionDrawable(getTextureForItem(item));
                drawable.setMinWidth(32);
                drawable.setMinHeight(32);
                button.getStyle().imageUp = drawable;
            } else {
                button.getStyle().imageUp = emptySlotDrawable;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        map.dispose();
        renderer.dispose();
        vignetteShader.dispose();
        manager.disposeAll();
        player.dispose();
        bgTexture.dispose();
        if (dimBackground != null) dimBackground.dispose();
        if (pauseIcon != null) pauseIcon.dispose();
    }

    public void pauseGame() {
        isPaused = true;
        pauseTable.setVisible(true);
    }

    public void resumeGame() {
        isPaused = false;
        pauseTable.setVisible(false);
    }

    private TextureRegion getTextureForItem(Item item) {
        if (item.getName().equals("RocketPart1")) return manager.image.rocketPart1;
        if (item.getName().equals("RocketPart2")) return manager.image.rocketPart2;
        if (item.getName().equals("RocketPart3")) return manager.image.rocketPart3;
        return manager.image.whitePixel;
    }
}
