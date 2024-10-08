package com.otc.backend.body;

import com.github.javafaker.Faker;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PaymentDtoGenerator {

    public JSONObject generateFakePaymentDto(String extractedInvoiceId) throws JSONException {
        Faker faker = new Faker();

        JSONObject paymentDto = new JSONObject();
        paymentDto.put("paymentDate", formatDate(faker.date().birthday(18, 65)));
        paymentDto.put("fullNameOnPaymentCard", faker.name().fullName());
        paymentDto.put("cardNumber", faker.finance().creditCard());
        paymentDto.put("expiringDate", formatDate(faker.date().future(365, TimeUnit.DAYS)));
        paymentDto.put("issueNumber", faker.number().digits(3));
        paymentDto.put("securityNumber", faker.number().digits(3));

        paymentDto.put("invoiceId", Integer.parseInt(extractedInvoiceId));
        paymentDto.put("status", "Paid");

        return paymentDto;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

}
