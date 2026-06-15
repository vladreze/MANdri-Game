package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.mandri.entities.Item;
import com.mandri.entities.Rocket;
import com.mandri.storage.MainAssetsManager;

public class SpaceScreen extends BaseLevelScreen {
    private Texture noiseTexture;
    private Array<Item> rocketParts;
    private Rocket rocket;
    private ParticleEffect pickupEffect;

    private float PART_ANIMATION_DELAY = .2f;
    private float partAnimationTimer = 0f;
    private boolean isAnimating = false;
    private int animatingPartIndex = -1;

    public SpaceScreen(Main game, MainAssetsManager manager) {
        super(game, manager);
    }

    @Override
    protected String getMapPath() {
        return "assets/maps/spaceMap/space.tmx";
    }

    @Override
    protected String getBackgroundPath() {
        return "assets/maps/spaceMap/bgSpace.png";
    }

    @Override
    protected int getLevelMusicIndex() {
        return 1;
    }

    @Override
    public void show() {
        rocketParts = new Array<>();
        pickupEffect = new ParticleEffect();
        pickupEffect.load(Gdx.files.internal("assets/particles/collectItem.p"), Gdx.files.internal("assets/particles/"));

        Pixmap pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                float noise = MathUtils.random();
                pixmap.setColor(noise, noise, noise, 1f);
                pixmap.drawPixel(x, y);
            }
        }
        noiseTexture = new Texture(pixmap);
        noiseTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        pixmap.dispose();

        super.show();
    }

    @Override
    protected void handleCustomSpawn(MapObject object, String type, float x, float y) {
        if ("RocketPart1".equals(type) || "RocketPart2".equals(type) || "RocketPart3".equals(type)) {
            rocketParts.add(new Item(manager, type, x, y));
        } else if ("LevelExit".equals(type) || "LevelExit".equals(object.getProperties().get("class", String.class))) {
            rocket = new Rocket(x, y, manager);
        }
    }

    @Override
    protected void updateLevelSpecifics(float delta, TiledMapTileLayer collisionLayer) {
        if (isAnimating) {
            partAnimationTimer += delta;
            if (partAnimationTimer > PART_ANIMATION_DELAY) {
                isAnimating = false; animatingPartIndex = -1; partAnimationTimer = 0f;
            }
        }

        if (rocket != null) {
            rocket.update(delta);
        }

        for (int i = 0; i < rocketParts.size; i++) {
            Item part = rocketParts.get(i);
            if (player.bounds.overlaps(part.bounds)) {
                if (inventory.addItem(part)) {
                    manager.music.playBigBonusSound();
                    pickupEffect.setPosition(part.bounds.x + part.bounds.width / 2, part.bounds.y + part.bounds.height / 2);
                    pickupEffect.scaleEffect(.85f);
                    pickupEffect.start();

                    switch (part.getName()) {
                        case "RocketPart1": animatingPartIndex = 0; break;
                        case "RocketPart2": animatingPartIndex = 1; break;
                        case "RocketPart3": animatingPartIndex = 2; break;
                    }
                    isAnimating = true; partAnimationTimer = 0f;
                    rocketParts.removeIndex(i);
                    updateInventoryUI();
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (rocket != null && player.bounds.overlaps(rocket.bounds)) {
                if (rocket.curState == Rocket.State.BROKEN) {
                    boolean partConsumed = false;
                    if (inventory.consumeItem("RocketPart1")) partConsumed = true;
                    else if (inventory.consumeItem("RocketPart2")) partConsumed = true;
                    else if (inventory.consumeItem("RocketPart3")) partConsumed = true;

                    if (partConsumed) {
                        rocket.insetPart(); updateInventoryUI(); manager.music.playBonusSound();
                    }
                } else if (rocket.curState == Rocket.State.FIXED) {
                    rocket.launchRocket();
                    manager.music.playRocketBreakSound();
                    isLevelFinished = true;
                }
            }
        }

        pickupEffect.update(delta);
    }

    @Override
    protected void updateCamera(float delta) {
        float desiredX, desiredY, targetY, alpha;

        if (rocket != null && rocket.isFlying()) {
            desiredX = rocket.x + rocket.width / 2f;
            desiredY = rocket.y + rocket.height / 2f;
            targetY = MathUtils.clamp(desiredY, (playerCameraHeight / 2), 10000f);
            alpha = 4.5f * delta;
        } else {
            float lookAheadOffset = player.isRunningRight() ? 45f : -45f;
            desiredX = player.bounds.getX() + lookAheadOffset;
            desiredY = player.bounds.getY();
            targetY = MathUtils.clamp(desiredY, (playerCameraHeight / 2), mapHeight);
            alpha = 3.5f * delta;
        }

        float targetX = MathUtils.clamp(desiredX, (playerCameraWidth / 2), mapWidth - (playerCameraWidth / 2));
        camera.position.set(MathUtils.lerp(camera.position.x, targetX, alpha), MathUtils.lerp(camera.position.y, targetY, alpha), 0);

        if (player.isShaking()) {
            camera.position.add(MathUtils.random(-0.5f, 0.5f), MathUtils.random(-0.5f, 0.5f), 0);
        }
        camera.update();
    }

    @Override
    protected void drawLevelSpecificShadows(float shadowOffset) {
        for  (int i = 0; i < rocketParts.size; i++) {
            rocketParts.get(i).drawShadow(batch, manager);
        }
    }

    @Override
    protected void drawLevelSpecifics() {
        if (rocket != null) rocket.draw(batch);
        for (Item part : rocketParts) part.draw(batch, manager);
        pickupEffect.draw(batch);
    }

    @Override
    protected void drawLevelSpecificUI() {
        float rocketPartsStartX = 500f;
        float rocketPartSize = 24f;
        float rocketPartSpacing = 6f;
        float rocketPartsStartY = (hudCameraHeight - 40f) + rocketPartSize / 2;

        for (int i = 0; i < 3; i++) {
            float currentPartSize = rocketPartSize;
            if (isAnimating && i == animatingPartIndex) {
                currentPartSize = rocketPartSize + (14f * MathUtils.sin(partAnimationTimer * (MathUtils.PI / PART_ANIMATION_DELAY)));
            }

            float drawX = (rocketPartsStartX + i * (rocketPartSize + rocketPartSpacing)) - currentPartSize / 2f;
            float drawY = rocketPartsStartY - currentPartSize / 2f;
            String partName = "RocketPart" + (i + 1);

            if (inventory.hasItem(partName) || (rocket != null && rocket.partsInserted > 0)) {
                batch.setColor(1f, 1f, 1f, 1f);
            } else {
                batch.setColor(.3f, .3f, .3f, .8f);
            }

            TextureRegion tex = getTextureForItemByName(partName);
            if (tex != null) batch.draw(tex, drawX, drawY, rocketPartSize, rocketPartSize);
        }
    }

    @Override
    protected String getLevelTheme() {
        String mapName = getMapPath();
        String[] parts = mapName.split("/");
        return parts[parts.length - 1].substring(0, parts[parts.length - 1].indexOf("."));
    }

    @Override
    protected Screen getRestartScreen() {
        return new SpaceScreen(game, manager);
    }

    @Override
    protected Texture getNoiseTexture() {
        return noiseTexture;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (pickupEffect != null) pickupEffect.dispose();
    }
}
