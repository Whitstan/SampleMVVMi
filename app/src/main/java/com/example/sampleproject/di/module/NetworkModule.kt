package com.example.sampleproject.di.module

import com.example.sampleproject.di.TODO_API_URL
import dagger.Module
import dagger.Provides
import java.time.Duration
import javax.inject.Named
import com.example.sampleproject.BuildConfig
import com.example.sampleproject.SchedulerProvider
import com.example.sampleproject.data.remote.api.TodoApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import javax.inject.Singleton
import timber.log.Timber

@Module
object NetworkModule {
    private val TIMEOUT = Duration.ofMinutes(2)

    @Provides
    @Named(TODO_API_URL)
    fun provideEndPointUrl() = "${BuildConfig.TODO_API_URL}/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor {
        Timber.tag("HTTP").v(it)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT)
            .writeTimeout(TIMEOUT)
            .readTimeout(TIMEOUT)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    fun provideRetrofitBuilder(
        moshiConverterFactory: MoshiConverterFactory,
        schedulerProvider: SchedulerProvider,
    ): Retrofit.Builder = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(schedulerProvider.io()))
        .addConverterFactory(moshiConverterFactory)

    @Provides
    @Singleton
    @Suppress("LongParameterList")
    fun provideTodoApiRetrofit(
        @Named(TODO_API_URL) url: String,
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder,
    ): TodoApi =
        retrofitBuilder
            .baseUrl(url)
            .client(okHttpClientBuilder.build())
            .build()
            .create(TodoApi::class.java)
}