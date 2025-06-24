package com.devmind.devmindgame.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;
    private int difficulty;
    private LocalDateTime timeStarted;
    private boolean isEnded = false;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();
}

