package com.calendar.server.Kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNotification {
    private Long chatId;
    private String message;
    private LocalDateTime timestamp;
}
