package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.UserParticipation;

@Repository
public interface IUserParticipationRepository extends JpaRepository<UserParticipation, Integer> {

    List<UserParticipation> findByUser_Id(Integer userId);
}