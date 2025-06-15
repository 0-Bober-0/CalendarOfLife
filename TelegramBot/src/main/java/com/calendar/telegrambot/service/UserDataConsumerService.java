package com.calendar.telegrambot.service;

import com.calendar.telegrambot.dto.WeekCalculationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDataConsumerService {


    @KafkaListener(
            topics = "week-calculation-results",
            containerFactory = "userKafkaListenerContainerFactory"
    )
    public void consumeUserData(WeekCalculationResult result) {
        System.out.println(result);
    }
}