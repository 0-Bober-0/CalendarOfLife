package com.calendar.server.service;

import com.calendar.server.entity.User;
import com.calendar.server.kafka.ResultProducer;
import com.calendar.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyNotificationService {

    private final UserRepository userRepository;
    private final ResultProducer resultProducer;

    // Каждый понедельник в 9:00 утра
    @Scheduled(cron = "0 0 12 * * MON")
    public void sendWeeklyNotifications() {
        List<User> users = userRepository.findAll();
        LocalDate now = LocalDate.now();

        users.forEach(user -> {
            long weeks = ChronoUnit.WEEKS.between(user.getBirth_date(), now);
            resultProducer.sendResult(user.getChat_id(), weeks);
        });
    }
}