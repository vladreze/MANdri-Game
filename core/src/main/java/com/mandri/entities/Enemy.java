package com.mandri.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private final float SPEED = 50f;
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

    public Enemy(float startX, float startY, MainAssetsManager manager) {
        this.x = startX;
        this.y = startY;
        this.spawnX = startX;
        this.spawnY = startY;
        this.currentState = State.ALIVE;
        this.bounds = new Rectangle(x, y, 15, 15);
        this.manager = manager;

        damageShader = new ShaderProgram(
            Gdx.files.internal("shaders/default.vsh"),
            Gdx.files.internal("shaders/damage.fsh")
        );
        damageShader.pedantic = false;
    }

    public void update(float delta, TiledMapTileLayer layer, OrthographicCamera camera) {
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
            velocityX=SPEED;
        }
        else{
            velocityX=-SPEED;
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
    }
}
