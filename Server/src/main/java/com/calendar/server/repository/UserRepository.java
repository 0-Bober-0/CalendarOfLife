package com.calendar.server.repository;

import com.calendar.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.chat_id = :chatId")
    Optional<User> findByChatId(@Param("chatId") Long chatId);
}