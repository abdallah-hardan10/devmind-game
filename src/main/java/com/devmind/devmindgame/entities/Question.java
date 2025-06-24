package com.devmind.devmindgame.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String equation;
    private float correctAnswer;
    private LocalDateTime timeAsked;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private Answer answer;
}
