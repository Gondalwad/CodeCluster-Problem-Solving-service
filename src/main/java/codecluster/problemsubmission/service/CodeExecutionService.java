package codecluster.problemsubmission.service;

import codecluster.problemsubmission.dao.QuestionRepository;
import codecluster.problemsubmission.dto.ExecuteCodeDto;
import codecluster.problemsubmission.dto.ExecutedResponseDto;
import org.springframework.stereotype.Service;

@Service
public class CodeExecutionService {
    private QuestionRepository questionRepository;

    public ExecutedResponseDto getResult(String problemId, boolean runAll, ExecuteCodeDto requestDto) {

        return null;
    }
}
