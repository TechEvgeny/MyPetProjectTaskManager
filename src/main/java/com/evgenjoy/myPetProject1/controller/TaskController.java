package com.evgenjoy.myPetProject1.controller;

import com.evgenjoy.myPetProject1.model.dto.TaskRequest;
import com.evgenjoy.myPetProject1.model.dto.TaskResponse;
import com.evgenjoy.myPetProject1.model.dto.TaskUpdateRequest;
import com.evgenjoy.myPetProject1.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Задачи", description = "Управление задачами")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(
            summary = "Получить все задачи",
            description = "Возвращает список всех активных (не удаленных) задач",
            method = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно получен список задач"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<List<TaskResponse>> getTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить одну задачу по id",
            description = "Возвращает задачу по указанному идентификатору",
            method = "Get",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно получена задача"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<TaskResponse> getTask(@PathVariable long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    @Operation(
            summary = "Создание задачи",
            description = "Создает новую задачу с указанными параметрами",
            method = "Post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.createTask(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление задачи",
            description = "Полностью обновляет задачу с указанным id, требует передачи всех полей",
            method = "Put",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
                    @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<TaskResponse> updateTask(@PathVariable long id, @Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(id, taskRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление задачи",
            description = "Полное удаление задачи по переданному идентификатору",
            method = "Delete",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное удаление задачи"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(
            summary = "Мягкое удаление задачи",
            description = "Убирает задачу из списка активных задач, но не удаляет ее полностью",
            method = "Delete",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное мягкое удаление задачи"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<Void> softDeleteTask(@PathVariable long id) {
        taskService.softDeleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Частичное изменение задачи",
            description = "Изменяет переданные поля задачи, поля которые не переданы, остаются без изменений",
            method = "Patch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное изменение задачи"),
                    @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<TaskResponse> patchTask(@PathVariable long id, @Valid @RequestBody TaskUpdateRequest request) {
        return ResponseEntity.ok(taskService.patchTask(id, request));
    }

    @PatchMapping("/{id}/complete")
    @Operation(
            summary = "Помечает задачу выполненной",
            description = "Устанавливает статус задачи как выполненная (completed = true)",
            method = "Patch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача помечена как \"выполнена\""),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<TaskResponse> completeTask(@PathVariable long id) {
        TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest();
        taskUpdateRequest.setCompleted(true);
        return ResponseEntity.ok(taskService.patchTask(id, taskUpdateRequest));
    }

    @PatchMapping("/{id}/uncomplete")
    @Operation(
            summary = "Помечает задачу не выполненной",
            description = "Устанавливает статус задачи как невыполненная (completed = false)",
            method = "Patch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача помечена как \"невыполнена\""),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            })
    public ResponseEntity<TaskResponse> uncompleteTask(@PathVariable Long id) {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setCompleted(false);
        return ResponseEntity.ok(taskService.patchTask(id, request));
    }


}