package com.printrevo.tech.userservice.platform.notifications;

import com.printrevo.tech.userservice.platform.notifications.body.SendEmailNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class DefaultNotificationProvider {

    private final String apiKey;
    private final String notificationServiceUrl;

    public DefaultNotificationProvider(@Value("${providers.notification.api-key}") String apiKey
            , @Value("${uris.root.notification-service}") String notificationServiceUrl) {
        this.apiKey = apiKey;
        this.notificationServiceUrl = notificationServiceUrl;
    }

    public void sendSMS(String phoneNumber, String message) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", apiKey);
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("recipient", phoneNumber);
        reqBody.put("content", message);
        reqBody.put("delivery_mode", "Normal");
        reqBody.put("subject", "DA-REMIT SMS");
        reqBody.put("sender_name", "DA REMIT");
        var requestEntity = new HttpEntity<>(reqBody, headers);
        log.info(requestEntity.toString());
        var response = new RestTemplate().postForEntity(
                String.format("%s/send/sms", notificationServiceUrl), requestEntity, String.class);
        log.info(response.getBody());
        log.info(String.valueOf(response.getBody()));
    }

    public void sendEmail(SendEmailNotificationRequest reqBody) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", apiKey);
        var requestEntity = new HttpEntity<>(reqBody, headers);
        log.info(requestEntity.toString());
        var response = new RestTemplate().postForEntity(
                String.format("%s/send/email", notificationServiceUrl), requestEntity, String.class);
        log.info(response.getBody());
        log.info(String.valueOf(response.getBody()));
    }
}
