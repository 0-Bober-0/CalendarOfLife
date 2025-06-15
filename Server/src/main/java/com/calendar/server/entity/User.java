package com.calendar.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "people")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "chat_id")
    private Long chat_id;

    @Size(min = 2, max = 50)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birth_date;
}