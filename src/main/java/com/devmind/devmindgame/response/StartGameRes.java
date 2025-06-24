package com.devmind.devmindgame.response;

import lombok.Data;


import java.time.LocalDateTime;
@Data
public class StartGameRes {
    private String message;
    private String submitUrl;
    private String question;
    private LocalDateTime timeStarted;
}