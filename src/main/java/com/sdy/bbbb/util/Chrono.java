package com.sdy.bbbb.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Chrono {

    public static long dPlus(LocalDateTime dayBefore) {
        return ChronoUnit.DAYS.between(dayBefore, LocalDateTime.now());
    }

    public static long dMinus(LocalDateTime dayAfter) {
        return ChronoUnit.DAYS.between(dayAfter, LocalDateTime.now());
    }

    public static String timesAgo(LocalDateTime dayBefore) {
        long gap = ChronoUnit.MINUTES.between(dayBefore, LocalDateTime.now());
        String word = "";
        if (gap == 0){
            word = "방금 전";
        }else if (gap < 60) {
            word = gap + "분 전";
        }else if (gap < 60 * 24){
            word = (gap / 60) + "시간 전";
        }else if (gap < 60 * 24 * 10) {
            word = (gap / 60 / 24) + "일 전";
        }else if (gap < 60 * 24 * 7 * 5) {
            word = (gap / 60 / 24 / 7) + "주 전";
        }else {
            word = dayBefore.format(DateTimeFormatter.ofPattern("MM월 dd일"));
        }
        return word;
    }

    public static String customForm(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("MM월 dd일"));
    }
}
