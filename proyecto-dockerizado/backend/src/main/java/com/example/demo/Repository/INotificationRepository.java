package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.domain.Notification;
import java.util.List;
import java.util.Optional;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Integer>{
    public List<Notification> findAll();
    public Optional<Notification> findById(Integer Id);
    public void deleteById(Integer Id);
}
