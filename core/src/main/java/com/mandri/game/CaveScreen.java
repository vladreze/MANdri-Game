package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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

    private Monster monster;

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
        }

        super.show();

        MapLayer collisionLayer = map.getLayers().get("collisions");
        if (collisionLayer != null) {
            for (MapObject object : collisionLayer.getObjects()) {
                String type = object.getProperties().get("type", String.class);
                if (type == null) continue;

                if ("gateExit".equals(type) || "LevelExit".equals(type) || "exitWater".equals(type)) {
                    Float x = object.getProperties().get("x", Float.class);
                    Float y = object.getProperties().get("y", Float.class);
                    Float w = object.getProperties().get("width", Float.class);
                    Float h = object.getProperties().get("height", Float.class);
                    if (x != null && y != null) {
                        door = new Rectangle(x, y, w != null ? w : 64, h != null ? h : 128);
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
        else if ("gateExit".equals(type) || "LevelExit".equals(type) || "pitExit".equals(type) || "exitWater".equals(type)) {
            Float w = object.getProperties().get("width", Float.class);
            Float h = object.getProperties().get("height", Float.class);
            door = new Rectangle(x, y, w != null ? w : 64, h != null ? h : 128);
        }
        else if ("Table".equalsIgnoreCase(type)) {
            Float w = object.getProperties().get("width", Float.class);
            Float h = object.getProperties().get("height", Float.class);
            tablePassword = new Rectangle(x, y, w != null ? w : 32, h != null ? h : 16);
        }
    }

    @Override
    protected void updateLevelSpecifics(float delta, TiledMapTileLayer collisionLayer) {
        for (int i = 0; i < caveItems.size; i++) {
            Item item = caveItems.get(i);
            item.update(delta, player.bounds.x);

            if (player.bounds.overlaps(item.bounds)) {
                String itemName = item.getName();

                if ("emerald".equals(itemName) || "numb1".equals(itemName) || "numb3".equals(itemName) ||
                    "numb5".equals(itemName) || "numb0".equals(itemName)) {
                    item.collect();
                    boolean added = inventory.addItem(item);
                    if (added) {
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
                        item.playJumpEffect();
                        manager.music.playBonusSound();
                    }
                }

                if ("stalactite".equals(itemName)) {
                    if (player.bounds.overlaps(item.bounds)) {
                        player.takeDamage("stalactite");
                        caveItems.removeIndex(i);
                        i--;
                        continue;
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            Rectangle reachBounds = new Rectangle(player.bounds.x - 15, player.bounds.y, player.bounds.width + 30, player.bounds.height);

            boolean isNearTerminal = (tablePassword != null && reachBounds.overlaps(tablePassword)) ||
                (door != null && reachBounds.overlaps(door));

            if (isNearTerminal) {
                if (!isDoorOpened) {
                    if (inventory.hasItem("numb0") && inventory.hasItem("numb1") &&
                        inventory.hasItem("numb3") && inventory.hasItem("numb5")) {

                        inventory.consumeItem("numb0");
                        inventory.consumeItem("numb1");
                        inventory.consumeItem("numb3");
                        inventory.consumeItem("numb5");

                        manager.music.playBigBonusSound();
                        isDoorOpened = true;
                        isLevelFinished = true;
                        updateInventoryUI();
                    }
                } else {
                    manager.music.playJumpSound();
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
        return null;
    }
}
