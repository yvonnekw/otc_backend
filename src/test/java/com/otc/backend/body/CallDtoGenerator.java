package com.otc.backend.body;

import com.github.javafaker.Faker;
import com.otc.backend.dto.CallDto;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallDtoGenerator {

    private final Faker faker;

    public CallDtoGenerator() {
        this.faker = new Faker();
    }

    public JSONObject makeCallDto(String telephone, String username) throws JSONException {
        String startTime = formatTime(faker.date().past(1, java.util.concurrent.TimeUnit.DAYS));
        String endTime = formatTime(faker.date().future(1, java.util.concurrent.TimeUnit.DAYS));

        return constructRequestBody(telephone, username, startTime, endTime);

    }

    private String formatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

    private JSONObject constructRequestBody(String telephone, String username, String startTime, String endTime) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("startTime", startTime);
        requestBody.put("endTime", endTime);
        requestBody.put("discountForCalls", faker.number().numberBetween(5, 20));
        requestBody.put("username", username);
        requestBody.put("telephone", telephone);
        return requestBody;
    }
}
