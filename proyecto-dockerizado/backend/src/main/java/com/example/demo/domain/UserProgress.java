package com.example.demo.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "progress_date")
    private LocalDateTime progressDate;
    @Column(name = "effort_level")
    private String effortLevel;
    private String notes;
    @Column(name = "past_weight")
    private Integer pastWeight;
    @Column(name = "current_weight")
    private Integer currentWeight;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)
    @JsonIgnoreProperties({"progress", "role", "sesions", "stats", "notifications", "hibernateLazyInitializer"})
    private User user;

}
