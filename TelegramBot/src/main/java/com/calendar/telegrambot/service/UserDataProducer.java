package com.calendar.telegrambot.service;

import com.calendar.telegrambot.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserDataProducer {

    private final KafkaTemplate<String, User> kafkaTemplate;
    private final String topic;

    public UserDataProducer(KafkaTemplate<String, User> kafkaTemplate,
                            @Value("${kafka.topic.user-data}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendUserData(User user) {
        kafkaTemplate.send(topic, user.getChat_id().toString(), user);
    }
}