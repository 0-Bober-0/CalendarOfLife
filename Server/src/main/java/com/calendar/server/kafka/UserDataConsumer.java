package com.calendar.server.kafka;

import com.calendar.server.dto.WeekCalculationResult;
import com.calendar.server.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserDataConsumer {
    private final KafkaTemplate<String, WeekCalculationResult> resultTemplate;

    @KafkaListener(topics = "${kafka.topic.user-data}", groupId = "week-calculation-group")
    public void calculateWeeks(User user) {
        long weeks = ChronoUnit.WEEKS.between(
            user.getBirth_date(),
            LocalDate.now()
        );

        WeekCalculationResult result = new WeekCalculationResult(
            user.getChat_id(),
            weeks
        );
        resultTemplate.send("week-calculation-results", result);
    }
}