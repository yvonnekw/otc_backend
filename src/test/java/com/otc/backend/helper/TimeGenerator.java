package com.otc.backend.helper;

import com.github.javafaker.Faker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeGenerator {

    public static String generateStartAndEndTime() {
        Faker faker = new Faker();

        Date startDate = faker.date().past(1, TimeUnit.DAYS);

        Date endDate = faker.date().future(1, TimeUnit.DAYS, startDate);

        String startTime = formatTime(startDate);
        String endTime = formatTime(endDate);

        return "Start Time: " + startTime + "\nEnd Time: " + endTime;
    }

    private static String formatTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

}
