package com.example.taskpomodoro.data.local

import com.example.taskpomodoro.domain.dataclasses.PomodoroTask
import com.example.taskpomodoro.domain.enums.TaskImportance
import com.example.taskpomodoro.domain.enums.TaskStatus

object LocalTasksDataProvider {
    val sampleTasks = listOf(
        PomodoroTask(
            text = "Limpiar el piso",
            importance = TaskImportance.LOW,
            status = TaskStatus.DONE
        ),
        PomodoroTask(
            text = "Lavar los platos",
            importance = TaskImportance.MEDIUM,
            status = TaskStatus.UNDONE
        ),
        PomodoroTask(
            text = "Barrer el suelo",
            importance = TaskImportance.HIGH,
            status = TaskStatus.UNDONE
        ),
        PomodoroTask(
            text = "Completar el código",
            importance = TaskImportance.VERY_HIGH,
            status = TaskStatus.DONE
        ),
        PomodoroTask(
            text = "Limpiar el piso",
            importance = TaskImportance.LOW,
            status = TaskStatus.DONE
        ),
        PomodoroTask(
            text = "Lavar los platos",
            importance = TaskImportance.MEDIUM,
            status = TaskStatus.UNDONE
        ),
        PomodoroTask(
            text = "Barrer el suelo",
            importance = TaskImportance.HIGH,
            status = TaskStatus.UNDONE
        ),
        PomodoroTask(
            text = "Completar el código",
            importance = TaskImportance.VERY_HIGH,
            status = TaskStatus.DONE
        ),
        PomodoroTask(
            text = "Limpiar el piso",
            importance = TaskImportance.LOW,
            status = TaskStatus.DONE
        ),
        PomodoroTask(
            text = "Lavar los platos",
            importance = TaskImportance.MEDIUM,
            status = TaskStatus.UNDONE
        ),
        PomodoroTask(
            text = "Barrer el suelo",
            importance = TaskImportance.HIGH,
            status = TaskStatus.UNDONE
        ),
        PomodoroTask(
            text = "Completar el código",
            importance = TaskImportance.VERY_HIGH,
            status = TaskStatus.DONE
        )
    )

    fun getUndoneTasks(): List<PomodoroTask> {
        return sampleTasks.filter {
            task -> task.status === TaskStatus.UNDONE
        }
    }
}