package pl.wsei.pam.lab06.data.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import pl.wsei.pam.lab06.data.CurrentDateProvider
import pl.wsei.pam.lab06.data.LocalDateConverter
import pl.wsei.pam.lab06.data.model.TodoTask
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository
import java.time.LocalDate

data class TodoTaskForm(
    val id: Int = 0,
    val title: String = "",
    val deadline: Long = LocalDateConverter.toMillis(LocalDate.now()),
    val isDone: Boolean = false,
    val priority: String = "Low"
)

data class TodoTaskUiState(
    val todoTask: TodoTaskForm = TodoTaskForm(),
    val isValid: Boolean = false
)

fun TodoTask.toTodoTaskUiState(isValid: Boolean = false): TodoTaskUiState =
    TodoTaskUiState(
        todoTask = this.toTodoTaskForm(),
        isValid = isValid
    )

fun TodoTaskForm.toTodoTask(): TodoTask = TodoTask(
    id = id,
    title = title,
    deadline = LocalDateConverter.fromMillis(deadline),
    isDone = isDone,
    priority = pl.wsei.pam.lab06.data.model.Priority.valueOf(priority)
)

fun TodoTask.toTodoTaskForm(): TodoTaskForm = TodoTaskForm(
    id = id,
    title = title,
    deadline = LocalDateConverter.toMillis(deadline),
    isDone = isDone,
    priority = priority.name
)

class FormViewModel(
    private val repository: TodoTaskRepository,
    private val currentDateProvider: CurrentDateProvider
) : ViewModel() {

    var todoTaskUiState by mutableStateOf(TodoTaskUiState())
        private set

    suspend fun save() {
        if (validate()) {
            repository.insertItem(todoTaskUiState.todoTask.toTodoTask())
        }
    }

    fun updateUiState(todoTaskForm: TodoTaskForm) {
        todoTaskUiState = TodoTaskUiState(
            todoTask = todoTaskForm,
            isValid = validate(todoTaskForm)
        )
    }

    private fun validate(uiState: TodoTaskForm = todoTaskUiState.todoTask): Boolean {
        return uiState.title.isNotBlank() &&
                !LocalDateConverter.fromMillis(uiState.deadline).isBefore(currentDateProvider.getCurrentDate())
    }
}
