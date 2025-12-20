package com.evgenjoy.myPetProject1.controller;

import com.evgenjoy.myPetProject1.model.dto.TaskRequest;
import com.evgenjoy.myPetProject1.model.dto.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
@Tag(name = "Тестовый класс", description = "Тест контроллера приложения, тест валидации и ResponseEntity")
public class TempTestController {

    @PostMapping("/validate")
    @Operation(
            summary = "Тест валидации",
            description = "Тестируем валидацию ентити")

    public ResponseEntity<String> testValidation(@Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok("Test validate " + taskRequest.getTitle() +" " + taskRequest.getDescription());

    }
    @GetMapping("/response")
    @Operation(
            summary = "Тест возвращаемого ResponseEntity",
            description = "Тестируем правильность возвращаемого ентити")
    public ResponseEntity<TaskResponse> testTaskResponse() {
        TaskResponse taskResponse = TaskResponse.builder()
                .id(1L)
                .title("Задача 1")
                .description("Подрорбное описание задачи")
                .dueDate(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(taskResponse);
    }
}
