package com.evgenjoy.myPetProject1.controller;

import com.evgenjoy.myPetProject1.model.dto.TaskRequest;
import com.evgenjoy.myPetProject1.model.dto.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
public class TempTestController {

    @PostMapping("/validate")
    public ResponseEntity<String> testValidation(@Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok("Test validate " + taskRequest.getTitle() +" " + taskRequest.getDescription());

    }
    @GetMapping("/response")
    public ResponseEntity<TaskResponse> testTaskResponse() {
        TaskResponse taskResponse = TaskResponse.builder()
                .id(1L)
                .title("Задача 1")
                .description("Подрорбное описание задачи")
                .completed(false)
                .priority(1)
                .dueDate(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(taskResponse);
    }
}
