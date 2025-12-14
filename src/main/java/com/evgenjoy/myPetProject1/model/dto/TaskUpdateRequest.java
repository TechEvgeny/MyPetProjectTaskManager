package com.evgenjoy.myPetProject1.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateRequest {


    @Size(min = 3, max = 100, message = "Строка должна содержать от 3-х до 100 букв!")
    private String title;

    @Size(max = 300, message = "Не более 300 букв!")
    private String description;

    private Boolean completed;

    @Size(min = 1, max = 3, message = "Приоритет должен быть в диапозоне от 1 до 3")
    private Integer priority;

    private LocalDateTime dueDate;
}
