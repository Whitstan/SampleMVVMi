package com.example.sampleproject.util

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector

abstract class InjectingTestApplication : Application(), HasAndroidInjector, AndroidInjector<Any> {
    override fun androidInjector(): AndroidInjector<Any> = this
}