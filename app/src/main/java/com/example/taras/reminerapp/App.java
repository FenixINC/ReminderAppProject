package com.example.taras.reminerapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

/**
 * Created by Taras Koloshmatin on 26.07.2018
 */
public class App extends Application {

    private static App sInstance;

    public App() {
        super();
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        } else {
            Timber.plant(new NoLoggingTree());
        }
    }

    public static App getInstance() {
        return sInstance;
    }

    public class NoLoggingTree extends Timber.Tree {

        @Override
        protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        }
    }
}
