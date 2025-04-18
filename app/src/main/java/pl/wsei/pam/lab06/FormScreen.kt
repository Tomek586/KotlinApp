package pl.wsei.pam.lab06

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.data.LocalDateConverter
import pl.wsei.pam.lab06.data.viewmodel.AppViewModelProvider
import pl.wsei.pam.lab06.data.viewmodel.FormViewModel
import pl.wsei.pam.lab06.ui.TodoTaskInputBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    viewModel: FormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                title = "Form",
                showBackIcon = true,
                route = "list",
                onSaveClick = {
                    scope.launch {
                        viewModel.save()

                        val localDate = LocalDateConverter.fromMillis(viewModel.todoTaskUiState.todoTask.deadline)

                        val taskTitle = viewModel.todoTaskUiState.todoTask.title


                        (context as? MainActivity)?.scheduleAlarmForTask(localDate, taskTitle)

                        navController.navigate("list")
                    }
                }
            )
        },
        content = {
            TodoTaskInputBody(
                todoUiState = viewModel.todoTaskUiState,
                onItemValueChange = viewModel::updateUiState,
                modifier = Modifier.padding(it)
            )
        }
    )
}

