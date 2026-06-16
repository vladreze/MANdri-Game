package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
            monster=new  Monster(x, y, manager);
        }
        else if ("stalactite".equalsIgnoreCase(type) || "emerald".equalsIgnoreCase(type) ||
            "geyser".equalsIgnoreCase(type) || type.toLowerCase().contains("numb") || type.toLowerCase().contains("number")) {
            caveItems.add(new Item(manager, type, x, y));
        }
        else if ("LevelExit".equals(type) || "pitExit".equals(type)||"exitWater".equals(type)) {
        }
    }

    @Override
    protected void updateLevelSpecifics(float delta, TiledMapTileLayer collisionLayer) {
        for (int i = 0; i < caveItems.size; i++) {
            Item item = caveItems.get(i);
            item.update(delta, player.bounds.x);
            if (player.bounds.overlaps(item.bounds)) {
                if ("emerald".equals(item.getName())||"numb1".equals(item.getName())||"numb3".equals(item.getName())||"numb5".equals(item.getName())||"numb0".equals(item.getName())) {
                    item.collect();
                    boolean added = inventory.addItem(item);
                    if (added) {
                        manager.music.playBonusSound();

                        pickupEffect.setPosition(item.bounds.x + item.bounds.width / 2, item.bounds.y + item.bounds.height / 2);
                        pickupEffect.scaleEffect(.85f);
                        pickupEffect.start();

                        caveItems.removeIndex(i);
                        updateInventoryUI();
                    }
                }if ("geyser".equals(item.getName())) {
                    if (player.currentState == Player.State.FALLING && player.bounds.y > item.bounds.y) {
                        player.bounce();
                        player.velocityY = player.JUMP_FORCE;
                        item.playJumpEffect();
                        manager.music.playBonusSound();
                    }
                }
                if ("stalactite".equals(item.getName())) {
                    if (player.bounds.overlaps(item.bounds)) {
                        player.takeDamage("stalactite");
                        caveItems.removeIndex(i);
                    }
                }
            }
        }
        if(monster != null) {
            monster.update(delta);
            if (player.bounds.overlaps(monster.bounds) && monster.curState != Monster.State.HAPPY) {
                if (inventory.consumeItem("emerald")) {
                    monster.giveEmerald();
                    updateInventoryUI();
                    manager.music.playBonusSound();
                }
            }
        }

        pickupEffect.update(delta);
    }

    @Override
    protected void drawLevelSpecificShadows(float shadowOffset) {
        for (int i = 0; i < caveItems.size; i++) {
            caveItems.get(i).drawShadow(batch, manager);
        }
    }

    @Override
    protected void drawLevelSpecifics() {
        for (int i = 0; i < caveItems.size; i++) {
            caveItems.get(i).draw(batch, manager);
            }
        if (monster != null) {
            monster.draw(batch);
        }

        pickupEffect.draw(batch);
    }

    @Override
    protected void drawLevelSpecificUI() {

    }

    @Override
    protected String getLevelTheme() {
        String mapName = getMapPath();
        String[] parts = mapName.split("/");
        return parts[parts.length - 1].substring(0, parts[parts.length - 1].indexOf("."));
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

