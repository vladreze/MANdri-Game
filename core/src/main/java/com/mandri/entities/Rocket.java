package com.mandri.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mandri.storage.MainAssetsManager;

public class Rocket{
    public enum State{BROKEN, FIXED, FLYING}
    public State curState;
    public float x, y;
    public Rectangle bounds;
    private float velY=0;
    private final MainAssetsManager manager;
    public Rocket(float x, float y, MainAssetsManager manager){
        this.x=x;
        this.y=y;
        this.manager=manager;
        this.curState=State.BROKEN;
        this.bounds=new Rectangle(x,y,32,32);
    }
    public void update(float delta){
        if(curState==State.FLYING){
            velY+=100f*delta;
            y+=velY*delta;
        }
    }
    public void draw(SpriteBatch batch){
        TextureRegion frame;
        if(curState==State.BROKEN){
            frame=manager.image.rocketBroken;
        }
        else if(curState==State.FLYING){
            frame=manager.image.rocketFlying;
        }
        else{
            frame=manager.image.rocketFixed;
        }
        batch.draw(frame, x,y);
    }
}
