package codecluster.problemsubmission.service;

import codecluster.problemsubmission.dao.CodeSnippetRepo;
import codecluster.problemsubmission.dao.QuestionRepository;
import codecluster.problemsubmission.dao.TestCasesRepo;
import codecluster.problemsubmission.dto.ExecuteCodeDto;
import codecluster.problemsubmission.dto.ExecutedResponseDto;
import codecluster.problemsubmission.dto.TestCaseResponseDto;
import codecluster.problemsubmission.exception.NoSuchProblemException;
import codecluster.problemsubmission.exception.ProgrammingLanguageNotSupportedException;
import codecluster.problemsubmission.executor.Test;
import codecluster.problemsubmission.model.CodeSnippet;
import codecluster.problemsubmission.model.TestCase;
import codecluster.problemsubmission.util.CodeExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CodeExecutionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestCasesRepo testCasesRepo;

    @Autowired
    Test tester;
    @Autowired
    private CodeSnippetRepo codeSnippetRepo;

    ///    This only executes sample testcases
    public ExecutedResponseDto getResult(Long problemId, ExecuteCodeDto requestDto) {

        Optional<List<TestCase>> optionalTestCases = testCasesRepo.findByCodingQuestionQuestionIdAndIsSampleTrue(problemId);
        if(optionalTestCases.isEmpty()){
            throw new NoSuchProblemException("No Such Problem Found!"); /// exception for aop
        }
        /// sorts the testcases according to displayOrder of testcases
        List<TestCase> testcases = optionalTestCases.get().stream().sorted(Comparator.comparing(TestCase::getDisplayOrder)).toList();

        Optional<CodeSnippet>  snippet = codeSnippetRepo.findByCodingQuestionQuestionIdAndProgrammingLanguageLanguageId(problemId, requestDto.getProgrammingLanguageId());
        /// error for aop if not found ( as problem id there this must be because no programming language supported )
        if (snippet.isEmpty()) throw new ProgrammingLanguageNotSupportedException("Programming Language Not Supported");

        CodeExecutionResult result = tester.test(requestDto, testcases, snippet.get());
        /// Preparing Executed Response Dto
        return generateResponseDto(result, testcases.size());

    }


    /// Helper method to form response dto to send to user
    private ExecutedResponseDto generateResponseDto(CodeExecutionResult result, int noOfTestcases){
        ExecutedResponseDto responseDto = new ExecutedResponseDto();
        responseDto.setTotalNoOfTestCases(noOfTestcases);
        responseDto.setSuccessful(result.isSuccessful());
        responseDto.setFailedTestCase(result.getTestcases());
        responseDto.setErrorMessage(result.isSuccessful() ? null : result.getMessage());
        /// using stream to get count of passed testcases
        responseDto.setNoOfPassedTestCases(
                (int)result.getTestcases().stream()
                        .filter(TestCaseResponseDto::isPassed)
                        .count());

        return responseDto;
    }
}
