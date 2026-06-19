package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mandri.entities.Enemy;
import com.mandri.entities.Item;
import com.mandri.entities.Monster;
import com.mandri.entities.Player;
import com.mandri.storage.CutsceneManager;
import com.mandri.storage.MainAssetsManager;

public class CaveScreen extends BaseLevelScreen {
    private Array<Item> caveItems;
    private ParticleEffect pickupEffect;

    private boolean isDoorOpened = false;
    private Rectangle door;
    private Rectangle tablePassword;
    private Rectangle levelExit;

    private Monster monster;
    private boolean allItemsAdded = false;
    private int itemCount = 0;
    private int insertedItemCount = 0;

    private Texture noiseTexture;

    private float PART_ANIMATION_DELAY = .2f;
    private float partAnimationTimer = 0f;
    private boolean isAnimating = false;
    private int animatingPartIndex = -1;

    public CaveScreen(Main game, MainAssetsManager manager) {
        super(game, manager);
    }

    @Override
    public void show() {
        if (!isInitialized) {
            caveItems = new Array<>();
            pickupEffect = new ParticleEffect();
            pickupEffect.load(
                Gdx.files.internal("assets/particles/collectItem.p"),
                Gdx.files.internal("assets/particles/")
            );

            Pixmap pixmap = new Pixmap(320, 180, Pixmap.Format.RGBA8888);
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
        }

        super.show();

        MapLayer collisionLayer = map.getLayers().get("collisions");
        if (collisionLayer != null) {
            for (MapObject object : collisionLayer.getObjects()) {
                String type = object.getProperties().get("type", String.class);
                if (type == null) continue;

                Float x = object.getProperties().get("x", Float.class);
                Float y = object.getProperties().get("y", Float.class);
                Float w = object.getProperties().get("width", Float.class);
                Float h = object.getProperties().get("height", Float.class);

                if ("gateExit".equals(type)) {
                    if (x != null && y != null) {
                        door = new Rectangle(x, y, w != null ? w : 64, h != null ? h : 128);
                    }
                } else if ("LevelExit".equals(type) || "exitWater".equals(type)) {
                    if (x != null && y != null) {
                        levelExit = new Rectangle(x, y, w != null ? w : 64, h != null ? h : 128);
                    }
                }
            }
        }

        if (player != null) {
            player.isJetpackEnabled = false;
        }
    }

    @Override
    protected String getMapPath() {
        return "assets/maps/caveMap/cave.tmx";
    }

    @Override
    protected String getBackgroundPath() {
        return "assets/maps/caveMap/bgCave.png";
    }

    @Override
    protected int getLevelMusicIndex() {
        return 3;
    }

    @Override
    protected void handleCustomSpawn(MapObject object, String type, float x, float y) {
        if (type == null) {
            type = "";
        }

        if ("spider".equals(type)) {
            enemies.add(new Enemy(x, y, manager, type));
        }
        else if ("monster".equals(type)) {
            monster = new Monster(x, y, manager);
        }
        else if ("stalactite".equalsIgnoreCase(type) || "emerald".equalsIgnoreCase(type) ||
            "geyser".equalsIgnoreCase(type) || type.toLowerCase().contains("numb") ||
            type.toLowerCase().contains("number")) {
            caveItems.add(new Item(manager, type, x, y));
        }
        else if ("gateExit".equals(type)) {
            Float w = object.getProperties().get("width", Float.class);
            Float h = object.getProperties().get("height", Float.class);
            door = new Rectangle(x, y, w != null ? w : 64, h != null ? h : 128);
        }
        else if ("LevelExit".equals(type) || "pitExit".equals(type) || "exitWater".equals(type)) {
            Float w = object.getProperties().get("width", Float.class);
            Float h = object.getProperties().get("height", Float.class);
            levelExit = new Rectangle(x, y, w != null ? w : 64, h != null ? h : 128);
        }
        else if ("table".equalsIgnoreCase(type)) {
            Float w = object.getProperties().get("width", Float.class);
            Float h = object.getProperties().get("height", Float.class);
            tablePassword = new Rectangle(x, y, w != null ? w : 32, h != null ? h : 16);
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

        for (int i = 0; i < caveItems.size; i++) {
            Item item = caveItems.get(i);
            item.update(delta, player.bounds.x);
            String itemName = item.getName();

//            if (!allItemsAdded) {
//                if (itemCount == 4) allItemsAdded = true;
//                if ("numb1".equals(itemName) || "numb3".equals(itemName) ||
//                    "numb5".equals(itemName) || "numb0".equals(itemName)) {
//                    boolean added = inventory.addItem(item);
//                    updateInventoryUI();
//                    itemCount++;
//                    if (added) {
//                        manager.music.playBonusSound();
//                    }
//                }
//            }

            if (player.bounds.overlaps(item.bounds)) {
                if ("emerald".equals(itemName) || "numb1".equals(itemName) || "numb3".equals(itemName) ||
                    "numb5".equals(itemName) || "numb0".equals(itemName)) {
                    item.collect();
                    boolean added = inventory.addItem(item);
                    if (added) {
                        if (itemName.contains("numb")) isAnimating = true;

                        animatingPartIndex = switch (itemName) {
                            case "numb5" -> 0;
                            case "numb3" -> 1;
                            case "numb0" -> 3;
                            case "numb1" -> 2;
                            default -> -1;
                        };

                        manager.music.playBonusSound();

                        pickupEffect.setPosition(item.bounds.x + item.bounds.width / 2, item.bounds.y + item.bounds.height / 2);
                        pickupEffect.scaleEffect(.85f);
                        pickupEffect.start();

                        caveItems.removeIndex(i);
                        updateInventoryUI();
                        i--;
                        continue;
                    }
                }

                if ("geyser".equals(itemName)) {
                    if (player.currentState == Player.State.FALLING && player.bounds.y > item.bounds.y) {
                        player.bounce();
                        player.velocityY = player.JUMP_FORCE * 1.2f;
                        item.playJumpEffect("geyser");
                        manager.music.playBonusSound();
                    }
                }

                if ("stalactite".equals(itemName)) {
                    if (player.bounds.overlaps(item.bounds)) {
                        player.takeDamage("stalactite");
                        caveItems.removeIndex(i);
                        i--;
                    }
                }
            }
        }

        if (monster != null) {
            monster.update(delta);
            if (player.bounds.overlaps(monster.bounds) && monster.curState != Monster.State.HAPPY) {
                if (inventory.consumeItem("emerald")) {
                    monster.giveEmerald();
                    updateInventoryUI();
                    manager.music.playBonusSound();
                }
                if(player.bounds.x < monster.x){
                    player.bounds.x = monster.x - player.bounds.width;
                }
                else{
                    player.bounds.x = monster.x + monster.width;
                }
            }
        }

        pickupEffect.update(delta);

        if (isDoorOpened && levelExit != null && player.bounds.overlaps(levelExit)) {
            isLevelFinished = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            Rectangle reachBounds = new Rectangle(player.bounds.x - 15, player.bounds.y, player.bounds.width + 30, player.bounds.height);

            boolean isNearTerminal = (tablePassword != null && reachBounds.overlaps(tablePassword)) ||
                (door != null && reachBounds.overlaps(door));

            if (isNearTerminal) {
                if (!isDoorOpened) {
                    boolean itemIsInserted = false;
                    Item selectedItem = inventory.getSelectedItem();

                    if (selectedItem != null) {
                        if (selectedItem.getName().contains("numb")) {
                            inventory.getSlots()[inventory.getGlobalSelectedIndex()] = null;
                            itemIsInserted = true;
                        }
                    }

                    if (itemIsInserted) {
                        insertedItemCount++;
                        updateInventoryUI();
                        manager.music.playBonusSound();

                        if (insertedItemCount == 4) {
                            manager.music.playBigBonusSound();
                            isDoorOpened = true;
                        }
                    } else if (insertedItemCount < 4) {
                        manager.music.playHurtSound(1);
                    }
                } else {
                    manager.music.playJumpSound();

                    if (door != null) {
                        int startX = (int) (door.x / 16);
                        int startY = (int) (door.y / 16);
                        int endX = (int) ((door.x + door.width - 1) / 16);
                        int endY = (int) ((door.y + door.height - 1) / 16);

                        for (MapLayer mapLayer : map.getLayers()) {
                            if (mapLayer instanceof TiledMapTileLayer) {
                                TiledMapTileLayer tileLayer = (TiledMapTileLayer) mapLayer;
                                for (int tx = startX; tx <= endX; tx++) {
                                    for (int ty = startY; ty <= endY; ty++) {
                                        tileLayer.setCell(tx, ty, null);
                                    }
                                }
                            }
                        }
                        door = null;
                    }
                    tablePassword = null;
                }
            }
        }
    }

    @Override
    protected void drawLevelSpecificShadows(float shadowOffset) {
        for (int i = 0; i < caveItems.size; i++) {
            caveItems.get(i).drawShadow(batch, manager);
        }
    }

    @Override
    protected void drawLevelSpecifics() {
        if (tablePassword != null) {
            if (!isDoorOpened) {
                batch.draw(manager.image.caveEmptyTable, tablePassword.x, tablePassword.y, tablePassword.width, tablePassword.height);
            } else {
                batch.draw(manager.image.caveDoneTable, tablePassword.x, tablePassword.y, tablePassword.width, tablePassword.height);
            }
        }

        for (int i = 0; i < caveItems.size; i++) {
            caveItems.get(i).draw(batch, manager);
        }

        if (monster != null) {
            monster.draw(batch);
        }

        if (!pickupEffect.isComplete()) {
            pickupEffect.draw(batch);
        }
    }

    @Override
    protected void drawLevelSpecificUI() {
        float cavePartX = 480f;
        float cavePartSize = 20f;
        float cavePartSpacing = 6f;
        float cavePartsStartY = hudCameraHeight - cavePartSize * 2;

        for (int i = 0; i < 4; i++) {
            float currentPartSize = cavePartSize;
            if (isAnimating && i == animatingPartIndex) {
                currentPartSize = cavePartSize + (14f * MathUtils.sin(partAnimationTimer * (MathUtils.PI / PART_ANIMATION_DELAY)));
            }

            float drawX = cavePartX + i * (cavePartSize + cavePartSpacing) - (currentPartSize - cavePartSize) / 2f;
            float drawY = cavePartsStartY - (currentPartSize - cavePartSize) / 2f;

            String partName = switch (i) {
                case 0 -> "numb5";
                case 1 -> "numb3";
                case 2 -> "numb0";
                case 3 -> "numb1";
                default -> "";
            };

            if (inventory.hasItem(partName)) {
                batch.setColor(1f, 1f, 1f, 1f);
            } else {
                batch.setColor(.5f, .5f, .5f, .9f);
            }

            TextureRegion tex = getTextureForItemByName(partName);
            if (tex != null) batch.draw(tex, drawX, drawY, currentPartSize, cavePartSize);
        }
    }

    @Override
    protected String getLevelTheme() {
        return "cave";
    }

    @Override
    protected Screen getRestartScreen() {
        return new CaveScreen(game, manager);
    }

    @Override
    protected Screen getNextScreen() {
        return new CutsceneManager(game, manager).cs4();
    }

    @Override
    protected Texture getNoiseTexture() {
        return noiseTexture;
    }
}
