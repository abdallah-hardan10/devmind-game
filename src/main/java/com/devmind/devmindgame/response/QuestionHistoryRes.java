package com.devmind.devmindgame.response;

import lombok.Data;

@Data
public class QuestionHistoryRes {

    private String question;
    private float playerAnswer;
    private boolean correct;
    private double timeTaken;
}
