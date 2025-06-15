package com.calendar.telegrambot.entity;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class User {
    private int id;

    private Long chat_id;

    @Size(min=2, max=50, message="Имя должно быть от 2 до 50 символов")
    private String name;

    private LocalDate birth_date;
}