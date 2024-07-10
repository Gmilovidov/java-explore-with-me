package ru.practicum;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String pattern = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(pattern);
}
