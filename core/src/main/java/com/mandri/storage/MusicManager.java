package com.mandri.storage;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {
    private final AssetManager manager;
    private Music music;
    private float volume = 0.3f;
    private float soundEffectsvolume = 1.0f;

    public MusicManager(AssetManager manager){
        this.manager = manager;
    }

    public void loadMusic(){
        //music
        manager.load("music/music-cave.mp3", Music.class);
        manager.load("music/music-forest.mp3", Music.class);
        manager.load("music/music-space.mp3", Music.class);

        manager.load("music/music-space-1.mp3", Music.class);

        manager.load("music/music-menu.mp3", Music.class);

        manager.load("music/logo-music.mp3", Music.class);
        manager.load("music/cs2-bg-music.mp3", Music.class);
        manager.load("music/cs3-bg-music.mp3", Music.class);
//        manager.load("music/cs4-bg-music.mp3", Music.class);
        manager.load("music/credits-music.mp3", Music.class);
        manager.load("music/cs4-bg-music.mp3", Music.class);

        //sounds
        manager.load("sounds/walking.mp3", Sound.class);
        manager.load("sounds/jump.mp3", Sound.class);
        manager.load("sounds/landing.mp3", Sound.class);
        manager.load("sounds/hurt1.mp3", Sound.class);
        manager.load("sounds/hurt2.mp3", Sound.class);
        manager.load("sounds/hurt3.mp3", Sound.class);
        manager.load("sounds/grab.mp3", Sound.class);
        manager.load("sounds/punch-monster.mp3", Sound.class);

        manager.load("sounds/bee.mp3", Sound.class);
        manager.load("sounds/enemy-hurt.mp3", Sound.class);
        manager.load("sounds/monster-punch-player2.mp3", Sound.class);
        manager.load("sounds/monster-walk.mp3", Sound.class);

        manager.load("sounds/spaceship.mp3", Sound.class);
        manager.load("sounds/rocket-break.mp3", Sound.class);
        manager.load("sounds/rocket-fly-sound.mp3", Sound.class);

        manager.load("sounds/bonus.mp3", Sound.class);
        manager.load("sounds/bonus-big.mp3", Sound.class);
        manager.load("sounds/game-over.mp3", Sound.class);

        manager.load("sounds/acid-block.mp3", Sound.class);
        manager.load("sounds/cracking-block.mp3", Sound.class);
        manager.load("sounds/thorn-block.mp3", Sound.class);

//        manager.load("sounds/text-type(long).mp3", Sound.class);
        manager.load("sounds/text-type.mp3", Sound.class);
        manager.load("sounds/splash.mp3", Sound.class);

        manager.load("sounds/pause-sound.mp3", Sound.class);
        manager.load("sounds/start-sound.mp3", Sound.class);


    }
    public void playMenuMusic(){
        music = manager.get("music/music-menu.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(volume);
        music.play();
    }
    public void playLogoMusic(){
        music = manager.get("music/logo-music.mp3", Music.class);
        music.setVolume(soundEffectsvolume);
        music.play();
    }
    public boolean isLogoMusicPlaying() {
        return music != null && music.isPlaying();
    }
    public void playCs1BgMusic(){
        music = manager.get("music/music-space.mp3", Music.class);
        music.setVolume(soundEffectsvolume);
        music.play();
    }
    public void playCs2BgMusic(){
        music = manager.get("music/cs2-bg-music.mp3", Music.class);
        music.setVolume(soundEffectsvolume);
        music.play();
    }
    public void playCs3BgMusic(){
        music = manager.get("music/cs3-bg-music.mp3", Music.class);
        music.setVolume(soundEffectsvolume);
        music.play();
    }
    public void playCs4BgMusic(){
        music = manager.get("music/cs4-bg-music.mp3", Music.class);
        music.setVolume(soundEffectsvolume);
        music.play();
    }
    public void playCreditsMusic(){
        music = manager.get("music/credits-music.mp3", Music.class);
        music.setVolume(soundEffectsvolume);
        music.play();
    }
    public void playSplashSound(){
        manager.get("sounds/splash.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playEngineSound(){
        manager.get("sounds/rocket-fly-sound.mp3", Sound.class).play(soundEffectsvolume);
    }

    public void playGameOverSound(){
        manager.get("sounds/game-over.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playLevelMusic(int n){
        switch (n){
            case 1:
                music = manager.get("music/music-space-1.mp3", Music.class);
                music.setLooping(true);
                music.setVolume(volume);
                music.play();
                break;
            case 2:
                music = manager.get("music/music-forest.mp3", Music.class);
                music.setLooping(true);
                music.setVolume(volume);
                music.play();
                break;
            case 3:
                music = manager.get("music/music-cave.mp3", Music.class);
                music.setLooping(true);
                music.setVolume(volume);
                music.play();

        }
    }

    public void playWalkSound(){
        manager.get("sounds/walking.mp3", Sound.class).play(soundEffectsvolume);
    }

    public void playJumpSound(){
        manager.get("sounds/jump.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playLandSound(){
        manager.get("sounds/landing.mp3", Sound.class).play(soundEffectsvolume);

    }
    public void playHurtSound(int n){
        switch (n){
            case 1:
                manager.get("sounds/hurt1.mp3", Sound.class).play(soundEffectsvolume);
                break;
            case 2:
                manager.get("sounds/hurt2.mp3", Sound.class).play(soundEffectsvolume);
                break;
            case 3:
                manager.get("sounds/hurt3.mp3", Sound.class).play(soundEffectsvolume);
        }

    }

    public void playGrabSound(){
        manager.get("sounds/grab.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playPunchSound(){
        manager.get("sounds/punch-monster.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playSlimeBlockSound(){
        manager.get("sounds/acid-block.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playCrackingBlockSound(){
        manager.get("sounds/cracking-block.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playThornBlockSound(){
        manager.get("sounds/thorn-block.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playMonsterWalkSound(){
        manager.get("sounds/monster-walk.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playMonsterPunchSound(){
        manager.get("sounds/monster-punch-player2.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playRocketBreakSound(){
        manager.get("sounds/rocket-break.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playTypeSound(){
        manager.get("sounds/text-type.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playBeeSound(){
        manager.get("sounds/bee.mp3", Sound.class).play(soundEffectsvolume);

    }
    public void playSpaceshipSound(){
        manager.get("sounds/spaceship.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playEnemyHurtSound(){
        manager.get("sounds/enemy-hurt.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playBonusSound(){
        manager.get("sounds/bonus.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playBigBonusSound(){
        manager.get("sounds/bonus-big.mp3", Sound.class).play(soundEffectsvolume);
    }

    public void playGameStartSound(){
        manager.get("sounds/start-sound.mp3", Sound.class).play(soundEffectsvolume);
    }
    public void playGamePauseSound(){
        manager.get("sounds/pause-sound.mp3", Sound.class).play(soundEffectsvolume);
    }


    public void disposeMusic(){
        manager.dispose();
    }

    public void stopMusic(){
        if (music != null && music.isPlaying()){
            music.stop();
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (music != null) {
            music.setVolume(volume);
        }
    }

    public float getVolume() {
        return volume;
    }

    public float getSoundEffectsvolume() {
        return soundEffectsvolume;
    }

    public void setSoundEffectsvolume(float soundEffectsvolume){
        this.soundEffectsvolume = soundEffectsvolume;

    }
}
