package com.mandri.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    private float velocityX, velocityY;
    private final float SPEED = 300f;
    private final float GRAVITY = -1000f;
    private final float JUMP_FORCE = 500f;

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
        this.bounds = new Rectangle(x, y, 32, 32);
        this.manager = manager;

    }

    public void update(float delta, float floorY, int screenWidth) {
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

        velocityY += GRAVITY * delta;
        x += velocityX * delta;
        y += velocityY * delta;

        x = MathUtils.clamp(x, 0, screenWidth - 32);

        if (y <= floorY) {
            y = floorY;
            velocityY = 0;

            if(!isGrounded){
                manager.music.playLandSound();
            }
            isGrounded = true;
        } else {
            isGrounded = false;
        }

        bounds.setPosition(x, y);

        updateState(delta);
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
