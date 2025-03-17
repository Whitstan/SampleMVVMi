package com.example.sampleproject.di.module

import com.example.sampleproject.di.scope.FragmentScope
import com.example.sampleproject.ui.details.DetailsFragment
import com.example.sampleproject.ui.details.DetailsFragmentModule
import com.example.sampleproject.ui.list.ListFragment
import com.example.sampleproject.ui.list.ListFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ContributeFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ListFragmentModule::class])
    abstract fun contributeListFragmentInjector(): ListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [DetailsFragmentModule::class])
    abstract fun contributeDetailsFragmentInjector(): DetailsFragment
}