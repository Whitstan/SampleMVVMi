package com.example.sampleproject

import com.example.sampleproject.di.module.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

    private val appComponent: AndroidInjector<App> by lazy {
        DaggerAppComponent
            .factory()
            .create(this)
    }

    override fun applicationInjector() = appComponent
}