package com.telusko.quiz_service.service;

import com.telusko.quiz_service.dao.QuizDao;
import com.telusko.quiz_service.entity.QuestionWrapper;
import com.telusko.quiz_service.entity.Quiz;
import com.telusko.quiz_service.entity.Response;
import com.telusko.quiz_service.feign.QuizInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Integer> questions = quizInterface.getQuestionsForQuiz(category,numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
          Quiz quiz = quizDao.findById(id).get();

          List<Integer> questionsIds = quiz.getQuestions();
          quizInterface.getQuestionsFromId(questionsIds);
          ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionsIds);

          return questions;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        return quizInterface.getScore(responses);
    }
}
