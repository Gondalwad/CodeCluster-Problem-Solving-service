package codecluster.problemsubmission.controller;

import codecluster.problemsubmission.dto.ErrorResponseDto;
import codecluster.problemsubmission.dto.ExecuteCodeDto;
import codecluster.problemsubmission.dto.ExecutedResponseDto;
import codecluster.problemsubmission.exception.NoSuchProblemException;
import codecluster.problemsubmission.service.CodeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/problems")
public class CodeExecution {

    @Autowired
    private CodeExecutionService codeExecutionService;

    //    This only executes sample testcases
    @PostMapping("/{problemId}/run")
    public ResponseEntity<ExecutedResponseDto> run(@PathVariable("problemId") Long problemId, @RequestBody ExecuteCodeDto requestDto){

        ExecutedResponseDto responseDto = codeExecutionService.getResult(problemId, requestDto);
        return ResponseEntity.accepted().body(responseDto);
    }
}
