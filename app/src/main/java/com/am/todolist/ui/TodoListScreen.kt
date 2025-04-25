package com.am.todolist.ui

import android.R.attr.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.am.todolist.local.TodoEntity
import com.am.todolist.presentation.TodoViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun TodoListSceen(
    viewModel: TodoViewModel = hiltViewModel()
){val todos by viewModel.todos.collectAsState()
    var text by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") }

    val filteredTodos = when (filter) {
        "Active" -> todos.filter { !it.isDone }
        "Completed" -> todos.filter { it.isDone }
        else -> todos
    }

    val screenHeight = LocalDensity.current.run { LocalConfiguration.current.screenHeightDp.dp }


    val bottomPadding = screenHeight * 0.12f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF8F8F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding)
        ) {
            Text(
                text = "My ToDo List",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Enter a new task") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            viewModel.addTodo(TodoEntity(title = text))
                            text = ""
                        }
                    },
                    shape = CircleShape,
                    containerColor = Color.Black,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(filteredTodos) { todo: TodoEntity ->
                    TodoItem(
                        todo = todo,
                        onCheckChange = {
                            viewModel.updateTodo(todo.copy(isDone = !todo.isDone))
                        },
                        onDelete = {
                            viewModel.deleteTodo(todo)
                        }
                    )
                }
            }
        }

        //filter buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f) //
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterButton(text = "All", isSelected = filter == "All") {
                filter = "All"
            }
            FilterButton(text = "Active", isSelected = filter == "Active") {
                filter = "Active"
            }
            FilterButton(text = "Completed", isSelected = filter == "Completed") {
                filter = "Completed"
            }
        }
    }
}


@Composable
fun TodoItem(
    todo: TodoEntity,
    onCheckChange: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { onCheckChange() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = todo.title,
            modifier = Modifier.weight(1f),
            style = if (todo.isDone) {
                TextStyle(textDecoration = TextDecoration.LineThrough)
            } else {
                TextStyle()
            }
        )
        IconButton(onClick = { onDelete() }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Composable
fun FilterButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.LightGray else Color.Transparent,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = null,
    ) {
        Text(text)
    }
}