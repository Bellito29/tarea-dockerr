package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.MessageRequest;
import com.example.demo.controller.rest.dto.MessageResponse;
import com.example.demo.domain.Message;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface IMessageMapper {

    @Mapping(source = "notification.id", target = "notificationId")
    @Mapping(source = "notification.type", target = "notificationType")
    MessageResponse messageToMessageResponse(Message message);

    List<MessageResponse> messagesToMessagesResponse(List<Message> messages);

    Message messageRequestToMessage(MessageRequest messageRequest);
}
