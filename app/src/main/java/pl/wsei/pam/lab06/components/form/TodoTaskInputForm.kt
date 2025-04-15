package pl.wsei.pam.lab06.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.wsei.pam.lab06.data.viewmodel.TodoTaskForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTaskInputForm(
    item: TodoTaskForm,
    modifier: Modifier = Modifier,
    onValueChange: (TodoTaskForm) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text("Tytuł zadania")
        TextField(
            value = item.title,
            onValueChange = { onValueChange(item.copy(title = it)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date Picker
        val datePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
            yearRange = IntRange(2023, 2035),
            initialSelectedDateMillis = item.deadline,
        )
        var showDialog by remember { mutableStateOf(false) }

        Text(
            text = "Wybierz datę deadline",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )

        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        datePickerState.selectedDateMillis?.let {
                            onValueChange(item.copy(deadline = it))
                        }
                    }) {
                        Text("Wybierz")
                    }
                }
            ) {
                DatePicker(state = datePickerState, showModeToggle = true)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Status zadania")
        Row {
            Checkbox(
                checked = item.isDone,
                onCheckedChange = { onValueChange(item.copy(isDone = it)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (item.isDone) "Zrobione" else "Nie zrobione")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Priorytet")
        val options = listOf("High", "Medium", "Low")
        options.forEach { option ->
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = item.priority == option,
                    onClick = { onValueChange(item.copy(priority = option)) }
                )
                Text(text = option)
            }
        }
    }
}
