package com.techyos.oneclickonemail.ui.activities;

import android.support.v7.app.AppCompatActivity;

import com.techyos.oneclickonemail.App;

/**
 * Created by ylemin on 29/10/15.
 *
 * For lazyness purposes, I like to not rewrite code if I can avoid to. So this class will contain
 * all the code that activities share that I don't want to rewrite.
 */

public class ActivityBase extends AppCompatActivity {

    /**
     * Protected Methods
     */

    protected App getApp() {
        return (App) getApplication();
    }

}
