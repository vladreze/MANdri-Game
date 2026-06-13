package com.mandri.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mandri.storage.MainAssetsManager;

public class Item {
    public boolean isCollected;
    protected String name;
    public float x, y;
    public float width = 25f;
    public float height = 25f;
    public Rectangle bounds;
    private final MainAssetsManager manager;

    public Item(MainAssetsManager manager, String name, float x, float y) {
        this.name=name;
        this.manager = manager;
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, width, height);
        this.isCollected = false;
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
            if(frame!=null){
                batch.draw(frame,x,y,width,height);
            }
        }
    }
}
