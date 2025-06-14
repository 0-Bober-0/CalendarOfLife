package com.calendar.server.Kafka;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
public class UserNotification {
    private Long chatId;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();

     public UserNotification(Long chatId, String message) {
        this.chatId = chatId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}