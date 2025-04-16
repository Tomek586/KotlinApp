package pl.wsei.pam.lab06.data

import android.content.Context
import pl.wsei.pam.lab06.data.repository.DatabaseTodoTaskRepository
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository
import pl.wsei.pam.lab06.data.room.AppDatabase
import pl.wsei.pam.lab06.NotificationHandler

interface AppContainer {
    val todoTaskRepository: TodoTaskRepository
    val notificationHandler: NotificationHandler
    val dateProvider: CurrentDateProvider
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val todoTaskRepository: TodoTaskRepository by lazy {
        DatabaseTodoTaskRepository(AppDatabase.getInstance(context).taskDao())
    }

    override val notificationHandler: NotificationHandler by lazy {
        NotificationHandler(context)
    }

    override val dateProvider: CurrentDateProvider by lazy {
        DefaultCurrentDateProvider()
    }
}
