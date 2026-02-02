package com.evgenjoy.myPetProject1.model.enums;

import lombok.Getter;

public enum Status {
    CREATE(1, "create"),
    IN_PROGRESS(2, "in progress"),
    DONE(3, "done");

    @Getter
    private final String text;
    @Getter
    private final int number;

    Status(int value, String text){
        this.number = value;
        this.text = text;
    }

    public static Status fromValue(int value) {
        for (Status status : values()) {
            if (status.getNumber() == value) {
                return status;
            }
        }
        throw new IllegalStateException("Не верный статус! Должен быть числом - [2 - in progress,  3 - done]");
    }

}
