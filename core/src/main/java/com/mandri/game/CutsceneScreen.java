package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mandri.storage.MainAssetsManager;
import com.mandri.ui.FontCreator;

public class CutsceneScreen implements Screen {
    private String[] storyText;
    private Texture[] cutsceneImg;
    private int currentSlide = 0;

    private Screen screen;
    private Vector2[] textPositions;

    private Texture textBg;

    private final Main main;
    private final MainAssetsManager manager;

    private Animation<TextureRegion> animation;
    private  float animationTimer = 0f;
    private Animation<TextureRegion> animation2;
    private  float animationTimer2 = 0f;
    private enum State{ANIMATION, DIALOGUE,ANIM2}
    private State currentState;

    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    private boolean isTyping = true;
    private String displayedText = "";
    private float timer = 0f;
    private float printTimer;
    private final float TYPING_DELAY = 0.1f;
    private int charIndex = 0;

    private int id;

    private boolean logoMusicStarted = false;
    private float musicSafeguardTimer = 0f;

    private boolean isFadingOut = false;
    private float transitionAlpha = 1.0f;
    private boolean isFadingIn = true;

    public CutsceneScreen(Main main, MainAssetsManager manager, Texture[] cutsceneImg, String[] storyText,
                          Screen screen, Animation<TextureRegion> anim,Animation<TextureRegion> anim2,
                          Vector2[] textPositions, int id) {
        this.main = main;
        this.manager = manager;
        this.printTimer = 0f;
        this.storyText = storyText;
        this.cutsceneImg = cutsceneImg;
        this.screen = screen;
        this.animation = anim;
        this.animation2 = anim2;
        this.textPositions = textPositions;
        this.id = id;

        if(this.animation != null) this.currentState = State.ANIMATION;
        else this.currentState = State.DIALOGUE;
        if(id==1) manager.music.playCs1BgMusic();
        if(id==2){
            manager.music.playCs2BgMusic();
            manager.music.playEngineSound();
        }
        if(id==3){
            manager.music.playCs3BgMusic();
        }
        if(id==4){
            manager.music.playSplashSound();
            manager.music.playCs4BgMusic();
        }

    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = FontCreator.generateTextFont(30, 2f);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        //bg opacity
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.7f);
        pixmap.fill();
        textBg = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (logoMusicStarted) {
            musicSafeguardTimer += delta;
            if (musicSafeguardTimer > 0.5f && !manager.music.isLogoMusicPlaying()) {
                main.setScreen(screen);
                return;
            }
        }

        if (currentState == State.DIALOGUE) {
            if(isTyping){
                timer +=delta;
                if(timer>= TYPING_DELAY){
                    timer = 0f;
                    if(charIndex< storyText[currentSlide].length()){ //cont to print
                        charIndex++;
                        displayedText = storyText[currentSlide].substring(0, charIndex);

                        printTimer+=delta;
                        if(printTimer> 0.05f){
                            manager.music.playTypeSound();
                            printTimer = 0;
                        }
                    }
                    else{
                        isTyping = false; //end print
                        printTimer = 0;
                    }
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if(!isTyping){
                    currentSlide++;
                    if(currentSlide>= storyText.length){
//                        if(animation2 !=null) currentState = State.ANIM2;
//                        else main.setScreen(screen); //CHANGE GAME SCREEN
                        currentSlide = storyText.length - 1;
                        isFadingOut = true;
                    }else{
                        displayedText = "";
                        charIndex = 0;
                        isTyping = true;

                        if(id==1){
                            if(currentSlide == storyText.length-2) manager.music.stopMusic();
                            if(currentSlide == 3) manager.music.playRocketBreakSound();
                        }
                        if (currentSlide == storyText.length-1) {
                            if(id==1){
                                manager.music.playLogoMusic();
                                logoMusicStarted = true;
                            }
                        }
                    }
                } else {
                    charIndex = storyText[currentSlide].length();
                    displayedText = storyText[currentSlide];
                    isTyping = false;
                }
            }
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if(currentState == State.ANIMATION){
            animationTimer+=delta;
            TextureRegion frame = animation.getKeyFrame(animationTimer);
            batch.draw(frame, 0, 0, 1280, 720);

            if(animation.isAnimationFinished(animationTimer)) {
                if(id!=4) currentState = State.DIALOGUE;
                else{
                    main.setScreen(screen);
                    return;
                }
            }
        }
        else if(currentState == State.DIALOGUE){
            batch.draw(cutsceneImg[currentSlide], 0, 0, 1280, 720);

            if (id==1){
                if(currentSlide == 0 || currentSlide ==1 || currentSlide ==2 || currentSlide ==3){
                    batch.draw(textBg, 0, 0, 1280, 320);
                }
            }
            if(id==2){
                batch.draw(textBg, 0, 0, 1280, 150);
            }

            float currentTextX = textPositions[currentSlide].x;
            float currentTextY = textPositions[currentSlide].y;
            font.draw(batch, displayedText, currentTextX, currentTextY);

            if(!isTyping ){
                if(Math.sin(System.currentTimeMillis()/200.0)>0) {
                    if(currentSlide != storyText.length-1){
                        font.draw(batch, "[press SPACE]", 1000, 100);
                    }
                    if(id==3)font.draw(batch, "[press SPACE]", 1000, 100);
                }
            }
        }
        if(currentState == State.ANIM2){
            animationTimer2+=delta;
            TextureRegion frame = animation2.getKeyFrame(animationTimer2);
            batch.draw(frame, 0, 0, 1280, 720);

            if(animation2.isAnimationFinished(animationTimer2)) main.setScreen(screen);
        }

        batch.end();

        if (isFadingIn) {
            transitionAlpha -= delta * 0.8f;
            if (transitionAlpha <= 0) {
                transitionAlpha = 0;
                isFadingIn = false;
            }
        } else if (isFadingOut) {
            transitionAlpha += delta * 0.8f;
            if (transitionAlpha >= 1f) {
                main.setScreen(screen);
                return;
            }
        }
        if (transitionAlpha > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.begin();
            batch.setColor(0, 0, 0, transitionAlpha);
            batch.draw(manager.image.whitePixel, 0, 0, 1280, 720);
            batch.setColor(Color.WHITE);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        manager.disposeAll();
        batch.dispose();
        font.dispose();
        textBg.dispose();
    }
}
