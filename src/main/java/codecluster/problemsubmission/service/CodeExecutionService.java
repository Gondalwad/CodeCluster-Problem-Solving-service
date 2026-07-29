package codecluster.problemsubmission.service;

import codecluster.problemsubmission.dao.QuestionRepository;
import codecluster.problemsubmission.dao.TestCasesRepo;
import codecluster.problemsubmission.dto.ExecuteCodeDto;
import codecluster.problemsubmission.dto.ExecutedResponseDto;
import codecluster.problemsubmission.model.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CodeExecutionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestCasesRepo testCasesRepo;


    public ExecutedResponseDto getResult(Long problemId, boolean runAll, ExecuteCodeDto requestDto) {

        Optional<List<TestCase>> testcases = testCasesRepo.findByCodingQuestionQuestionId(problemId);

        return null;
    }
}
