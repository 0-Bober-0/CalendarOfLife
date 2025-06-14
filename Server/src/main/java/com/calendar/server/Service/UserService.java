package com.calendar.server.Service;

import com.calendar.server.Kafka.KafkaProducerService;
import com.calendar.server.entity.User;
import com.calendar.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;



    @Scheduled(cron = "0 0 9 * * MON")
    public void sendWeeklyNotifications() {
        userRepository.findAll().forEach(user -> {
    System.out.println("ChatId: ");
    System.out.println("BirthDate: ");
});
    }
}
