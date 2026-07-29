package codecluster.problemsubmission.controller;

import codecluster.problemsubmission.dto.ExecuteCodeDto;
import codecluster.problemsubmission.dto.ExecutedResponseDto;
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

    @PostMapping("/{problemId}/run")
    public ResponseEntity<ExecutedResponseDto> run(@PathVariable("problemId") String problemId, @RequestBody ExecuteCodeDto requestDto){
        ExecutedResponseDto responseDto = codeExecutionService.getResult(problemId, false, requestDto);
        return ResponseEntity.accepted().build();
    }
}
