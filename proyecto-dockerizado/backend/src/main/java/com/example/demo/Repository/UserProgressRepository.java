package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.UserProgress;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Integer> {
    
    @Modifying
    @Query("UPDATE UserProgress up SET up.user = null WHERE up.user.id IN :userIds")
    void detachUsersFromProgress(@Param("userIds") List<Integer> userIds);
}
