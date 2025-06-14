package com.calendar.server.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="people")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(unique=true, nullable=false, name="chatId")
    private Long chatId;

    @Size(min=2, max=50, message="Имя должно быть от 2 до 50 символов")
    @Column(name="name")
    private String name;

    @Column(name="birth_date", nullable=false)
    private LocalDate date_of_birth;
}
