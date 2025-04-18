package pl.wsei.pam.lab06.data.repository

import kotlinx.coroutines.flow.map
import pl.wsei.pam.lab06.data.model.TodoTask
import pl.wsei.pam.lab06.data.model.TodoTaskEntity
import pl.wsei.pam.lab06.data.room.TodoTaskDao


class DatabaseTodoTaskRepository(private val dao: TodoTaskDao) : TodoTaskRepository {

    override fun getAllAsStream() = dao.findAll().map { list ->
        list.map { it.toModel() }
    }

    override fun getItemAsStream(id: Int) = dao.find(id).map { it.toModel() }

    override suspend fun insertItem(item: TodoTask) {
        dao.insertAll(TodoTaskEntity.fromModel(item))
    }

    override suspend fun deleteItem(item: TodoTask) {
        dao.removeById(TodoTaskEntity.fromModel(item))
    }

    override suspend fun updateItem(item: TodoTask) {
        dao.updateItem(TodoTaskEntity.fromModel(item))
    }
    
}
