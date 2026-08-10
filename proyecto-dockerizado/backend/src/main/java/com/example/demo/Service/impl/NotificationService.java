package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.INotificationRepository;

import lombok.RequiredArgsConstructor;
import com.example.demo.domain.Notification;
import com.example.demo.service.INotificationService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificationService implements INotificationService{
    private final INotificationRepository notificationRepository;

    public Notification save(Notification notification){
        return notificationRepository.save(notification);
    }
    public List<Notification> findAll(){
        return notificationRepository.findAll();
    }
    public Optional<Notification> findById(Integer id){
        return notificationRepository.findById(id);
    }
    public void deleteById(Integer id){
        notificationRepository.deleteById(id);
    }
}
