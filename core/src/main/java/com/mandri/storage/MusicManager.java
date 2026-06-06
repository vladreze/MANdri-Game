package com.mandri.storage;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {
    private final AssetManager manager;
    private Music music;

    public MusicManager(AssetManager manager){
        this.manager = manager;
    }

    public void loadMusic(){
        //music
        manager.load("music/music-cave.mp3", Music.class);
        manager.load("music/music-forest.mp3", Music.class);
        manager.load("music/music-space.mp3", Music.class);

        manager.load("music/music-menu.mp3", Music.class);

        //sounds
        manager.load("sounds/walking.mp3", Sound.class);
        manager.load("sounds/jump.mp3", Sound.class);
        manager.load("sounds/landing.mp3", Sound.class);
        manager.load("sounds/hurt1.mp3", Sound.class);
        manager.load("sounds/hurt2.mp3", Sound.class);
        manager.load("sounds/hurt3.mp3", Sound.class);

        manager.load("sounds/bee.mp3", Sound.class);
        manager.load("sounds/enemy-hurt.mp3", Sound.class);
        manager.load("sounds/spaceship.mp3", Sound.class);

        manager.load("sounds/bonus.mp3", Sound.class);
        manager.load("sounds/bonus-big.mp3", Sound.class);

    }
    public void playMenuMusic(){
        music = manager.get("music/music-menu.mp3", Music.class);
        music.setLooping(true);
        music.play();
    }
    public void playLevelMusic(int n){
        switch (n){
            case 1:
                music = manager.get("music/music-space.mp3", Music.class);
                music.setLooping(true);
                music.play();
                break;
            case 2:
                music = manager.get("music/music-forest.mp3", Music.class);
                music.setLooping(true);
                music.play();
                break;
            case 3:
                music = manager.get("music/music-cave.mp3", Music.class);
                music.setLooping(true);
                music.play();
        }
    }

    public void playWalkSound(){
        manager.get("sounds/walking.mp3", Sound.class).play();
    }

    public void playJumpSound(){
        manager.get("sounds/jump.mp3", Sound.class).play();
    }
    public void playLandSound(){
        manager.get("sounds/landing.mp3", Sound.class).play();

    }
    public void playHurtSound(int n){
        switch (n){
            case 1:
                manager.get("sounds/hurt1.mp3", Sound.class).play();
                break;
            case 2:
                manager.get("sounds/hurt2.mp3", Sound.class).play();
                break;
            case 3:
                manager.get("sounds/hurt3.mp3", Sound.class).play();
        }

    }
    public void playBeeSound(){
        manager.get("sounds/bee.mp3", Sound.class).play();

    }
    public void playSpaceshipSound(){
        manager.get("sounds/spaceship.mp3", Sound.class).play();
    }
    public void playEnemyHurtSound(){
        manager.get("sounds/enemy-hurt.mp3", Sound.class).play();
    }
    public void playBonusSound(){
        manager.get("sounds/bonus.mp3", Sound.class).play();
    }
    public void playBigBonusSound(){
        manager.get("sounds/bonus-big.mp3", Sound.class).play();
    }

    public void disposeMusic(){
        manager.dispose();
    }
}
