package com.example.sampleproject.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.data.local.dao.TodoDao

@Database(
    entities = [Todo::class],
    version = 1,
    exportSchema = false
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao
}