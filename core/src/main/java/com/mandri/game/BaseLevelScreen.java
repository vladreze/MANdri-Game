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
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mandri.entities.Enemy;
import com.mandri.entities.InventoryLogic;
import com.mandri.entities.Item;
import com.mandri.entities.Player;
import com.mandri.storage.MainAssetsManager;
import com.mandri.ui.ButtonActions;
import com.mandri.ui.FontCreator;

public abstract class BaseLevelScreen extends PlayScreen implements Screen {
    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected OrthographicCamera hudCamera;
    protected Stage hudStage;

    protected Texture bgTexture;
    protected Player player;
    protected Array<Enemy> enemies;

    protected TiledMap map;
    protected OrthogonalTiledMapRenderer renderer;

    protected final MainAssetsManager manager;
    protected final Main game;

    protected float playerStartX = 170f;
    protected float playerStartY = 105f;

    protected ShaderProgram vignetteShader;
    protected ShaderProgram shadowShader;

    protected int displayedLives = 3;
    protected final float HEART_DELAY = .75f;
    protected float heartTimer = 0f;
    protected float HEART_ANIMATION_DELAY = 5f;
    protected float heartAnimationTimer = 0f;

    protected final float playerCameraWidth = 320f;
    protected final float playerCameraHeight = 180f;
    protected final float hudCameraWidth = 640f;
    protected final float hudCameraHeight = 360f;

    protected float mapWidth;
    protected float mapHeight;

    protected float transitionAlpha = 1f;
    protected boolean isFadingIn = true;
    protected boolean isFadingOut = false;
    protected boolean isLevelFinished = false;

    protected boolean isPaused = false;
    protected Table pauseTable;
    protected Texture dimBackground;
    protected boolean isInitialized = false;

    protected InventoryLogic inventory;
    protected Table hotbarTable;
    protected Table mainInventoryTable;
    protected boolean isInventoryOpen = false;
    protected TextureRegionDrawable emptySlotDrawable;
    protected TextureRegionDrawable selectedSlotDrawable;

    protected float levelFinishTimer = 0f;

    public BaseLevelScreen(Main game, MainAssetsManager manager) {
        super(game, manager);
        this.game = game;
        this.manager = manager;
    }

    protected abstract String getMapPath();
    protected abstract String getBackgroundPath();
    protected abstract int getLevelMusicIndex();
    protected abstract void handleCustomSpawn(MapObject object, String type, float x, float y);
    protected abstract void updateLevelSpecifics(float delta, TiledMapTileLayer collisionLayer);
    protected abstract void drawLevelSpecifics();
    protected abstract void drawLevelSpecificUI();
    protected abstract void drawLevelSpecificShadows(float shadowOffset);

    @Override
    public void show() {
        if (!isInitialized) {
            batch = new SpriteBatch();

            camera = new OrthographicCamera();
            camera.setToOrtho(false, playerCameraWidth, playerCameraHeight);

            hudCamera = new OrthographicCamera();
            hudCamera.setToOrtho(false, hudCameraWidth, hudCameraHeight);

            bgTexture = new Texture(Gdx.files.internal(getBackgroundPath()));
            bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

            map = new TmxMapLoader().load(getMapPath());
            MapProperties properties = map.getProperties();

            int mapWidthInTiles = properties.get("width", Integer.class);
            int tilePixelWidth = properties.get("tilewidth", Integer.class);
            int mapHeightInTiles = properties.get("height", Integer.class);
            int tilePixelHeight = properties.get("tileheight", Integer.class);

            mapWidth = mapWidthInTiles * tilePixelWidth;
            mapHeight = mapHeightInTiles * tilePixelHeight;

            renderer = new OrthogonalTiledMapRenderer(map);
            player = new Player(playerStartX, playerStartY, manager, getLevelTheme());
            enemies = new Array<>();

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
                        } else {
                            handleCustomSpawn(object, type, x, y);
                        }
                    }
                }
            }

            manager.getMusic().setVolume(0f);
            manager.music.playLevelMusic(getLevelMusicIndex());
            manager.music.playLevelMusic(getLevelMusicIndex());
            initShaders();
            initUI();
            isInitialized = true;
        }
        Gdx.input.setInputProcessor(hudStage);
    }

    private void initShaders() {
        vignetteShader = new ShaderProgram(
            Gdx.files.internal("shaders/default.vsh"), Gdx.files.internal("shaders/vignette.fsh")
        );
        vignetteShader.pedantic = false;

        shadowShader = new ShaderProgram(
            Gdx.files.internal("shaders/default.vsh"), Gdx.files.internal("shaders/shadow.fsh")
        );
        shadowShader.pedantic = false;
        renderer.getBatch().setShader(vignetteShader);
    }

    private void initUI() {
        hudStage = new Stage(new com.badlogic.gdx.utils.viewport.FitViewport(hudCameraWidth, hudCameraHeight));

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

        Pixmap selectedPixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
        selectedPixmap.setColor(0.1f, 0.1f, 0.1f, 0.6f);
        selectedPixmap.fill();
        selectedPixmap.setColor(1f, 1f, 0f, 1f);
        selectedPixmap.drawRectangle(0, 0, 40, 40);
        selectedPixmap.drawRectangle(1, 1, 38, 38);
        selectedSlotDrawable = new TextureRegionDrawable(new Texture(selectedPixmap));
        selectedPixmap.dispose();

        hotbarTable = new Table();
        hotbarTable.setFillParent(true);
        hotbarTable.bottom().padBottom(15);
        for (int i = 0; i < 4; i++) {
            ImageButton.ImageButtonStyle slotStyle = new ImageButton.ImageButtonStyle();
            slotStyle.up = emptySlotDrawable;
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
            slotStyle.up = emptySlotDrawable;
            ImageButton button = new ImageButton(slotStyle);
            mainInventoryTable.add(button).width(40).height(40).pad(2);
        }

        hudStage.addActor(hotbarTable);
        hudStage.addActor(mainInventoryTable);
        hudStage.addActor(pauseTable);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            if (isPaused) resumeGame(); else pauseGame();
        }

        if(!isPaused) {
            updateTimers(delta);

            TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("ground");
            MapLayer objectLayer = (MapLayer) map.getLayers().get("collisions");
            player.update(delta, collisionLayer, objectLayer, mapWidth);

            for(int i = enemies.size - 1; i >= 0; i--) {
                Enemy e = enemies.get(i);
                if (e.isDead && e.deathTimer >= e.DEATH_TIME) {
                    enemies.removeIndex(i);
                    continue;
                }
                e.update(delta, collisionLayer, camera);
                if (player.bounds.overlaps(e.bounds) && !e.isDead) {
                    if (player.currentState == Player.State.FALLING && player.bounds.y > e.bounds.y) {
                        manager.music.playBonusSound();
                        player.bounce();
                        e.die();
                        e.velocityY = player.JUMP_FORCE / 1.5f;
                        manager.music.playPunchSound();
                    } else {
                        player.takeDamage("mob");
                    }
                }
            }

            updateLevelSpecifics(delta, collisionLayer);

            handleInventoryInput();

            updateCamera(delta);
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

        batch.draw(bgTexture, backgroundStartX, backgroundStartY, bgWidth, bgHeight,
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

        drawLevelSpecificShadows(shadowOffset);

        for (Enemy e : enemies) {
            e.drawShadow(batch, shadowOffset, -shadowOffset);
        }
        if (!isLevelFinished) {
            player.drawShadow(batch, shadowOffset, -shadowOffset);
        }

        batch.setShader(null);

        drawLevelSpecifics();
        for (Enemy e : enemies) { e.draw(batch); }
        if (!isLevelFinished) { player.draw(batch); }

        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.setShader(null);
        batch.begin();
        drawHearts();
        drawLevelSpecificUI();
        batch.setColor(1f, 1f, 1f, 1f);
        batch.end();

        if (isInventoryOpen) {
            Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
            batch.begin();
            batch.draw(dimBackground, 0, 0, hudCameraWidth, hudCameraHeight);
            batch.end();
        }

        hudStage.act(delta);
        hudStage.draw();

        handleTransitions(delta);
    }

    protected void updateCamera(float delta) {
        float lookAheadOffset = player.isRunningRight() ? 45f : -45f;
        float desiredX = player.bounds.getX() + lookAheadOffset;
        float desiredY = player.bounds.getY();

        float targetX = MathUtils.clamp(desiredX, (playerCameraWidth / 2), mapWidth - (playerCameraWidth / 2));
        float targetY = MathUtils.clamp(desiredY, (playerCameraHeight / 2), mapHeight);

        float alpha = 3.5f * delta;
        float smoothCameraX = MathUtils.lerp(camera.position.x, targetX, alpha);
        float smoothCameraY = MathUtils.lerp(camera.position.y, targetY, alpha);

        camera.position.set(smoothCameraX, smoothCameraY, 0);

        if (player.isShaking()) {
            float shakePower = .5f;
            camera.position.x += MathUtils.random(-shakePower, shakePower);
            camera.position.y += MathUtils.random(-shakePower, shakePower);
        }
        camera.update();
    }

    private void updateTimers(float delta) {
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

        HEART_ANIMATION_DELAY = (player.liveCount == 3) ? 5f : (player.liveCount == 2) ? 2.5f : .7f;

        if (heartTimer == 0) {
            heartAnimationTimer += delta;
            if (heartAnimationTimer >= HEART_ANIMATION_DELAY) heartAnimationTimer = 0f;
        } else {
            heartAnimationTimer = 0f;
        }
    }

    private void drawHearts() {
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

            if (i < player.liveCount) batch.draw(manager.image.fullHeart, drawX, drawY, currentHeartSize, currentHeartSize);
            else if (i < displayedLives) batch.draw(manager.image.poisonHeart, drawX, drawY, currentHeartSize, currentHeartSize);
            else batch.draw(manager.image.emptyHeart, drawX, drawY, baseSize, baseSize);
        }
    }

    private void handleInventoryInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            isInventoryOpen = !isInventoryOpen;
            mainInventoryTable.setVisible(isInventoryOpen);
            hotbarTable.setVisible(!isInventoryOpen);
            player.isInventoryOpen = isInventoryOpen;
            manager.music.playGrabSound();
            if (isInventoryOpen) updateInventoryUI();
            else isPaused = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) { inventory.selectColumn(0); updateInventoryUI(); }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) { inventory.selectColumn(1); updateInventoryUI(); }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) { inventory.selectColumn(2); updateInventoryUI(); }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) { inventory.selectColumn(3); updateInventoryUI(); }

        if (isInventoryOpen) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) { inventory.nextRow(); updateInventoryUI(); }
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) { inventory.prevRow(); updateInventoryUI(); }
        }
    }

    private void handleTransitions(float delta) {
        if (player.isDead() && !isFadingOut) {
            manager.music.playHurtSound(3);
            isFadingOut = true;
            manager.getMusic().stopMusic();
        }

        if (isFadingIn) {
            transitionAlpha -= delta * .5f;
            float currentVol = manager.getMusic().getVolume();

            if (currentVol < 1f) {
                manager.getMusic().setVolume(Math.min(1f, currentVol + 0.35f * delta));
            }

            if (transitionAlpha <= 0) {
                transitionAlpha = 0;
                isFadingIn = false;
            }
        } else if (isFadingOut) {
            transitionAlpha += delta * .5f;
            if (transitionAlpha >= 1f) {
                game.setScreen(getRestartScreen());
                manager.music.playGameOverSound();
                return;
            }
        } else if (isLevelFinished) {
            transitionAlpha += delta * .15f;
            levelFinishTimer += delta;
            float currentVol = manager.getMusic().getVolume();

            if (currentVol > 0f) {
                manager.getMusic().setVolume(Math.max(0f, currentVol - 0.1f * delta));
            }

            if (transitionAlpha >= 1f) {
                transitionAlpha = 1f;
                if (levelFinishTimer >= 3f) {
                    manager.getMusic().stopMusic();
                    game.setScreen(new ForestScreen(game, manager));
                }
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

    protected void updateInventoryUI() {
        Item[] items = inventory.getSlots();
        int activeRow = inventory.getActiveRow();
        int selectedCol = inventory.getSelectedCol();
        int globalSelectedIndex = inventory.getGlobalSelectedIndex();

        for (int i = 0; i < 4; i++) {
            ImageButton button = (ImageButton) hotbarTable.getCells().get(i).getActor();
            int itemIndex = (activeRow * 4) + i;
            updateSlotStyle(button, items[itemIndex], i == selectedCol);
        }
        for (int i = 0; i < 16; i++) {
            ImageButton button = (ImageButton) mainInventoryTable.getCells().get(i).getActor();
            updateSlotStyle(button, items[i], i == globalSelectedIndex);
        }
    }

    private void updateSlotStyle(ImageButton button, Item item, boolean isSelected) {
        if (item != null) {
            TextureRegionDrawable drawable = new TextureRegionDrawable(getTextureForItem(item));
            drawable.setMinWidth(32); drawable.setMinHeight(32);
            button.getStyle().imageUp = drawable;
        } else {
            button.getStyle().imageUp = emptySlotDrawable;
        }
        button.getStyle().up = isSelected ? selectedSlotDrawable : emptySlotDrawable;
    }

    protected TextureRegion getTextureForItem(Item item) {
        return getTextureForItemByName(item.getName());
    }

    protected TextureRegion getTextureForItemByName(String itemName) {
        if (itemName.equals("RocketPart1")) return manager.image.rocketPart1;
        if (itemName.equals("RocketPart2")) return manager.image.rocketPart2;
        if (itemName.equals("RocketPart3")) return manager.image.rocketPart3;
        return manager.image.whitePixel;
    }

    public void pauseGame() { isPaused = true; pauseTable.setVisible(true); }
    public void resumeGame() { isPaused = false; pauseTable.setVisible(false); }

    @Override public void resize(int w, int h) { camera.update(); if (hudStage != null) hudStage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    protected abstract String getLevelTheme();

    protected abstract Screen getRestartScreen();

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
    }
}
