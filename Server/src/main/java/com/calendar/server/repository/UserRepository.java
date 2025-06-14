package com.calendar.server.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByChatId(Long chatId);
}
