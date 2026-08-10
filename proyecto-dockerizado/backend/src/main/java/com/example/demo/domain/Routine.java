package com.example.demo.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import jakarta.persistence.CascadeType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "routine_name")
    private String routineName;
    private String description;
    private String visibility;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ExercisesInRoutine> exercisesInRoutine;

    @ManyToOne
    @JoinColumn(name="sesion_id")
    @JsonIgnoreProperties({"routines", "user", "hibernateLazyInitializer"})
    private Sesion sesion;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    @JsonIgnoreProperties({"sesions", "progress", "role", "notifications", "hibernateLazyInitializer"})
    private User user;

    @ManyToOne
    @JoinColumn(name="trainer_id")
    @JsonIgnoreProperties({"students", "sesions", "progress", "role", "hibernateLazyInitializer"})
    private Trainer trainer;

}
