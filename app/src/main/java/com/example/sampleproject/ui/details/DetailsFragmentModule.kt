package com.example.sampleproject.ui.details

import com.example.sampleproject.di.module.createOrReUseViewModel
import com.example.sampleproject.di.scope.FragmentScope
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object DetailsFragmentModule {

    @Provides
    @FragmentScope
    fun provideDetailsViewModel(
        fragment: DetailsFragment,
        provider: Provider<DetailsViewModelImpl>,
    ): DetailsViewModel {
        return createOrReUseViewModel(fragment, provider)
    }
}