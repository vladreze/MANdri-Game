package com.mandri.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mandri.storage.MainAssetsManager;

public class Enemy {
    public enum State { ALIVE, DEAD }
    public Enemy.State currentState;
    private float x, y;
    private float spawnX, spawnY;

    public float velocityX, velocityY;
    private float speed;
    private final float GRAVITY = -600f;
    private final float PATROL_DISTANCE = 50f;

    private float enemyDamageRed = .8f;
    private float enemyDamageGreen = .3f;
    private float enemyDamageBlue = .3f;

    private ShaderProgram damageShader;

    public Rectangle bounds;
    private boolean runningRight = true;
    private final MainAssetsManager manager;

    public boolean isDead = false;

    public float deathTimer = 0;
    public float DEATH_TIME = 2.5f;

    private float walkTimer = 0f;

    private ParticleEffect deathEffect;
    private String type;
    private boolean isFlying;
    private TextureRegion aliveTexture;
    private TextureRegion deadTexture;
    private TextureRegion angryTexture;
    private boolean isAngry=false;

    public Enemy(float startX, float startY, MainAssetsManager manager, String type) {
        this.x = startX;
        this.y = startY;
        this.spawnX = startX;
        this.spawnY = startY;
        this.currentState = State.ALIVE;
        this.bounds = new Rectangle(x, y, 15, 15);
        this.manager = manager;
        this.type = type;
        if("bee".equals(type)) {
            this.aliveTexture=new TextureRegion(manager.image.forestBeeAlive);
            this.angryTexture=new TextureRegion(manager.image.forestBeeAngr);
            this.deadTexture = new TextureRegion(manager.image.forestBeeDead);
            this.speed = 35f;
            this.isFlying = true;
            this.angryTexture = new TextureRegion(manager.image.forestBeeAngr);
        }
        if("hive".equals(type)) {
            this.aliveTexture=new TextureRegion(manager.image.forestHive);
            this.deadTexture = new TextureRegion(manager.image.forestHive);
            this.speed = 0f;
            this.isFlying = true;
        }
        if("fox".equals(type)) {
            this.aliveTexture=new TextureRegion(manager.image.forestFox);
            this.deadTexture = new TextureRegion(manager.image.forestFox);
            this.speed = 30f;
            this.isFlying = true;
        }
        if("bat".equals(type)) {
            this.aliveTexture=new TextureRegion(manager.image.forestBeeAlive);
            this.deadTexture = new TextureRegion(manager.image.forestBeeDead);
            this.speed = 30f;
            this.isFlying = true;
        }
        damageShader = new ShaderProgram(
            Gdx.files.internal("shaders/default.vsh"),
            Gdx.files.internal("shaders/damage.fsh")
        );
        damageShader.pedantic = false;

        deathEffect = new ParticleEffect();

        deathEffect.load(
            Gdx.files.internal("particles/deathMobSpace.p"),
            Gdx.files.internal("particles/")
        );

        deathEffect.setPosition(x + bounds.width / 2, y + bounds.height);
    }
    public void beeAngry(){
        if("bee".equals(type)&&!isDead&&!isAngry) {
            this.aliveTexture=angryTexture;
            isAngry=true;
        }
    }
    public void update(float delta, TiledMapTileLayer layer, OrthographicCamera camera) {
        deathEffect.update(delta);

        if(currentState==State.DEAD) {
            deathTimer += delta;
            velocityY += GRAVITY * delta;
            y += velocityY * delta;
            this.bounds.setPosition(x, y);
            return;
        }
        float camL = camera.position.x - (camera.viewportWidth/2);
        float camR = camera.position.x + (camera.viewportWidth/2);
        boolean onScreen = (x + bounds.width> camL && x <camR);
        walkTimer+=delta;
        if(walkTimer> 0.95f){
            if(onScreen) manager.music .playMonsterWalkSound();
            walkTimer = 0f;
        }
        if(runningRight==true){
            velocityX=speed;
        }
        else{
            velocityX=-speed;
        }

        float oldX = x;
        x += velocityX * delta;
        if(x>spawnX+PATROL_DISTANCE){
            x=spawnX+PATROL_DISTANCE;
            runningRight = false;
        }
        else if(x<spawnX-PATROL_DISTANCE){
            x=spawnX-PATROL_DISTANCE;
            runningRight = true;
        }
        bounds.setPosition(x, y);

        if (checkCollision(bounds, layer)) {
            x = oldX;
            bounds.setPosition(x, y);
            runningRight = !runningRight;
        }

        float oldY = y;
        velocityY += GRAVITY * delta;
        y += velocityY * delta;
        bounds.setPosition(x, y);

        if (checkCollision(bounds, layer)) {
            y = oldY;
            bounds.setPosition(x, y);
            velocityY = 0;
        }
    }

    private boolean checkCollision(Rectangle rect, TiledMapTileLayer layer) {
        int startX = (int) (rect.x / 16);
        int endX = (int) ((rect.x + rect.width - 0.1f) / 16);
        int startY = (int) (rect.y / 16);
        int endY = (int) ((rect.y + rect.height - 0.1f) / 16);

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                if (layer.getCell(i, j) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frame;
        if(currentState==State.ALIVE) {
        frame=manager.image.spaceMobAlive;
        //напрямок руху
            if((!runningRight&&!frame.isFlipX())|| (runningRight&&frame.isFlipX())){
                frame.flip(true,false);
            }
            batch.draw(frame, x, y);
        }
        else{
            deathEffect.draw(batch);
            frame = manager.image.spaceMobDead;
            batch.setShader(damageShader);
            damageShader.setUniformf("damage_color", enemyDamageRed, enemyDamageGreen, enemyDamageBlue);
            if (!frame.isFlipY()) {
                frame.flip(false,true);
            }
            if (deathTimer % .2f > .1f) {
                batch.draw(frame, x, y);
            }
            batch.setShader(null);
        }
    }

    public void dispose() {
        if (damageShader != null) {
            damageShader.dispose();
        }
        if (deathEffect != null) {
            deathEffect.dispose();
        }
    }

    public void drawShadow(SpriteBatch batch, float offsetX, float offsetY) {
        TextureRegion frame;
        if (currentState == State.ALIVE) {
            frame = manager.image.spaceMobAlive;
            if ((!runningRight && !frame.isFlipX()) || (runningRight && frame.isFlipX())) {
                frame.flip(true, false);
            }
        } else {
            frame = manager.image.spaceMobDead;
            if (!frame.isFlipY()) {
                frame.flip(false, true);
            }
        }
        batch.draw(frame, x + offsetX, y + offsetY);
    }

    public void die() {
        if (!isDead) {
            isDead = true;
            currentState = State.DEAD;
            deathEffect.setPosition(x + bounds.width / 2, y + bounds.height / 2);
            deathEffect.start();
        }
    }
}
