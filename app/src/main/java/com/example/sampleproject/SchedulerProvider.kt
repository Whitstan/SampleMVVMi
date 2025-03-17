package com.example.sampleproject

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface SchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun trampoline(): Scheduler
    fun ui(): Scheduler
}

class AppSchedulerProvider @Inject constructor() : SchedulerProvider {
    override fun io(): Scheduler = Schedulers.io()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun trampoline(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}