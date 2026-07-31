package codecluster.problemsubmission.executor;

import codecluster.problemsubmission.dto.TestCaseResponseDto;
import codecluster.problemsubmission.model.TestCase;
import codecluster.problemsubmission.util.CodeExecutionResult;
import org.springframework.stereotype.Component;

import java.util.List;

public interface Container {

    /**
     * Executes the combined user + driver code against all provided test cases.
     *
     * @param userCode   The code submitted by the user/student
     * @param driverCode The harness/driver code that feeds input and captures output
     * @param testCases  The list of test cases (stdin, expected output)
     * @return Aggregated result containing status (ACCEPTED, WRONG_ANSWER, etc.) and test case breakdown
     */
    CodeExecutionResult executeProgram(String userCode, String driverCode, List<TestCase> testCases);

    /**
     * Returns the underlying Docker Container ID
     */
    String getContainerId();
}
