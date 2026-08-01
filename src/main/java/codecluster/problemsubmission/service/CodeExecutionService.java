package codecluster.problemsubmission.service;

import codecluster.problemsubmission.dao.*;
import codecluster.problemsubmission.dto.ExecuteCodeDto;
import codecluster.problemsubmission.dto.ExecutedResponseDto;
import codecluster.problemsubmission.dto.SubmitCodeWithEvaluationDto;
import codecluster.problemsubmission.enums.SubmissionStatus;
import codecluster.problemsubmission.exception.NoSuchProblemException;
import codecluster.problemsubmission.exception.ProgrammingLanguageNotSupportedException;
import codecluster.problemsubmission.executor.Test;
import codecluster.problemsubmission.model.CodeSnippet;
import codecluster.problemsubmission.model.CodeSubmission;
import codecluster.problemsubmission.model.Submission;
import codecluster.problemsubmission.model.TestCase;
import codecluster.problemsubmission.util.CodeExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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
    @Autowired
    private SubmissionRepo submissionRepo;
    @Autowired
    private CodeSubmissionRepo codeSubmissionRepo;

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
        if (snippet.isEmpty() || snippet.get().getDriverCode() == null|| snippet.get().getDriverCode().isBlank()) throw new ProgrammingLanguageNotSupportedException("Programming Language Not Supported");

        CodeExecutionResult result = tester.test(requestDto, testcases, snippet.get());
        /// Preparing Executed Response Dto
        return generateResponseDto(result, testcases.size());

    }


    /// This method evaluates the code against all the testcases return object of SubmitCodeWithEvaluationDto
    public SubmitCodeWithEvaluationDto executeCodeForEvaluation(Long problemId, ExecuteCodeDto requestDto){
        Optional<List<TestCase>> optionalTestCases = testCasesRepo.findByCodingQuestionQuestionId(problemId);
        if(optionalTestCases.isEmpty()){
            throw new NoSuchProblemException("No Such Problem Found!"); /// exception for aop
        }
        /// sorts the testcases according to displayOrder of testcases
        List<TestCase> testcases = optionalTestCases.get().stream().sorted(Comparator.comparing(TestCase::getDisplayOrder)).toList();

        Optional<CodeSnippet>  snippet = codeSnippetRepo.findByCodingQuestionQuestionIdAndProgrammingLanguageLanguageId(problemId, requestDto.getProgrammingLanguageId());
        /// error for aop if not found ( as problem id there this must be because no programming language supported )
        if (snippet.isEmpty()) throw new ProgrammingLanguageNotSupportedException("Programming Language Not Supported");

        CodeExecutionResult result = tester.test(requestDto, testcases, snippet.get());
        /// Saving result to database
        Submission submission = new Submission();
        submission.setQuestion(snippet.get().getCodingQuestion()); // getting coding question from snippet
        submission.setUserId(requestDto.getUserId());
        submission.setSubmittedAt(OffsetDateTime.now());
        submission.setStatus(SubmissionStatus.fromCode(result.getCodeClusterErrorCode())); // gets error code from result
        /// code_submission
        CodeSubmission codeSubmission = new CodeSubmission();
        codeSubmission.setExecutionTimeMs(result.getExecutionTimeInMs());
        codeSubmission.setMemoryUsedKb(result.getMemoryUsedInKb());
        codeSubmission.setSourceCode(requestDto.getProgram());
        codeSubmission.setProgrammingLanguage(snippet.get().getProgrammingLanguage());
        codeSubmission.setCompilerOutput(result.getCompilerOutputString());

        ///  returning this object as this without saving because in case of assessment we also need to save assessmentId
        return new SubmitCodeWithEvaluationDto(codeSubmission, submission, result, testcases.size());
    }

    /// Helper method to form response dto to send to user.
    public ExecutedResponseDto generateResponseDto(CodeExecutionResult result, int noOfTestcases){
        ExecutedResponseDto responseDto = new ExecutedResponseDto();
        responseDto.setTotalNoOfTestCases(noOfTestcases);
        responseDto.setSuccessful(result.isSuccessful());
        responseDto.setErrorMessage(result.getMessage());
        /// using stream to get count of passed testcases
        responseDto.setNoOfPassedTestCases(result.getNoOfPassedTestCases());
        /// setting only sample testcase and testcases which is failed
        responseDto.setTestCases(
                result.getTestcases().stream()
                        .filter(t -> testCasesRepo.findById(t.getTestCaseId())
                                .map(TestCase::getSample) // or tc.isSample()
                                .orElse(false)
                                || !t.isPassed()
                        )
                        .toList() // Converts Stream back to List (use .collect(Collectors.toList()) if on Java < 16)
        );


        return responseDto;
    }

    ///  this method saves the submission to db
    public void saveToDb(SubmitCodeWithEvaluationDto dto) {
        dto.getCodeSubmission().setSubmission(dto.getSubmission());
        codeSubmissionRepo.save(dto.getCodeSubmission());
    }
}
