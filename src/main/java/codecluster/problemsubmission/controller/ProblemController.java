package codecluster.problemsubmission.controller;

import codecluster.problemsubmission.dto.QuestionResponse;
import codecluster.problemsubmission.service.ProblemStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/problems/statement")
public class ProblemController {
    @Autowired
    ProblemStatementService problemStatementService;
    /**
     * This controller returns all the coding questions
     */

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getProblems(){
        return ResponseEntity.ok(problemStatementService.getAllCodingQuestions());
    }
}
