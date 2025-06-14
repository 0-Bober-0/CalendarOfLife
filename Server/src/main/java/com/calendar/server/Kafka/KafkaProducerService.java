package com.calendar.server.Kafka;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, UserNotification> kafkaTemplate;

    public void sendUserNotification(Long chatId, String message) {
        UserNotification notification = new UserNotification(chatId, message);
        kafkaTemplate.send("user-notifications", notification);
    }
}
