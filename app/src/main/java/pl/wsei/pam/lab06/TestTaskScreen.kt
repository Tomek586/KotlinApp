package pl.wsei.pam.lab06

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import android.util.Log

@Composable
fun TestTaskScreen(navController: NavController) {
    // Stan dla tytułu, daty i godziny zadania
    var taskTitle by remember { mutableStateOf("Test Task") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Edytuj zadanie", style = MaterialTheme.typography.headlineMedium)

        // Pole tekstowe na tytuł zadania
        OutlinedTextField(
            value = taskTitle,
            onValueChange = { taskTitle = it },
            label = { Text("Tytuł zadania") },
            modifier = Modifier.fillMaxWidth()
        )

        // Przycisk wyboru daty
        Button(onClick = { showDatePicker = true }) {
            Text(text = "Wybierz datę: $selectedDate")
        }

        // Przycisk wyboru godziny
        Button(onClick = { showTimePicker = true }) {
            Text(text = "Wybierz godzinę: $selectedTime")
        }

        Button(onClick = {
            val dateTime = LocalDateTime.of(selectedDate, selectedTime)
            val alarmTimestamp = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            Log.d("TestTaskScreen", "Ustawiam alarm dla $taskTitle na $alarmTimestamp")
            (context as? MainActivity)?.scheduleAlarm(alarmTimestamp, taskTitle)
        }) {
            Text(text = "Zapisz zadanie i ustaw alarm")
        }
    }

    // Dialog wyboru daty
    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                showDatePicker = false
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        ).show()
    }

    // Dialog wyboru czasu
    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                selectedTime = LocalTime.of(hour, minute)
                showTimePicker = false
            },
            selectedTime.hour,
            selectedTime.minute,
            true
        ).show()
    }
}
