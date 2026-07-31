package codecluster.problemsubmission.util;

import codecluster.problemsubmission.dto.TestCaseResponseDto;

import java.util.List;
///  This is used to get code execution result from Test.test()
public class CodeExecutionResult {
    private int noOfPassedTestCases;
    private boolean isSuccessful;
    private List<TestCaseResponseDto> testcases;
    private String message;
    private int memoryUsedInKb;
    private int executionTimeInMs;
    private String compilerOutputString;
    /// -1 : CompileTime Error,
    /// -2 : Runtime Exception,
    /// -3 : Wrong Answer,
    /// -4: Time Limit Exceeded,
    /// -5 : Memory Limit Exceeded
    /// 1 : Accepted
    private int codeClusterErrorCode;

    public int getNoOfPassedTestCases() {
        return noOfPassedTestCases;
    }

    public void setNoOfPassedTestCases(int noOfPassedTestCases) {
        this.noOfPassedTestCases = noOfPassedTestCases;
    }

    public int getCodeClusterErrorCode() {
        return codeClusterErrorCode;
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

    public int getExecutionTimeInMs() {
        return this.executionTimeInMs;
    }

    public int getMemoryUsedInKb() {
        return memoryUsedInKb;
    }

    public void setMemoryUsedInKb(int memoryUsedInKb) {
        this.memoryUsedInKb = memoryUsedInKb;
    }

    public void setExecutionTimeInMs(int executionTimeInMs) {
        this.executionTimeInMs = executionTimeInMs;
    }

    public void setCodeClusterErrorCode(int codeClusterErrorCode) {
        this.codeClusterErrorCode = codeClusterErrorCode;
    }

    public String getCompilerOutputString() {
        return this.compilerOutputString;
    }

    public void setCompilerOutputString(String compilerOutputString){
        this.compilerOutputString = compilerOutputString;
    }
}
