package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.NotificationRequest;
import com.example.demo.controller.rest.dto.NotificationResponse;
import com.example.demo.domain.Notification;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface INotificationMapper {

    NotificationResponse notificationToNotificationResponse(Notification notification);

    List<NotificationResponse> notificationsToNotificationsResponse(List<Notification> notifications);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    Notification notificationRequestToNotification(NotificationRequest notificationRequest);
}