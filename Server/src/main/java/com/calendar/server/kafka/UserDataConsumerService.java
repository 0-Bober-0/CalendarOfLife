package com.calendar.server.kafka;

import com.calendar.server.entity.User;
import com.calendar.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class UserDataConsumerService {
    private final UserRepository userRepository;

    public UserDataConsumerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(
            topics = "${kafka.topic.user-data}",
            containerFactory = "userKafkaListenerContainerFactory"
    )
    public void consumeUserData(User user) {
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
    }
}