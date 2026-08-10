package com.example.demo.service;

import com.example.demo.domain.Notification;
import java.util.List;
import java.util.Optional;

public interface INotificationService {
    public Notification save(Notification notification);
    public List<Notification> findAll();
    public Optional<Notification> findById(Integer id);
    public void deleteById(Integer id);
}
