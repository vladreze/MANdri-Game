package com.mandri.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
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
    public boolean isFalling = false;
    private float velocityX, velocityY;
    private final float GRAVITY = 150f;
    private final MainAssetsManager manager;

    private ParticleEffect mushroomJumpEffect;

    public Item(MainAssetsManager manager, String name, float x, float y) {
        this.name=name;
        this.manager = manager;
        this.x = x;
        this.y = y;
        this.isCollected = false;
        if (name.equals("acorn") || name.equals("mushroom") || name.equals("axe")|| name.equals("emerald")||name.equals("geyser")) {
            this.width = 16f;
            this.height = 16f;
        }
        else {
            this.width = 25f;
            this.height = 25f;
        }
        this.bounds = new Rectangle(x, y, width, height);

        if ("mushroom".equals(name)||"geyser".equals(name)) {
            mushroomJumpEffect = new ParticleEffect();
            mushroomJumpEffect.load(Gdx.files.internal("particles/mushroomJump.p"), Gdx.files.internal("particles/"));
            mushroomJumpEffect.scaleEffect(.7f);
            mushroomJumpEffect.setPosition(x + width / 2, y + bounds.height / 1.25f);
        }
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
            else if ("emerald".equals(name)){
                frame=manager.image.caveEmerald;
            }
            else if ("geyser".equals(name)){
                frame=manager.image.caveGeyser;
            }
            else if ("numb0".equals(name)){
                frame=manager.image.caveNumber0;
            }
            else if ("numb1".equals(name)){
                frame=manager.image.caveNumber1;
            }
            else if ("numb3".equals(name)){
                frame=manager.image.caveNumber3;
            }
            else if ("numb5".equals(name)){
                frame=manager.image.caveNumber5;
            }
            if(frame!=null){
                batch.draw(frame,x,y,width,height);
            }
        }
        if (mushroomJumpEffect != null) mushroomJumpEffect.draw(batch);
    }

    public void update(float delta, float playerX) {
        if (mushroomJumpEffect != null) mushroomJumpEffect.update(delta);
        if ("acorn".equals(name)||"stalactite".equals(name)) {
            float playerAcornX = Math.abs(this.x - playerX);
            if (playerAcornX < 100f) {
                isFalling = true;
            }
            if (isFalling) {
                velocityY -= GRAVITY * delta;
                y += velocityY * delta;
                bounds.setPosition(x, y);
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

    public void playJumpEffect() {
        if (mushroomJumpEffect != null) mushroomJumpEffect.start();
    }
}
