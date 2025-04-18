package pl.wsei.pam.lab06

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pl.wsei.pam.lab06.components.ListItem
import pl.wsei.pam.lab06.data.viewmodel.AppViewModelProvider
import pl.wsei.pam.lab06.data.viewmodel.ListViewModel

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: ListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val listUiState by viewModel.listUiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { navController.navigate("form") }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Dodaj",
                    modifier = androidx.compose.ui.Modifier.scale(1.5f)
                )
            }
        },
        topBar = {
            AppTopBar(
                navController = navController,
                title = "List",
                showBackIcon = false,
                route = "form"
            )
        },
        content = { innerPadding ->
            LazyColumn(modifier = androidx.compose.ui.Modifier.padding(innerPadding)) {
                items(items = listUiState.items, key = { it.id }) { item ->
                    ListItem(
                        item = item,
                        onTaskCompleted = { completedTask ->
                            if (!completedTask.isDone) {
                                viewModel.markTaskCompleted(completedTask)
                            }
                        }
                    )
                }
            }
        }
    )
}
