package com.example.sampleproject.ui.list

import com.example.sampleproject.di.module.createOrReUseViewModel
import com.example.sampleproject.di.scope.FragmentScope
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object ListFragmentModule {

    @Provides
    @FragmentScope
    fun provideListViewModel(
        fragment: ListFragment,
        provider: Provider<ListViewModelImpl>,
    ): ListViewModel {
        return createOrReUseViewModel(fragment, provider)
    }
}