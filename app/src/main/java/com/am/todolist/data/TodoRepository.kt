package com.am.todolist.data

import com.am.todolist.local.TodoDao
import com.am.todolist.local.TodoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val dao: TodoDao
) {

    fun getTodos(): Flow<List<TodoEntity>> = dao.getTodos()

    suspend fun insertTodo(todo: TodoEntity) {
        dao.insertTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoEntity) {
        dao.deleteTodo(todo)
    }

    suspend fun updateTodo(todo: TodoEntity) {
        dao.updateTodo(todo)
    }
}