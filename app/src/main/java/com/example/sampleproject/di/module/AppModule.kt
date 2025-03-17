package com.example.sampleproject.di.module

import android.app.Application
import android.content.Context
import com.example.sampleproject.App
import com.example.sampleproject.data.local.dao.TodoDao
import com.example.sampleproject.data.local.database.TodoDatabase
import androidx.room.Room
import com.example.sampleproject.AppSchedulerProvider
import com.example.sampleproject.SchedulerProvider
import com.example.sampleproject.data.repository.TodoRepository
import com.example.sampleproject.data.repository.TodoRepositoryImpl
import com.example.sampleproject.data.remote.api.TodoApi
import com.example.sampleproject.domain.interactor.TodoInteractor
import com.example.sampleproject.domain.interactor.TodoInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        AppModule.DelegateBindings::class,
        NetworkModule::class
    ]
)
object AppModule {
    @Module
    interface DelegateBindings {
        @Binds
        @Singleton
        fun provideApplication(delegate: App): Application

        @Binds
        @Singleton
        fun provideContext(delegate: Application): Context

        @Binds
        @Singleton
        fun bindSchedulerProvider(impl: AppSchedulerProvider): SchedulerProvider
    }

    @Provides
    fun provideDatabase(context: Context): TodoDatabase {
        return Room.databaseBuilder(context, TodoDatabase::class.java, "todo_app_db").build()
    }

    @Provides
    fun provideTodoDao(db: TodoDatabase): TodoDao = db.getTodoDao()

    @Provides
    fun provideTodoInteractor(todoRepository: TodoRepository): TodoInteractor = TodoInteractorImpl(todoRepository)

    @Provides
    fun provideTodoRepository(todoApi: TodoApi, todoDao: TodoDao): TodoRepository = TodoRepositoryImpl(todoApi, todoDao)
}