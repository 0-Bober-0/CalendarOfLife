package com.calendar.server.kafka;

import com.calendar.server.entity.User;
import com.calendar.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDataConsumerService {
    private final UserRepository userRepository;
    private final ResultProducer resultProducer;


    @KafkaListener(
            topics = "${kafka.topic.user-data}",
            containerFactory = "userKafkaListenerContainerFactory"
    )
    public void consumeUserData(User user) {
        System.out.println("reijftrgjt9rtjgrtjgjrti8jt9jrtjgt98j" + user.toString());
        userRepository.findByChatId(user.getChat_id())
                .ifPresentOrElse(
                        existingUser -> {
                            existingUser.setName(user.getName());
                            existingUser.setBirth_date(user.getBirth_date());
                            userRepository.save(existingUser);
                        },
                        () -> {
                            User newUser = new User();
                            newUser.setChat_id(user.getChat_id());
                            newUser.setName(user.getName());
                            newUser.setBirth_date(user.getBirth_date());
                            userRepository.save(newUser);
                        }
                );
        long weeks = ChronoUnit.WEEKS.between(
            user.getBirth_date(),
            LocalDate.now()
        );
        resultProducer.sendResult(user.getChat_id(), weeks);
    }
}