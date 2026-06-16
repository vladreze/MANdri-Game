package com.mandri.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mandri.storage.MainAssetsManager;

public class Monster {
    public enum State { EMPTY, HAPPY }
    public Monster.State curState;

    public float x, y;
    public Rectangle bounds;
    private float velX = 0;
    public float width = 32f;
    public float height = 32f;

    private final MainAssetsManager manager;
    public int partsInserted = 0;

    public Monster(float x, float y, MainAssetsManager manager) {
        this.x = x;
        this.y = y;
        this.manager = manager;
        this.curState = State.EMPTY;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void giveEmerald() {
        if (curState != State.HAPPY) {
            partsInserted++;
            if (partsInserted >= 2) {
                curState = State.HAPPY;
            }
        }
    }

    public void update(float delta) {
        if (curState == Monster.State.HAPPY) {
            velX -= 80f * delta;
            x += velX * delta;
            bounds.setPosition(x, y);
        }
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frame = manager.image.caveMonster;
        if (curState == Monster.State.HAPPY) {
            frame = manager.image.caveHappyMonster;
        }
        if (frame != null) {
            batch.draw(frame, x, y, width, height);
        }
    }
    public boolean isFlying() {
        return curState == Monster.State.HAPPY;
    }
}
