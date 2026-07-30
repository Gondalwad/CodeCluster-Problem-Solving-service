package codecluster.problemsubmission.util;

import codecluster.problemsubmission.dto.TestCaseResponseDto;

import java.util.List;

public class CodeExecutionResult {
    private boolean isSuccessful;
    private List<TestCaseResponseDto> testcases;
    private String message;
    /// -1 : CompileTime Error, -2 : Runtime Exception, -3 : TestCaseFailure, 0: Time Limit Exceeded
    /// 1 : Passed
    private int codeClusterErrorCode;

    public int getCodeClusterErrorCode() {
        return codeClusterErrorCode;
    }

    public void setCauseOfFailure(int codeClusterErrorCode) {
        this.codeClusterErrorCode = codeClusterErrorCode;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public List<TestCaseResponseDto> getTestcases() {
        return testcases;
    }

    public void setTestcases(List<TestCaseResponseDto> testcases) {
        this.testcases = testcases;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
