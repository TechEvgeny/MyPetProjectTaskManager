package com.evgenjoy.myPetProject1.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class UpdatePriorityRequest {

    @Getter
    @Setter
    @NotNull(message = "Требуется приоритет")
    private int numberPriority;

    @Getter
    @Setter
    @NotNull(message = "Требуется сообщение(commit)")
    private String message;
}
