package com.devmind.devmindgame.service;

import com.devmind.devmindgame.entities.Answer;
import com.devmind.devmindgame.entities.Game;
import com.devmind.devmindgame.entities.Question;
import com.devmind.devmindgame.repository.*;
import com.devmind.devmindgame.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public StartGameRes startNewGame(String name, int difficulty) {
        Random random = new Random();

        if (difficulty == 0) {
            difficulty = random.nextInt(3) + 1;
        }

        Game game = new Game();
        game.setPlayerName(name);
        game.setDifficulty(difficulty);
        game.setTimeStarted(LocalDateTime.now());
        game = gameRepository.save(game);

        Question question = generateQuestion(difficulty);
        question.setGame(game);
        question.setTimeAsked(LocalDateTime.now());
        questionRepository.save(question);

        StartGameRes response = new StartGameRes();
        response.setMessage("Hello " + name + ", find your submit API URL below");
        response.setSubmitUrl("/game/" + game.getId() + "/submit");
        response.setQuestion(question.getEquation());
        response.setTimeStarted(game.getTimeStarted());

        return response;
    }

    public SubmitAnswerRes submitAnswer(Long gameId, float playerAnswer) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        if (game.isEnded()) {
            throw new IllegalStateException("Game already ended.");
        }

        Question lastQuestion = game.getQuestions().stream()
                .filter(q -> q.getAnswer() == null)
                .findFirst()
                .orElseThrow();

        LocalDateTime now = LocalDateTime.now();
        double timeTaken = Duration.between(lastQuestion.getTimeAsked(), now).toMillis() / 1000.0;

        boolean isCorrect = Math.abs(lastQuestion.getCorrectAnswer() - playerAnswer) < 0.01;

        Answer answer = new Answer();
        answer.setPlayerAnswer(playerAnswer);
        answer.setCorrect(isCorrect);
        answer.setTimeTakenInSeconds(timeTaken);
        answer.setTimeSubmitted(now);
        answer.setQuestion(lastQuestion);
        answerRepository.save(answer);

        Question nextQuestion = generateQuestion(game.getDifficulty());
        nextQuestion.setGame(game);
        nextQuestion.setTimeAsked(LocalDateTime.now());
        questionRepository.save(nextQuestion);

        SubmitAnswerRes response = new SubmitAnswerRes();
        response.setResult(isCorrect ?
                "Good job " + game.getPlayerName() + ", your answer is correct!" :
                "Sorry " + game.getPlayerName() + ", your answer is incorrect.");
        response.setTimeTaken(timeTaken);

        NextQuestionRes next = new NextQuestionRes();
        next.setSubmitUrl("/game/" + gameId + "/submit");
        next.setQuestion(nextQuestion.getEquation());
        response.setNextQuestion(next);

        long correctCount = game.getQuestions().stream()
                .filter(q -> q.getAnswer() != null && q.getAnswer().isCorrect())
                .count();
        long total = game.getQuestions().stream()
                .filter(q -> q.getAnswer() != null)
                .count();

        response.setCurrentScore(correctCount + "/" + total);
        return response;
    }

    public EndGameRes endGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        game.setEnded(true);
        gameRepository.save(game);

        List<Question> answeredQuestions = game.getQuestions().stream()
                .filter(q -> q.getAnswer() != null)
                .toList();

        double totalTime = answeredQuestions.stream()
                .mapToDouble(q -> q.getAnswer().getTimeTakenInSeconds())
                .sum();

        long correct = answeredQuestions.stream()
                .filter(q -> q.getAnswer().isCorrect())
                .count();

        Question bestQuestion = answeredQuestions.stream()
                .min(Comparator.comparingDouble(q -> q.getAnswer().getTimeTakenInSeconds()))
                .orElse(null);

        EndGameRes response = new EndGameRes();
        response.setName(game.getPlayerName());
        response.setDifficulty(game.getDifficulty());
        response.setCurrentScore(correct + "/" + answeredQuestions.size());
        response.setTotalTimeSpent(totalTime);

        if (bestQuestion != null) {
            BestScoreRes best = new BestScoreRes();
            best.setQuestion(bestQuestion.getEquation());
            best.setAnswer(bestQuestion.getAnswer().getPlayerAnswer());
            best.setTimeTaken(bestQuestion.getAnswer().getTimeTakenInSeconds());
            response.setBestScoreRes(best);
        }

        List<QuestionHistoryRes> historyList = answeredQuestions.stream().map(q -> {
            QuestionHistoryRes qh = new QuestionHistoryRes();
            qh.setQuestion(q.getEquation());
            qh.setPlayerAnswer(q.getAnswer().getPlayerAnswer());
            qh.setCorrect(q.getAnswer().isCorrect());
            qh.setTimeTaken(q.getAnswer().getTimeTakenInSeconds());
            return qh;
        }).collect(Collectors.toList());

        response.setHistory(historyList);

        return response;
    }

    private Question generateQuestion(int difficulty) {
        Random random = new Random();
        int operandsCount = difficulty + 1;
        int digits = difficulty;

        StringBuilder equation = new StringBuilder();
        float result = 0;
        boolean first = true;

        for (int i = 0; i < operandsCount; i++) {
            int number = (int) (Math.pow(10, digits - 1) + random.nextInt((int) Math.pow(10, digits) - (int) Math.pow(10, digits - 1)));

            if (first) {
                result = number;
                equation.append(number);
                first = false;
            } else {
                char operator = "+-*/^".charAt(random.nextInt(5));
                equation.append(" ").append(operator).append(" ").append(number);
                result = calculate(result, number, operator);
            }
        }

        Question q = new Question();
        q.setEquation(equation.toString());
        q.setCorrectAnswer(result);
        return q;
    }

    private float calculate(float a, float b, char operator) {
        float result = switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> b != 0 ? a / b : 0;
            case '^' -> (float) Math.pow(a, b);
            default -> 0;
        };

        if (Float.isInfinite(result) || Float.isNaN(result)) {
            result = 0;
        }

        return result;
    }
}
