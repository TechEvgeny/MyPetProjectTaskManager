package com.evgenjoy.myPetProject1.model.enums;

import lombok.Getter;

public enum Priority {
    LOW(1, "low"),
    MEDIUM(2, "medium"),
    HIGH(3, "high");

    @Getter
    private final String text;
    @Getter
    private final int number;

    Priority(int number, String text) {
        this.number = number;
        this.text = text;
    }


    public static Priority valueOf(int value) {
        for (Priority priority : Priority.values()) {
            if (priority.getNumber() == value) {
                return priority;
            }
        }
        throw new IllegalStateException("Не верный приоритет! Должен быть числом - [1 - low, 2 - normal, 3 - high]");
    }

}
