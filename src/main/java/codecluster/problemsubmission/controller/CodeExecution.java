package codecluster.problemsubmission.controller;

import codecluster.problemsubmission.dto.ExecuteCodeDto;
import codecluster.problemsubmission.dto.ExecutedResponseDto;
import codecluster.problemsubmission.dto.SubmitCodeWithEvaluationDto;
import codecluster.problemsubmission.model.Assessments;
import codecluster.problemsubmission.service.CodeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/problems")
public class CodeExecution {

    @Autowired
    private CodeExecutionService codeExecutionService;

    /** This api tests the user requested code against all the "Sample Testcases".
     * The result of testcases will not be persisted into database.
     * The code will be checked will all the sample testcases even if the testcases are failing.
     */
    @PostMapping("/{problemId}/run")
    public ResponseEntity<ExecutedResponseDto> run(@PathVariable("problemId") Long problemId, @RequestBody ExecuteCodeDto requestDto){

        ExecutedResponseDto responseDto = codeExecutionService.getResult(problemId, requestDto);
        return ResponseEntity.accepted().body(responseDto);
    }

    /**
     * This api tests the user requested code against all the test cases of particular problem.
     * Response will contain all details of number of testcases passed out of total number of testcases.
     * If during the order wise execution of testcases if any testcase failed it will halt the execution of lateral testcases.
     * Submission will be saved to the database even if the failed.
     */
    @PostMapping("/{problemId}/submit")
    public ResponseEntity<ExecutedResponseDto> submit(@PathVariable("problemId") Long problemId, @RequestBody ExecuteCodeDto requestDto){

        SubmitCodeWithEvaluationDto submitCodeWithEvaluationDto = codeExecutionService.executeCodeForEvaluation(problemId, requestDto);
        codeExecutionService.saveToDb(submitCodeWithEvaluationDto);
        ExecutedResponseDto responseDto = codeExecutionService.generateResponseDto(submitCodeWithEvaluationDto.getResult(), submitCodeWithEvaluationDto.getNoOfTestcases());
        return ResponseEntity.accepted().body(responseDto);

    }

    /** This is api is same as /submit api it just write an assessmentId, This is particularly for exam scenarios
     * This api tests the user requested code against all the test cases of particular problem.
     * Response will contain all details of number of testcases passed out of total number of testcases.
     * If during the order wise execution of testcases if any testcase failed it will halt the execution of lateral testcases.
     * Submission will be saved to the database even if the failed.
     */
    @PostMapping("/{assessmentId}/{problemId}/submit")
    public ResponseEntity<ExecutedResponseDto> assessmentSubmission(@PathVariable("assessmentId") Long assessmentId, @PathVariable("problemId") Long problemId, @RequestBody ExecuteCodeDto requestDto){
        SubmitCodeWithEvaluationDto submitCodeWithEvaluationDto = codeExecutionService.executeCodeForEvaluation(problemId, requestDto);
        ///  setting assessmentId and saving the submission
        Assessments assessment = new Assessments();
        assessment.setId(problemId);
        submitCodeWithEvaluationDto.getSubmission().setAssessment(assessment);
        codeExecutionService.saveToDb(submitCodeWithEvaluationDto);
        ExecutedResponseDto responseDto = codeExecutionService.generateResponseDto(submitCodeWithEvaluationDto.getResult(), submitCodeWithEvaluationDto.getNoOfTestcases());

        return ResponseEntity.accepted().body(responseDto);
    }
}
