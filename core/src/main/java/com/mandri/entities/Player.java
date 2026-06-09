package com.mandri.entities;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mandri.storage.MainAssetsManager;

public class Player {
    public enum State { FALLING, JUMPING, STANDING, RUNNING }
    public State currentState;
    public State previousState;

    private float x, y;
    private float velocityX, velocityY;
    private final float SPEED = 150f;
    private final float GRAVITY = -600f;
    private final float JUMP_FORCE = 300f;

    public Rectangle bounds;
    private boolean isGrounded = false;
    private boolean runningRight = true;
    private float stateTimer;

    private float stepTimer;
    private final MainAssetsManager manager;

    public Player(float startX, float startY, MainAssetsManager manager) {
        this.x = startX;
        this.y = startY;
        this.currentState = State.STANDING;
        this.previousState = State.STANDING;
        this.stateTimer = 0;
        this.stepTimer = 0;
        this.bounds = new Rectangle(x, y, 30, 30);
        this.manager = manager;
    }

    public void update(float delta, TiledMapTileLayer layer, float screenWidth) {
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
        x = MathUtils.clamp(x, 0, screenWidth - 32); // Границы мира
        bounds.setPosition(x, y);

        if (checkCollision(bounds, layer)) {
            x = oldX;
            bounds.setPosition(x, y);
            velocityX = 0;
        }

        float oldY = y;
        velocityY += GRAVITY * delta;
        y += velocityY * delta;
        bounds.setPosition(x, y);

        if (checkCollision(bounds, layer)) {
            if (velocityY < 0) {
                if (!isGrounded && velocityY < -50f) {
                    manager.music.playLandSound();
                }
                isGrounded = true;
            }
            else if(velocityY>0) manager.music.playHurtSound(1);
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
        batch.draw(getFrame(), x, y);
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
}
