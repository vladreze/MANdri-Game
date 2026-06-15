package com.mandri.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mandri.storage.MainAssetsManager;
import com.mandri.ui.FontCreator;

public class CutsceneScreen implements Screen {
    private String[] storyText;
    private Texture[] cutsceneImg;
    private int currentSlide = 0;

    private Screen screen;

    private Texture textBg;

    private final Main main;
    private final MainAssetsManager manager;

    private Animation<TextureRegion> animation;
    private  float animationTimer = 0f;
    private enum State{ANIMATION, DIALOGUE}
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

    public CutsceneScreen(Main main, MainAssetsManager manager){
//        , Texture[] cutsceneImg, String[] storyText,
//                          Screen screen, Animation<TextureRegion> anim) {
        this.main = main;
        this.manager = manager;
        this.printTimer = 0f;
//        this.storyText = storyText;
//        this.cutsceneImg = cutsceneImg;
//        this.screen = screen;
//        this.animation = anim;

        if(this.animation != null) this.currentState = State.ANIMATION;
        else this.currentState = State.DIALOGUE;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = FontCreator.generateTextFont(30, 2f);
        camera = new OrthographicCamera();
//        camera.setToOrtho(false, 800, 480);
        camera.setToOrtho(false, 1280, 720);

        //bg opacity
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.7f);
        pixmap.fill();
        textBg = new Texture(pixmap);
        pixmap.dispose();

        TextureRegion[] frames = new TextureRegion[7];
        cutsceneImg = new Texture[]{
          manager.image.cutscene1(1),
            manager.image.cutscene1(2),
            manager.image.cutscene1(3),
            manager.image.cutscene1(2),
            manager.image.cutscene1(1),
            manager.image.cutscene1(2),
            manager.image.cutscene1(3),
            manager.image.cutscene1(2),
            manager.image.cutscene1(9),
            manager.image.cutscene1(10),
            manager.image.cutscene1(11),
            manager.image.cutscene1(12)

            //2
            //3
//            manager.image.cutscene3(1),
//            manager.image.cutscene3(2),
//            manager.image.cutscene3(3),
//            manager.image.cutscene3(5),
//            manager.image.cutscene3(4),
//            manager.image.cutscene3(5),
//            manager.image.cutscene3(3),
//            manager.image.cutscene3(6),
//            manager.image.cutscene3(5),
//            manager.image.cutscene3(6),
//            manager.image.cutscene3(7),
//            manager.image.cutscene3(8),
//            manager.image.cutscene3(9),
//            manager.image.cutscene3(3)
        };

        storyText = new String[]{
          "[System]: Warning!",
            "[System]: An unknown object is approaching the ship at high speed. Collision is imminent.",
            "[System]: Warni...",

            "[You]: Aaah...What happened?\n Where am I?",
            "[You]:Oh no, my spaceship broke down! \n I need to find the parts that fell off.",
            "And so my journey began...",
            //2
            "[You]:Finally I'm flying home...",
            //3
            "[You]:Damn, I've fallen somewhere again. \n Well, let's see where this road leads..."
        };

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

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
                if(currentSlide< storyText.length){
                    displayedText = "";
                    charIndex = 0;
                    isTyping = true;
                }else{
                    main.setScreen(new PlayScreen(main, manager)); //CHANGE GAME SCREEN
                    return;
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

            if(animation.isAnimationFinished(animationTimer)) currentState = State.DIALOGUE;
        }
        else if(currentState == State.DIALOGUE){
            batch.draw(cutsceneImg[currentSlide], 0, 0, 1280, 720);
            batch.draw(textBg, 0, 0, 1280, 320);
            font.draw(batch, displayedText, 50, 200);

            if(!isTyping){
                if(Math.sin(System.currentTimeMillis()/200.0)>0) font.draw(batch, "press SPACE", 1100, 100);
            }
        }

        batch.end();
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
