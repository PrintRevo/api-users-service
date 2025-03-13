package com.printrevo.tech.userservice.platform.notifications.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailNotificationRequest {

    @JsonProperty("content")
    private String content = "content";

    @JsonProperty("delivery_mode")
    private String deliveryMode = "Normal";

    @JsonProperty("recipient")
    private String recipient;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("sender_name")
    private String senderName;

    @JsonProperty("template_name")
    private String templateName;

    @JsonProperty("replacements")
    private List<Replacement> replacements;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Replacement {

        @JsonProperty("placeholder")
        private String placeholder;

        @JsonProperty("value")
        private String value;
    }
}
