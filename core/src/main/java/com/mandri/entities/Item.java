package com.mandri.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mandri.storage.MainAssetsManager;

public class Item {
    public boolean isCollected = false;
    protected String name;
    public float x, y;
    public float width;
    public float height;
    public Rectangle bounds;
    private final MainAssetsManager manager;

    public Item(MainAssetsManager manager, String name, float x, float y) {
        this.name=name;
        this.manager = manager;
        this.x = x;
        this.y = y;
        this.isCollected = false;
        if (name.equals("acorn") || name.equals("mushroom") || name.equals("axe")) {
            this.width = 16f;
            this.height = 16f;
        }
        else {
            this.width = 25f;
            this.height = 25f;
        }
        this.bounds = new Rectangle(x, y, width, height);
    }
    public void collect(){
        isCollected = true;
    }
    public String getName(){
        return name;
    }
    public void draw(SpriteBatch batch, MainAssetsManager  manager) {
        if (!isCollected) {
            TextureRegion frame=null;
            if("RocketPart1".equals(name)){
                frame=manager.image.rocketPart1;
            }
            else if("RocketPart2".equals(name)){
                frame=manager.image.rocketPart2;
            }
            else if("RocketPart3".equals(name)){
                frame=manager.image.rocketPart3;
            }
            else if ("acorn".equals(name)){
                frame=manager.image.forestAcorn;
            }
            else if ("mushroom".equals(name)){
                frame=manager.image.forestMushroom;
            }
            else if ("axe".equals(name)){
                frame=manager.image.forestAxe;
            }
            if(frame!=null){
                batch.draw(frame,x,y,width,height);
            }
        }
    }

    public void drawShadow(SpriteBatch batch, MainAssetsManager manager) {
        if (!isCollected) {
            batch.setColor(0f, 0f, 0f, 0.4f);
            batch.draw(manager.image.whitePixel, x + 2, y - 2, width - 4, 3);
            batch.setColor(1f, 1f, 1f, 1f);
        }
    }
}
