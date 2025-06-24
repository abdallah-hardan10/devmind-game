package com.devmind.devmindgame.controller;

import com.devmind.devmindgame.response.*;
import com.devmind.devmindgame.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<StartGameRes> startGame(@RequestParam String name, @RequestParam int difficulty) {
        return ResponseEntity.ok(gameService.startNewGame(name, difficulty));
    }

    @PostMapping("/{gameId}/submit")
    public ResponseEntity<SubmitAnswerRes> submitAnswer(@PathVariable Long gameId, @RequestParam float answer) {
        return ResponseEntity.ok(gameService.submitAnswer(gameId, answer));
    }

    @GetMapping("/{gameId}/end")
    public ResponseEntity<EndGameRes> endGame(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.endGame(gameId));
    }
}
