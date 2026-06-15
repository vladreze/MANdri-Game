package com.mandri.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mandri.storage.MainAssetsManager;

public class Acorn {
    public float x, y;
    public float width=16f;
    public float height=16f;
    public Rectangle bounds;
    private float velY;
    private boolean isFall=false;
    private MainAssetsManager manager;
    private String type;
    public Acorn(MainAssetsManager manager, float x, float y, String type) {
        this.manager = manager;
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, 16, 16);
        this.type = type;
    }
    public void update(float delta, float playerX) {
        if(!isFall&&Math.abs(playerX-x)<10f){
            isFall=true;
        }
        if(isFall){
            velY-=delta*90f;
            y+=velY*delta;
            bounds.setPosition(x, y);
        }
    }
    public void draw(SpriteBatch batch) {
        if("acorn".equals(type)) {
            batch.draw(manager.image.forestAcorn, x, y, width, height);
        }
        else{
            batch.draw(manager.image.caveStalactite, x, y, width, height);
        }
    }
}
