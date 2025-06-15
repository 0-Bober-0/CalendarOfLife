package com.calendar.telegrambot;

import com.calendar.telegrambot.bot.TelegramBotApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TelegramBotApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TelegramBotApplication.class, args);

		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			TelegramBotApp bot = context.getBean(TelegramBotApp.class);
			botsApi.registerBot(bot);
		} catch (TelegramApiException e) {
			throw new RuntimeException("Failed to register bot", e);
		}
	}
}