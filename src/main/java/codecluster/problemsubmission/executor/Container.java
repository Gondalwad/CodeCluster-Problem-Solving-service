package codecluster.problemsubmission.executor;

import codecluster.problemsubmission.dto.TestCaseResponseDto;
import codecluster.problemsubmission.model.TestCase;
import codecluster.problemsubmission.util.CodeExecutionResult;

import java.util.List;

public interface Container {
    CodeExecutionResult executeProgram(List<TestCase> testCases);
}
