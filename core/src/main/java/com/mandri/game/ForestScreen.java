package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mandri.entities.Enemy;
import com.mandri.entities.Item;
import com.mandri.entities.Player;
import com.mandri.storage.CutsceneManager;
import com.mandri.storage.MainAssetsManager;

public class ForestScreen extends BaseLevelScreen {
    private Array<Item> forestItems;
    private ParticleEffect pickupEffect;

    public ForestScreen(Main game, MainAssetsManager manager) {
        super(game, manager);
    }

    @Override
    public void show() {
        if (!isInitialized) {
            forestItems = new Array<>();

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
        return "assets/maps/forestMap/forest.tmx";
    }

    @Override
    protected String getBackgroundPath() {
        return "assets/maps/forestMap/bgForest.png";
    }

    @Override
    protected int getLevelMusicIndex() {
        return 2;
    }

    @Override
    protected void handleCustomSpawn(MapObject object, String type, float x, float y) {
        if ("bee".equals(type) || "fox".equals(type) || "hive".equals(type) || "bat".equals(type)) {
            enemies.add(new Enemy(x, y, manager, type));
        }
        else if ("acorn".equals(type) || "axe".equals(type) || "mushroom".equals(type)) {
            forestItems.add(new Item(manager, type, x, y));
        }
        else if ("LevelExit".equals(type) || "pitExit".equals(type)) {
        }
    }

    @Override
    protected void updateLevelSpecifics(float delta, TiledMapTileLayer collisionLayer) {
        for (int i = 0; i < forestItems.size; i++) {
            Item item = forestItems.get(i);
            item.update(delta, player.bounds.x);
            if (player.bounds.overlaps(item.bounds)) {
                if ("axe".equals(item.getName())) {
                    item.collect();
                    boolean added = inventory.addItem(item);
                    if (added) {
                        manager.music.playBonusSound();

                        pickupEffect.setPosition(item.bounds.x + item.bounds.width / 2, item.bounds.y + item.bounds.height / 2);
                        pickupEffect.scaleEffect(.85f);
                        pickupEffect.start();

                        forestItems.removeIndex(i);
                        updateInventoryUI();
                    }
                }if ("mushroom".equals(item.getName())) {
                    if (player.currentState == Player.State.FALLING && player.bounds.y > item.bounds.y) {
                        player.bounce();
                        player.velocityY = player.JUMP_FORCE;
                        item.playJumpEffect();
                        manager.music.playBonusSound();
                    }
                }
                if ("acorn".equals(item.getName())) {
                    if (player.bounds.overlaps(item.bounds)) {
                        player.takeDamage("acorn");
                        forestItems.removeIndex(i);
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && inventory.hasItem("axe")) {
            MapLayer spawnLayer = map.getLayers().get("spawns");
            if (spawnLayer != null) {
                for (int i = spawnLayer.getObjects().getCount() - 1; i >= 0; i--) {
                    MapObject obj = spawnLayer.getObjects().get(i);
                    String type = obj.getProperties().get("type", String.class);

                    if ("tree".equals(type)) {
                        Float treeX = obj.getProperties().get("x", Float.class);
                        Float treeY = obj.getProperties().get("y", Float.class);

                        if (treeX != null && treeY != null) {
//                            boolean isFacingRight = player.isRunningRight();
//                            if (isFacingRight && treeX < player.bounds.x) continue;
//                            if (!isFacingRight && treeX > player.bounds.x) continue;

                            float distance = Math.abs((player.bounds.x + player.bounds.width / 2) - (treeX + 8));

                            if (distance < 30f && player.bounds.y >= treeY - 32f && player.bounds.y <= treeY + 128f) {
                                Integer health = obj.getProperties().get("health", Integer.class);
                                if (health == null) health = 2;

                                health--;
                                obj.getProperties().put("health", health);

                                int tileX = (int) (treeX / 16);
                                int baseTileY = (int) ((player.bounds.y + 4f) / 16);

                                if (health == 1) {
                                    for (MapLayer mapLayer : map.getLayers()) {
                                        if (mapLayer instanceof TiledMapTileLayer) {
                                            TiledMapTileLayer tileLayer = (TiledMapTileLayer) mapLayer;
                                            tileLayer.setCell(tileX, baseTileY + 1, null);
                                        }
                                    }
                                    manager.music.playCrackingBlockSound();
                                }
                                else if (health <= 0) {
                                    for (MapLayer mapLayer : map.getLayers()) {
                                        if (mapLayer instanceof TiledMapTileLayer) {
                                            TiledMapTileLayer tileLayer = (TiledMapTileLayer) mapLayer;
                                            for (int h = 0; h < 10; h++) {
                                                tileLayer.setCell(tileX, baseTileY + h, null);
                                            }
                                        }
                                    }
                                    manager.music.playCrackingBlockSound();

                                    inventory.consumeItem("axe");
                                    updateInventoryUI();
                                    spawnLayer.getObjects().remove(obj);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        pickupEffect.update(delta);
    }

    @Override
    protected void drawLevelSpecificShadows(float shadowOffset) {
        for (int i = 0; i < forestItems.size; i++) {
            forestItems.get(i).drawShadow(batch, manager);
        }
    }

    @Override
    protected void drawLevelSpecifics() {
        for (int i = 0; i < forestItems.size; i++) {
            forestItems.get(i).draw(batch, manager);
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
        return new ForestScreen(game, manager);
    }

    @Override
    protected Screen getNextScreen() {
       return new CutsceneManager(game, manager).cs3();
    }

    @Override
    protected Texture getNoiseTexture() {
        return null;
    }
}
