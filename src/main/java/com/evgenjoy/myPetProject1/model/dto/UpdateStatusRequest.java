package com.evgenjoy.myPetProject1.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class UpdateStatusRequest {

    @Setter
    @Getter
    @NotNull(message = "Требуется статус")
    private int numberStatus;

    @Getter
    @Setter
    @NotNull(message = "Требуется сообщение(commit)")
    private String message;

}
