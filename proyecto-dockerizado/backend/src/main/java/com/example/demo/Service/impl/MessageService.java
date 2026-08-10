package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.IMessageRepository;

import java.util.Optional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.demo.domain.Message;
import com.example.demo.service.IMessageService;

@RequiredArgsConstructor
@Service
public class MessageService implements IMessageService{
    private final IMessageRepository messageRepository;

    public List<Message> findAll(){
        return messageRepository.findAll();
    }
    public Optional<Message> findById(Integer id){
        return messageRepository.findById(id);
    }
    public void deleteById(Integer id){
        messageRepository.deleteById(id);
    }
    public Message save(Message message){
        return messageRepository.save(message);
    }


}
