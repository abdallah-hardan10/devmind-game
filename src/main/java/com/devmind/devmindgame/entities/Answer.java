package com.devmind.devmindgame.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float playerAnswer;
    private LocalDateTime timeSubmitted;
    private double timeTakenInSeconds;
    private boolean isCorrect;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
