package com.example.sampleproject.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.sampleproject.data.local.database.TodoDatabase
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import java.io.IOException

@Suppress("UnnecessaryAbstractClass")
abstract class DaoTest<Dao> {

    protected lateinit var db: TodoDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, TodoDatabase::class.java
        ).build()
    }

    abstract fun getDao(db: TodoDatabase): Dao

    protected fun withDao(action: Dao.() -> Unit) = with(getDao(db), action)

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    fun <T : Any> TestObserver<T>.awaitAndAssertValuesOnly(vararg values: T): TestObserver<T> =
        awaitCount(values.size).assertValuesOnly(*values)

    fun <T : Any> TestSubscriber<T>.awaitAndAssertValuesOnly(vararg values: T): TestSubscriber<T> =
        awaitCount(values.size).assertValuesOnly(*values)

    fun <T : Any> TestObserver<T>.awaitSuccessfulTerminalEvent(): TestObserver<T> =
        await().assertNoErrors().assertComplete()
}