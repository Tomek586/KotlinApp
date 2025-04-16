package pl.wsei.pam.lab06

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.wsei.pam.lab06.data.NotificationConfig
import pl.wsei.pam.lab06.data.SettingsPreferenceManager
import pl.wsei.pam.lab06.ui.NumericOutlinedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val prefManager = remember { SettingsPreferenceManager(context) }

    var daysBefore by remember { mutableStateOf(prefManager.getNotificationConfig().daysBefore.toString()) }
    var hoursBefore by remember { mutableStateOf(prefManager.getNotificationConfig().hoursBefore.toString()) }
    var repeatCount by remember { mutableStateOf(prefManager.getNotificationConfig().repeatCount.toString()) }
    var savedMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Konfiguracja powiadomień") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Powrót"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NumericOutlinedTextField(
                    value = daysBefore,
                    onValueChange = { daysBefore = it },
                    label = "Ile dni przed terminem"
                )

                NumericOutlinedTextField(
                    value = hoursBefore,
                    onValueChange = { hoursBefore = it },
                    label = "Ile godzin przed terminem"
                )

                NumericOutlinedTextField(
                    value = repeatCount,
                    onValueChange = { repeatCount = it },
                    label = "Ile razy powtarzać powiadomienia"
                )

                Button(
                    onClick = {
                        // Przekształcamy dane na wartości numeryczne
                        val config = NotificationConfig(
                            daysBefore = daysBefore.toIntOrNull() ?: 1,
                            hoursBefore = hoursBefore.toIntOrNull() ?: 0,
                            repeatCount = repeatCount.toIntOrNull() ?: 1
                        )
                        prefManager.saveNotificationConfig(config)
                        savedMessage = "Ustawienia zapisane"
                        navController.popBackStack()
                    },
                    modifier = Modifier.padding()
                ) {
                    Text(text = "Zapisz ustawienia")
                }

                if (savedMessage.isNotEmpty()) {
                    Text(text = savedMessage, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    )
}
