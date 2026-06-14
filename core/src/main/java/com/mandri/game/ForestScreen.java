package com.mandri.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mandri.storage.MainAssetsManager;

public class ForestScreen extends BaseLevelScreen {

    public ForestScreen(Main game, MainAssetsManager manager) {
        super(game, manager);
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
        // if ("bee".equals(type)) { ... }
        // else if ("acorn".equals(type)) { ... }
    }

    @Override
    protected void updateLevelSpecifics(float delta, TiledMapTileLayer collisionLayer) {

    }

    @Override
    protected void drawLevelSpecificShadows(float shadowOffset) {

    }

    @Override
    protected void drawLevelSpecifics() {

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
}
