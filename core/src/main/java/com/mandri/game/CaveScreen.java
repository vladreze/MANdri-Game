package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.mandri.entities.Enemy;
import com.mandri.entities.Item;
import com.mandri.entities.Player;
import com.mandri.storage.MainAssetsManager;

public class CaveScreen extends BaseLevelScreen {
    private Array<Item> caveItems;

    public CaveScreen(Main game, MainAssetsManager manager) {
        super(game, manager);
    }

    @Override
    public void show() {
        if (!isInitialized) {
            caveItems = new Array<>();
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
        return null;
    }

    @Override
    protected Texture getNoiseTexture() {
        return null;
    }

    @Override
    protected void handleCustomSpawn(MapObject object, String type, float x, float y) {
        if ("spider".equals(type) || "monster".equals(type)) {
            enemies.add(new Enemy(x, y, manager, type));
        }
        else if ("acorn".equals(type) || "mushroom".equals(type) || "emerald".equals(type) ||
            "gateExit".equals(type) || (type != null && type.startsWith("numb"))) {
            caveItems.add(new Item(manager, type, x, y));
        }
    }

    @Override
    protected void updateLevelSpecifics(float delta, TiledMapTileLayer collisionLayer) {
        for (int i = caveItems.size - 1; i >= 0; i--) {
            Item item = caveItems.get(i);
            item.update(delta, player.bounds.x);
            if (player.bounds.overlaps(item.bounds)) {

                if ("mushroom".equals(item.getName())) {
                    if (player.currentState == Player.State.FALLING && player.bounds.y > item.bounds.y) {
                        player.bounce();
                        player.velocityY = player.JUMP_FORCE ;
                        item.playJumpEffect();
                    }
                }

                else if ("acorn".equals(item.getName())) {
                    player.takeDamage("acorn");
                    caveItems.removeIndex(i);
                }

                else if (item.getName() != null && (item.getName().startsWith("numb") || "emerald".equals(item.getName()))) {
                    item.collect();
                    if (inventory.addItem(item)) {
                        manager.music.playBonusSound();
                        caveItems.removeIndex(i);
                        updateInventoryUI();
                    }
                }

                else if ("gateExit".equals(item.getName())) {
                    isLevelFinished = true;
                }
            }
        }
    }

    @Override
    protected void drawLevelSpecificShadows(float shadowOffset) {
        for (Item item : caveItems) {
            item.drawShadow(batch, manager);
        }
    }

    @Override
    protected void drawLevelSpecifics() {
        for (Item item : caveItems) {
            item.draw(batch, manager);
        }
    }

    @Override
    protected void drawLevelSpecificUI() {

    }
}
