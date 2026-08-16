package codecluster.problemsubmission.service;

import codecluster.problemsubmission.dao.CodingQuestionsRepo;
import codecluster.problemsubmission.dao.QuestionRepository;
import codecluster.problemsubmission.dto.QuestionResponse;
import codecluster.problemsubmission.exception.NoSuchProblemException;
import codecluster.problemsubmission.model.CodingQuestion;
import codecluster.problemsubmission.model.Questions;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemStatementService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CodingQuestionsRepo codingQuestionsRepo;

    public List<QuestionResponse> getAllCodingQuestions() {
        List<CodingQuestion> allQuestions = codingQuestionsRepo.findAll();

        List<Long> questionIds = allQuestions.stream()
                .map(CodingQuestion::getQuestionId)
                .toList();

        List<Questions> questions = questionRepository.findAllById(questionIds);

        List<QuestionResponse> responses = questions.stream()
                .map(question -> new QuestionResponse(
                        question.getQuestionId().toString(),
                        question.getTitle()
                ))
                .toList();

        if(responses == null){
            throw new NoSuchProblemException("No Problems Found");
        }

        return responses;

    }
}
