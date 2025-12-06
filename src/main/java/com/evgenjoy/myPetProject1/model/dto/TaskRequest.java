package com.evgenjoy.myPetProject1.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class TaskRequest {

    @NotBlank(message = "Строка не должна быть пустой!")
    @Size(min = 3, max = 100, message = "Строка должна содержать от 3-х до 100 букв!")
    private String title;

    @Size(max = 300, message = "Не более 300 букв!")
    private String description;

    private Integer priority;

    private LocalDateTime dueDate;

}
