package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Event;

@Repository
public interface IEventRepository extends JpaRepository<Event, Integer>{
    Optional<Event> findById(Integer id);
    List<Event> findAll();
}
