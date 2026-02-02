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

    private Integer status;
    private Integer priority;
    private LocalDateTime dueDate;

}
