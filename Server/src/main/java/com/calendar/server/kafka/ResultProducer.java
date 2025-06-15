package com.calendar.server.kafka;

import lombok.RequiredArgsConstructor;
import com.calendar.server.dto.WeekCalculationResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultProducer {
    private final KafkaTemplate<String, WeekCalculationResult> kafkaTemplate;

    public void sendResult(Long chatId, long weeks) {
        WeekCalculationResult result = new WeekCalculationResult(chatId, weeks);
        kafkaTemplate.send("week-calculation-results", result);
    }
}
