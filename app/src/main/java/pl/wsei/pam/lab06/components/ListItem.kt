package pl.wsei.pam.lab06.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wsei.pam.lab06.data.model.TodoTask
import java.time.format.DateTimeFormatter

@Composable
fun ListItem(item: TodoTask, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xF5F4F6FF) // jasne tło karty
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Tytuł", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = item.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Deadline", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = item.deadline.format(DateTimeFormatter.ISO_DATE),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Priorytet", fontSize = 12.sp, color = Color.Gray)
            Text(
                text = item.priority.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (item.isDone) Color(0xFF4CAF50) else Color(0xFFD32F2F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.isDone) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = if (item.isDone) "Done" else "Not Done",
                    tint = Color.Black
                )
            }
        }
    }
}
