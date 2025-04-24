package com.am.todolist

import android.app.Application
import androidx.room.Room
import com.am.todolist.local.TodoDatabase

class MyApplication: Application() {
    lateinit var database: TodoDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo_db"
        ).build()
    }
}