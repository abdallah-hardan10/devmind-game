package com.devmind.devmindgame.response;

import lombok.Data;

@Data
public class NextQuestionRes {
    private String submitUrl;
    private String question;
}
