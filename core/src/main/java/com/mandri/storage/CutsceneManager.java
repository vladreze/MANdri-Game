package com.mandri.storage;

import com.mandri.game.CutsceneScreen;
import com.mandri.game.Main;

public class CutsceneManager {
    private Main main;
    private MainAssetsManager manager;
    private CutsceneScreen cutsceneScreen;
    public CutsceneManager(Main main, MainAssetsManager manager){
        this.main = main;
        this.manager = manager;
    }
}
