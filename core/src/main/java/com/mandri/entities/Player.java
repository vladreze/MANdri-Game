package com.mandri.entities;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mandri.storage.MainAssetsManager;

public class Player {
    public enum State { FALLING, JUMPING, STANDING, RUNNING }
    public State currentState;
    public State previousState;

    private float x, y;
    private float spawnX, spawnY;

    private float velocityX, velocityY;
    private final float SPEED = 150f;
    private final float GRAVITY = -600f;
    private final float JUMP_FORCE = 300f;

    public int liveCount = 3;
    private boolean isInvulnerable = false;
    private float invulnerableTimer = 0;
    private final float INVINCIBILITY_TIME = 1.5f;

    public Rectangle bounds;
    private boolean isGrounded = false;
    private boolean runningRight = true;
    private float stateTimer;

    private float stepTimer;
    private final MainAssetsManager manager;

    private ShaderProgram damageShader;

    private Array<ActiveBreakable> activeBreakables = new Array<>();

    private class ActiveBreakable {
        RectangleMapObject object;
        float timer;

        public ActiveBreakable(RectangleMapObject object, float timer) {
            this.object = object;
            this.timer = timer;
        }
    }

    public Player(float startX, float startY, MainAssetsManager manager) {
        this.x = startX;
        this.y = startY;
        this.spawnX = startX;
        this.spawnY = startY;
        this.currentState = State.STANDING;
        this.previousState = State.STANDING;
        this.stateTimer = 0;
        this.stepTimer = 0;
        this.bounds = new Rectangle(x, y, 30, 30);
        this.manager = manager;

        damageShader = new ShaderProgram(
            Gdx.files.internal("shaders/default.vsh"),
            Gdx.files.internal("shaders/damage.fsh")
        );
        damageShader.pedantic = false;

        if (!damageShader.isCompiled()) {
            Gdx.app.log("Shader Error", damageShader.getLog());
        }
    }

    public void update(float delta, TiledMapTileLayer layer, MapLayer objectLayer, float screenWidth) {
        if (isInvulnerable) {
            invulnerableTimer -= delta;
            if (invulnerableTimer <= 0) {
                isInvulnerable = false;
            }
        }

        handleBreakables(delta, objectLayer, layer);

        if (y < -50) {
            takeDamage();
            if (!isDead()) {
                this.x = spawnX;
                this.y = spawnY;
            }
        }

        velocityX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocityX = -SPEED;
            runningRight = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocityX = SPEED;
            runningRight = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded) {
            velocityY = JUMP_FORCE;
            isGrounded = false;

            manager.music.playJumpSound();
        }

        float oldX = x;
        x += velocityX * delta;
        x = MathUtils.clamp(x, 0, screenWidth - 32);
        bounds.setPosition(x, y);

        checkTraps(objectLayer);

        if (checkCollision(bounds, layer)) {
            x = oldX;
            bounds.setPosition(x, y);
            velocityX = 0;
        }

        float oldY = y;
        velocityY += GRAVITY * delta;
        y += velocityY * delta;
        bounds.setPosition(x, y);

        checkTraps(objectLayer);

        if (checkCollision(bounds, layer)) {
            if (velocityY < 0) {
                if (!isGrounded && velocityY < -50f) {
                    manager.music.playLandSound();
                }
                isGrounded = true;
            }

            y = oldY;
            bounds.setPosition(x, y);
            velocityY = 0;
        } else {
            isGrounded = false;
        }

        updateState(delta);
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

    private void checkTraps(MapLayer objectLayer) {
        if (objectLayer == null) return;
        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle trapRect = rectangleObject.getRectangle();
                String type = object.getProperties().get("type", String.class);
                if ("Trap".equals(type)) {
                    Rectangle adjustedTraRect = new Rectangle(
                        trapRect.x + 2,
                        trapRect.y,
                        trapRect.width-3,
                        trapRect.height
                    );
                    if (this.bounds.overlaps(adjustedTraRect)) {
                        takeDamage();
                    }
                }
            }
        }
    }

    private void handleBreakables(float delta, MapLayer objectLayer, TiledMapTileLayer tileLayer) {
        if (objectLayer == null || tileLayer == null) return;

        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                String type = object.getProperties().get("type", String.class);

                Rectangle originalRect = rectObject.getRectangle();

                Rectangle adjustedRect = new Rectangle(
                    originalRect.x,
                    originalRect.y,
                    originalRect.width,
                    originalRect.height + 4
                );

                if ("Breakable".equals(type) && this.bounds.overlaps(adjustedRect)) {
                    boolean alreadyActive = false;
                    for (ActiveBreakable ab : activeBreakables) {
                        if (ab.object == rectObject) {
                            alreadyActive = true;
                            break;
                        }
                    }
                    if (!alreadyActive) {
                        Float delayProp = object.getProperties().get("delay", Float.class);
                        float delay = (delayProp != null) ? delayProp : 2.0f;
                        activeBreakables.add(new ActiveBreakable(rectObject, delay));
                    }
                }
            }
        }

        for (int i = activeBreakables.size - 1; i >= 0; i--) {
            ActiveBreakable ab = activeBreakables.get(i);
            ab.timer -= delta;

            if (ab.timer <= 0) {
                Rectangle rect = ab.object.getRectangle();
                int tileX = (int) ((rect.x + 2) / 16);
                int tileY = (int) ((rect.y + 2) / 16);
                tileLayer.setCell(tileX, tileY, null);
                objectLayer.getObjects().remove(ab.object);
                activeBreakables.removeIndex(i);

                // manager.music.playBreakSound();
            }
        }
    }

    private void updateState(float delta) {
        previousState = currentState;

        if (velocityY > 0 && !isGrounded) {
            currentState = State.JUMPING;
        } else if (velocityY < 0 && !isGrounded) {
            currentState = State.FALLING;
        } else if (velocityX != 0) {
            currentState = State.RUNNING;
            stepTimer+=delta;
            if(stepTimer> 0.1f){
                manager.music.playWalkSound();
                stepTimer = 0;
            }
        } else {
            currentState = State.STANDING;
            stepTimer = 0;
        }

        if (currentState == previousState) {
            stateTimer += delta;
        } else {
            stateTimer = 0;
        }
    }

    public void draw(SpriteBatch batch) {
        if (isInvulnerable) {
            batch.setShader(damageShader);
            if (invulnerableTimer % 0.2f > 0.1f) {
                batch.draw(getFrame(), x, y);
            }
            batch.setShader(null);
        } else {
            batch.draw(getFrame(), x, y);
        }
    }

    private TextureRegion getFrame() {
        TextureRegion region;

        switch (currentState) {
            case JUMPING:
                if(velocityX == 0) region = manager.image.spaceJump_F;
                else region = runningRight ? manager.image.spaceJump_R : manager.image.spaceJump_L;
                break;
            case FALLING:
                if(velocityX == 0) region = manager.image.spaceFall_F;
                else region = runningRight ? manager.image.spaceFall_R : manager.image.spaceFall_L;
                break;
            case RUNNING:
                region = runningRight ? manager.image.spaceRun_R.getKeyFrame(stateTimer, true)
                    : manager.image.spaceRun_L.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = manager.image.spaceIdle_F.getKeyFrame(stateTimer, true);
                break;
        }
        return region;
    }

    public void takeDamage() {
        if (!isInvulnerable && !isDead()) {
            liveCount--;
            if(liveCount==2)manager.music.playHurtSound(1);
            else if(liveCount==1) manager.music.playHurtSound(2);

            if (liveCount > 0) {
                isInvulnerable = true;
                invulnerableTimer = INVINCIBILITY_TIME;
            }
        }
        if(isDead()){
            manager.music.playHurtSound(3);
        }
    }

    public boolean isDead() {
        return liveCount <= 0;
    }

    public boolean isShaking() {
        return activeBreakables.size > 0;
    }

    public void dispose() {
        if (damageShader != null) {
            damageShader.dispose();
        }
    }
}
