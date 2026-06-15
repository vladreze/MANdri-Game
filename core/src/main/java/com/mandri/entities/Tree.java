package com.mandri.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mandri.storage.MainAssetsManager;

public class Tree {
    public Rectangle bounds;
    public MapObject mapObject;
    private MainAssetsManager manager;
    public float x, y;
    public float width, height;
    public Tree(float x, float y, float width, float height, MapObject mapObject,MainAssetsManager manager) {
        this.bounds = new Rectangle(x, y, width, height);
        this.mapObject = mapObject;
        this.manager = manager;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void draw(SpriteBatch batch, MainAssetsManager manager) {
        batch.draw(this.manager.image.forestTree, x, y, width, height);
    }
}
