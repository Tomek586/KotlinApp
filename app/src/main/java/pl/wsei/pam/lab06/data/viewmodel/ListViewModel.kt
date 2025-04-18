package pl.wsei.pam.lab06.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.data.model.TodoTask
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository

class ListViewModel(
    private val repository: TodoTaskRepository
) : ViewModel() {

    val listUiState: StateFlow<ListUiState> =
        repository.getAllAsStream()
            .map { ListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = ListUiState()
            )

    fun markTaskCompleted(task: TodoTask) {
        viewModelScope.launch {
            repository.updateItem(task.copy(isDone = true))
        }
    }
}
