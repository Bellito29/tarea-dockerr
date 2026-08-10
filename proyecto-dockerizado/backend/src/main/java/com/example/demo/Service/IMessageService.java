package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import com.example.demo.domain.Message;

public interface IMessageService {
    
    public Message save(Message message);
    public List<Message> findAll();
    public Optional<Message> findById(Integer id);
    public void deleteById(Integer id);

}
