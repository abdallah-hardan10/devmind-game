package com.devmind.devmindgame.response;

import lombok.Data;

import java.util.List;

@Data
public class EndGameRes {
    private String name;
    private int difficulty;
    private String currentScore;
    private double totalTimeSpent;
    private BestScoreRes bestScoreRes;
    private List<QuestionHistoryRes> history;
}
