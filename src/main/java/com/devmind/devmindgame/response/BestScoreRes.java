package com.devmind.devmindgame.response;

import lombok.Data;

@Data
public class BestScoreRes {
    private String question;
    private float answer;
    private double timeTaken;
}
