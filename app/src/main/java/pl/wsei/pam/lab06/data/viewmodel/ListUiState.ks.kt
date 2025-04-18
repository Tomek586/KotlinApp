package pl.wsei.pam.lab06.data.viewmodel

import pl.wsei.pam.lab06.data.model.TodoTask

data class ListUiState(
    val items: List<TodoTask> = emptyList()
)
