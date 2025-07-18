package com.calendar.telegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekCalculationResult {
    private Long chatId;
    private long weeksLived;
}
