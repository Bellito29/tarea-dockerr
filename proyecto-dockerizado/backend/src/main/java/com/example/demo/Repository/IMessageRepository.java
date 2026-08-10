package com.example.demo.Repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.example.demo.domain.Message;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Integer>{
    
    @Override
    @EntityGraph(attributePaths = {"sender", "reciever"})
    public List<Message> findAll();
    public Optional<Message> findById(Integer Id);
    public void deleteById(Integer Id);

}
