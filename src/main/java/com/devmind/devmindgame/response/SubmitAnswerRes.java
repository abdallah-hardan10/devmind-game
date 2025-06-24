package com.devmind.devmindgame.response;

import lombok.Data;

@Data
public class SubmitAnswerRes {
    private String result;
    private double timeTaken;
    private NextQuestionRes nextQuestion;
    private String currentScore;
}
