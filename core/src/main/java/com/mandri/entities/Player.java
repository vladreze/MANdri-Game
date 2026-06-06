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

    private TextureRegion idleFront;
    private TextureRegion jumpRight, jumpLeft;
    private TextureRegion fallRight, fallLeft;
    private Animation<TextureRegion> runRight, runLeft;

    private Array<Texture> texturesToDispose;

    public Player(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.currentState = State.STANDING;
        this.previousState = State.STANDING;
        this.stateTimer = 0;
        this.texturesToDispose = new Array<>();
        this.bounds = new Rectangle(x, y, 32, 32);
        loadAnimations();
    }

    private void loadAnimations() {
        Texture tFront = new Texture("space/space_fall_F.png");
        Texture tJumpR = new Texture("space/space_jump_R.png");
        Texture tJumpL = new Texture("space/space_jump_L.png");
        Texture tFallR = new Texture("space/space_fall_R.png");
        Texture tFallL = new Texture("space/space_fall_L.png");

        texturesToDispose.addAll(tFront, tJumpR, tJumpL, tFallR, tFallL);

        idleFront = new TextureRegion(tFront);
        jumpRight = new TextureRegion(tJumpR);
        jumpLeft = new TextureRegion(tJumpL);
        fallRight = new TextureRegion(tFallR);
        fallLeft = new TextureRegion(tFallL);

        Texture tRunR = new Texture("space/space_run_R.png");
        texturesToDispose.add(tRunR);
        TextureRegion[][] tmpR = TextureRegion.split(tRunR, 32, 32);
        runRight = new Animation<>(0.15f, tmpR[0]);

        Texture tRunL = new Texture("space/space_run_L.png");
        texturesToDispose.add(tRunL);
        TextureRegion[][] tmpL = TextureRegion.split(tRunL, 32, 32);
        runLeft = new Animation<>(0.15f, tmpL[0]);
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
        }

        velocityY += GRAVITY * delta;
        x += velocityX * delta;
        y += velocityY * delta;

        x = MathUtils.clamp(x, 0, screenWidth - 32);

        if (y <= floorY) {
            y = floorY;
            velocityY = 0;
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
        } else {
            currentState = State.STANDING;
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
                region = runningRight ? jumpRight : jumpLeft;
                break;
            case FALLING:
                region = runningRight ? fallRight : fallLeft;
                break;
            case RUNNING:
                region = runningRight ? runRight.getKeyFrame(stateTimer, true) : runLeft.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = idleFront;
                break;
        }
        return region;
    }

    public void dispose() {
        for (Texture t : texturesToDispose) {
            t.dispose();
        }
    }
}
