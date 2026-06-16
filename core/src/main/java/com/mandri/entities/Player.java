package com.mandri.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
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

    public float velocityX, velocityY;
    private final float SPEED = 150f;
    public final float JUMP_FORCE = 350f;
    private final float GRAVITY = -(JUMP_FORCE * 2);
    private boolean wasSpacePressed = false;

    public int liveCount = 3;
    public boolean isJetpackEnabled = true;

    private boolean isInvulnerable = false;
    public boolean isInventoryOpen = false;
    private float invulnerableTimer = 0;
    private final float INVINCIBILITY_TIME = 1.5f;

    public Rectangle bounds;
    private boolean isGrounded = false;
    private boolean runningRight = true;
    private float stateTimer;

    private float playerDamageRed;
    private float playerDamageGreen;
    private float playerDamageBlue;

    private float stepTimer;
    private final MainAssetsManager manager;

    private ShaderProgram damageShader;

    private ParticleEffect groundParticleEffect;
    private ParticleEffect jetpackParticleEffect;
    private ParticleEffect damageParticleEffect;
    private ParticleEffect fallingParticleEffect;

    private Array<ActiveBreakable> activeBreakables = new Array<>();

    private TextureRegion jumpF, jumpR, jumpL;
    private TextureRegion fallF, fallR, fallL;
    private Animation<TextureRegion> runR, runL, idleF;
    private int jumpCount = 0;

    private class ActiveBreakable {
        RectangleMapObject object;
        float timer;

        public ActiveBreakable(RectangleMapObject object, float timer) {
            this.object = object;
            this.timer = timer;
        }
    }

    public Player(float startX, float startY, MainAssetsManager manager, String theme) {
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

        switch (theme) {
            case "forest":
                jumpF = manager.image.forestJump_F;
                jumpR = manager.image.forestJump_R;
                jumpL = manager.image.forestJump_L;
                fallF = manager.image.forestFall_F;
                fallR = manager.image.forestFall_R;
                fallL = manager.image.forestFall_L;
                runR  = manager.image.forestRun_R;
                runL  = manager.image.forestRun_L;
                idleF = manager.image.forestIdle_F;
                isJetpackEnabled = false;
                break;
            case "cave":
                jumpF = manager.image.caveJump_F;
                jumpR = manager.image.caveJump_R;
                jumpL = manager.image.caveJump_L;
                fallF = manager.image.caveFall_F;
                fallR = manager.image.caveFall_R;
                fallL = manager.image.caveFall_L;
                runR  = manager.image.caveRun_R;
                runL  = manager.image.caveRun_L;
                idleF = manager.image.caveIdle_F;
                isJetpackEnabled = false;
                break;
            default:
                jumpF = manager.image.spaceJump_F;
                jumpR = manager.image.spaceJump_R;
                jumpL = manager.image.spaceJump_L;
                fallF = manager.image.spaceFall_F;
                fallR = manager.image.spaceFall_R;
                fallL = manager.image.spaceFall_L;
                runR  = manager.image.spaceRun_R;
                runL  = manager.image.spaceRun_L;
                idleF = manager.image.spaceIdle_F;

                break;
        }

        damageShader = new ShaderProgram(
            Gdx.files.internal("shaders/default.vsh"),
            Gdx.files.internal("shaders/damage.fsh")
        );
        damageShader.pedantic = false;

        if (!damageShader.isCompiled()) {
            Gdx.app.log("Shader Error", damageShader.getLog());
        }

        groundParticleEffect = new ParticleEffect();
        damageParticleEffect = new ParticleEffect();
        fallingParticleEffect = new ParticleEffect();

        if (isJetpackEnabled) {
            jetpackParticleEffect = new ParticleEffect();

            jetpackParticleEffect.load(
                Gdx.files.internal("assets/particles/jetpack.p"),
                Gdx.files.internal("assets/particles/")
            );
            jetpackParticleEffect.scaleEffect(.85f);
        }

        groundParticleEffect.load(
            Gdx.files.internal("assets/particles/ground.p"),
            Gdx.files.internal("assets/particles/")
        );
        groundParticleEffect.scaleEffect(1f);

        damageParticleEffect.load(
            Gdx.files.internal("assets/particles/damage.p"),
            Gdx.files.internal("assets/particles/")
        );
        damageParticleEffect.scaleEffect(1f);

        fallingParticleEffect.load(
            Gdx.files.internal("assets/particles/fall.p"),
            Gdx.files.internal("assets/particles/")
        );
        fallingParticleEffect.scaleEffect(.5f);

        groundParticleEffect.allowCompletion();
        damageParticleEffect.allowCompletion();
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
            takeDamage("fall");
            if (!isDead()) {
                this.x = spawnX;
                this.y = spawnY;
            }
        }

        float jetpackOffsetX = runningRight ? 10f : 20f;

        if (isJetpackEnabled) {
            jetpackParticleEffect.setPosition(x + jetpackOffsetX, y + 12);
        }

        groundParticleEffect.setPosition((x + (bounds.width / 2)) - 6f, y);
        damageParticleEffect.setPosition(x + bounds.width / 2, y + bounds.height / 2);

        fallingParticleEffect.setPosition((x + (bounds.width / 2)) - 6f, y);

        velocityX = 0;
        if (!isDead() && !isInventoryOpen) {
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
                jumpCount++;

                if (isJetpackEnabled) {
                    jetpackParticleEffect.start();
                }

                manager.music.playJumpSound();
            }
        }

        if (wasSpacePressed && !Gdx.input.isKeyPressed(Input.Keys.SPACE) && velocityY > 0) {
            velocityY *= 0.75f;
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
                    groundParticleEffect.reset();
                    manager.music.playLandSound();
                }

                if (isJetpackEnabled) {
                    jetpackParticleEffect.allowCompletion();
                }

                isGrounded = true;
                jumpCount = 0;
            }

            y = oldY;
            bounds.setPosition(x, y);
            velocityY = 0;
        } else {
            isGrounded = false;
        }

        groundParticleEffect.update(delta);

        if (isJetpackEnabled) {
            jetpackParticleEffect.update(delta);
        }

        damageParticleEffect.update(delta);
        fallingParticleEffect.update(delta);
        wasSpacePressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
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

                if (type != null && type.contains("trap")) {
                    Rectangle adjustedTraRect = new Rectangle(
                        trapRect.x + 2,
                        trapRect.y,
                        trapRect.width-3,
                        trapRect.height
                    );

                    if (this.bounds.overlaps(adjustedTraRect)) {
                        if (type.equals("trap-poison")) {
                            takeDamage("trap-poison");
                        }
                        else if (type.equals("trap-thorn")) {
                            takeDamage("trap-thorn");
                        }
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
                        if (this.currentState == State.FALLING && this.bounds.y > adjustedRect.y) {
                            Float delayProp = object.getProperties().get("delay", Float.class);
                            float delay = (delayProp != null) ? delayProp : 2.0f;
                            activeBreakables.add(new ActiveBreakable(rectObject, delay));
                            manager.music.playCrackingBlockSound();
                        }
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

        if (currentState == State.FALLING && previousState != State.FALLING) {
            fallingParticleEffect.start();
            if (isJetpackEnabled) {
                jetpackParticleEffect.allowCompletion();
            }
        }
        if (previousState == State.FALLING && currentState != State.FALLING) {
            fallingParticleEffect.allowCompletion();
        }

        if (currentState == previousState) {
            stateTimer += delta;
        } else {
            stateTimer = 0;
        }
    }

    public void draw(SpriteBatch batch) {
        ShaderProgram currentShader = batch.getShader();
        batch.setShader(null);
        if (isJetpackEnabled) {
            jetpackParticleEffect.draw(batch);
        }
        fallingParticleEffect.draw(batch);
        if (isInvulnerable) {
            batch.setShader(damageShader);
            damageShader.setUniformf("damage_color", playerDamageRed, playerDamageGreen, playerDamageBlue);
            if (invulnerableTimer % 0.2f > 0.1f) {
                batch.draw(getFrame(), x, y);
            }
        } else {
            batch.setShader(currentShader);
            batch.draw(getFrame(), x, y);
        }
        batch.setShader(null);
        groundParticleEffect.draw(batch);
        damageParticleEffect.draw(batch);
        batch.setShader(currentShader);
    }

    private TextureRegion getFrame() {
        TextureRegion region;

        switch (currentState) {
            case JUMPING:
                if (velocityX == 0) region = jumpF;
                else region = runningRight ? jumpR : jumpL;
                break;
            case FALLING:
                if (velocityX == 0) region = fallF;
                else region = runningRight ? fallR : fallL;
                break;
            case RUNNING:
                region = runningRight ? runR.getKeyFrame(stateTimer, true) : runL.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = idleF.getKeyFrame(stateTimer, true);
                break;
        }
        return region;
    }

    public void bounce() {
        velocityY = JUMP_FORCE * 0.8f;
        currentState = State.JUMPING;
        isGrounded = false;
    }

    public void takeDamage(String trapType) {
        if (!isInvulnerable && !isDead()) {
            liveCount--;
            damageParticleEffect.reset();
            manager.music.playMonsterPunchSound();
            if(liveCount==2){
                manager.music.playHurtSound(1);
            }
            else if(liveCount==1){
                manager.music.playHurtSound(2);
            }
            if (liveCount > 0) {
                isInvulnerable = true;
                invulnerableTimer = INVINCIBILITY_TIME;
            }
            switch (trapType) {
                case "trap-thorn": {
                    playerDamageRed = .7f;
                    playerDamageGreen = -.2f;
                    playerDamageBlue = -.2f;
                    manager.music.playThornBlockSound();
                    break;
                }
                case "trap-poison": {
                    playerDamageRed = -.2f;
                    playerDamageGreen = .7f;
                    playerDamageBlue = -.2f;
                    manager.music.playSlimeBlockSound();
                    break;
                }
                default: {
                    playerDamageRed = .5f;
                    playerDamageGreen = -.2f;
                    playerDamageBlue = -.2f;
                }
            }
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
        if (groundParticleEffect != null) {
            groundParticleEffect.dispose();
        }
        if (jetpackParticleEffect != null) {
            jetpackParticleEffect.dispose();
        }
        if (damageParticleEffect != null) {
            damageParticleEffect.dispose();
        }
        if (fallingParticleEffect != null) {
            fallingParticleEffect.dispose();
        }
    }

    public boolean isRunningRight() {
        return runningRight;
    }

    public void drawShadow(SpriteBatch batch, float offsetX, float offsetY) {
        batch.setColor(0, 0, 0, 0.4f);
        batch.draw(manager.image.whitePixel, x + 4, y - 2, bounds.width - 8, 4);
        batch.setColor(1f, 1f, 1f, 1f);    }
}
