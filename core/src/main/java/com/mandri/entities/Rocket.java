package com.mandri.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
    public float width = 64f;
    public float height = 100f;
    private final MainAssetsManager manager;

    private ParticleEffect flyingEffect;

    public int partsInserted = 0;
    public Rocket(float x, float y, MainAssetsManager manager){
        this.x=x;
        this.y=y;
        this.manager=manager;
        this.curState=State.BROKEN;
        this.bounds=new Rectangle(x,y,width,height);

        flyingEffect = new ParticleEffect();

        flyingEffect.load(
            Gdx.files.internal("assets/particles/rocket.p"),
            Gdx.files.internal("assets/particles")
        );
    }
    public void insetPart() {
        if (partsInserted < 3){
            partsInserted++;
            if (partsInserted == 3){
                curState=State.FIXED;
            }
        }
    }
    public void update(float delta){
        if(curState==State.FLYING){
            flyingEffect.setPosition((x + bounds.width / 2), y - 5f);
            flyingEffect.update(delta);
            velY+=80f*delta;
            y+=velY*delta;
            bounds.setPosition(x, y);
        }
    }
    public void draw(SpriteBatch batch){
        TextureRegion frame;
        if(curState==State.BROKEN){
            frame=manager.image.rocketBroken;
        }
        else if(curState==State.FLYING){
            flyingEffect.draw(batch);
            frame=manager.image.rocketFlying;
        }
        else{
            frame=manager.image.rocketFixed;
        }
        batch.draw(frame, x,y,width,height);
    }

    public void dispose(){
        if (flyingEffect != null){
            flyingEffect.dispose();
        }
    }

    public void launchRocket() {
        flyingEffect.start();
        curState=State.FLYING;
    }

    public boolean isFlying() {
        return curState==State.FLYING;
    }
}
