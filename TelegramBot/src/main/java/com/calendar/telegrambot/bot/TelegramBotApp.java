package com.calendar.telegrambot.bot;

import com.calendar.telegrambot.config.MultiSessionTelegramBot;
import com.calendar.telegrambot.entity.User;
import com.calendar.telegrambot.service.UserDataProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TelegramBotApp extends MultiSessionTelegramBot {
    private final UserDataProducer userDataProducer;
    private final String kafkaTopic = "user-registration-topic";

    private enum RegistrationState {
        START,
        AWAITING_NAME,
        AWAITING_BIRTH_DATE,
        COMPLETED
    }

    private static class UserSession {
        RegistrationState state;
        User user;

        public UserSession(Long chatId) {
            this.state = RegistrationState.START;
            this.user = new User();
            this.user.setChat_id(chatId);
        }
    }

    private final Map<Long, UserSession> userSessions = new HashMap<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public TelegramBotApp(UserDataProducer userDataProducer, @Value("${telegram.bot.token}") String token) {
        super("Dating_AI_java_bot", token);
        this.userDataProducer = userDataProducer;
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        UserSession session = userSessions.computeIfAbsent(chatId, k -> new UserSession(chatId));

        try {
            switch (session.state) {
                case START:
                    handleStart(chatId, session, messageText);
                    break;
                case AWAITING_NAME:
                    handleNameInput(chatId, session, messageText);
                    break;
                case AWAITING_BIRTH_DATE:
                    handleBirthDateInput(chatId, session, messageText);
                    break;
                case COMPLETED:
                    sendTextMessage(chatId, "Регистрация уже завершена. Ваши данные: " +
                            session.user.getName() + ", " + session.user.getBirth_date());
                    break;
            }
        } catch (Exception e) {
            sendTextMessage(chatId, "Ошибка: " + e.getMessage());
            session.state = RegistrationState.START;
        }
    }

    private void handleStart(Long chatId, UserSession session, String messageText) {
        if ("/start".equalsIgnoreCase(messageText)) {
            sendTextMessage(chatId, "Привет! Давай познакомимся. Как тебя зовут?");
            session.state = RegistrationState.AWAITING_NAME;
        } else {
            sendTextMessage(chatId, "Для начала работы введите /start");
        }
    }

    private void handleNameInput(Long chatId, UserSession session, String name) {
        if (name.length() < 2 || name.length() > 50) {
            sendTextMessage(chatId, "Имя должно быть от 2 до 50 символов. Попробуйте еще раз.");
            return;
        }

        session.user.setName(name);
        sendTextMessage(chatId, "Отлично, " + name + "! Теперь введи свою дату рождения в формате ДД.ММ.ГГГГ");
        session.state = RegistrationState.AWAITING_BIRTH_DATE;
    }

    private void handleBirthDateInput(Long chatId, UserSession session, String dateStr) {
        try {
            LocalDate birthDate = LocalDate.parse(dateStr, dateFormatter);
            validateBirthDate(birthDate);

            session.user.setBirth_date(birthDate);

            userDataProducer.sendUserData(session.user);

            sendTextMessage(chatId, "Спасибо! Ваши данные успешно сохранены.");
            session.state = RegistrationState.COMPLETED;

        } catch (DateTimeParseException e) {
            sendTextMessage(chatId, "Неверный формат даты. Введите дату в формате ДД.ММ.ГГГГ");
        } catch (IllegalArgumentException e) {
            sendTextMessage(chatId, e.getMessage());
        }
    }

    private void validateBirthDate(LocalDate date) throws IllegalArgumentException {
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения не может быть в будущем");
        }
        if (date.isBefore(LocalDate.now().minusYears(120))) {
            throw new IllegalArgumentException("Дата рождения слишком старая");
        }
    }

    private Message sendTextMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        return executeTelegramApiMethod(message);
    }
}