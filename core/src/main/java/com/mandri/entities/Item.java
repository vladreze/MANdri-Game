package com.mandri.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.*;
import com.mandri.storage.MainAssetsManager;

public class Item {
    public boolean isCollected;
    public Body body;
    protected World world;
    protected String name;

    public Item(World world, String name, float x, float y) {
        this.name=name;
        this.world = world;
        this.isCollected = false;
        physicsItem(x,y);
    }
    private void physicsItem(float x, float y) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(x, y);
        body = world.createBody(bd);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / 16f, 8 / 16f);
        FixtureDef fd = new FixtureDef(); //вага тертя форма
        fd.shape = shape;
        fd.isSensor = true;
        body.createFixture(fd).setUserData(this);
        shape.dispose();
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
                float drawX=(body.getPosition().x*16f)-8;
                float drawY=(body.getPosition().y*16f)-8;
                batch.draw(frame,drawX,drawY);
            }
        }
    }
}
