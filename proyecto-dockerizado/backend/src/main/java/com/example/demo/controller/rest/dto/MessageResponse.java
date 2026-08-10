package com.example.demo.controller.rest.dto;

import lombok.Data;

@Data
public class MessageResponse {

    private Integer id;
    private String content;

    private Integer notificationId;
    private String notificationType;
}
