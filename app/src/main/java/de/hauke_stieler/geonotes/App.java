package de.hauke_stieler.geonotes;

import android.app.Application;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())
                .addInflater(new SkinMaterialViewInflater())
                .addInflater(new SkinConstraintViewInflater())
                .addInflater(new SkinCardViewInflater())
                .setSkinWindowBackgroundEnable(true)
                .setSkinStatusBarColorEnable(true)
                .loadSkin();
    }
}
