package com.soft.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsAppService {

    @Value("${whatsapp.api.url}")
    private String WHATSAPP_API_URL;

    @Value("${whatsapp.phone.number.id}")
    private String PHONE_NUMBER_ID;

    @Value("${whatsapp.access.token}")
    private String ACCESS_TOKEN;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String phoneNumber, String messageText) {

        String url = WHATSAPP_API_URL + PHONE_NUMBER_ID + "/messages";

        Map<String, Object> message = new HashMap<>();
        message.put("messaging_product", "whatsapp");
        message.put("to", phoneNumber);
        message.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", messageText);

        message.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(message, headers);

        restTemplate.postForEntity(url, entity, String.class);
    }
}
