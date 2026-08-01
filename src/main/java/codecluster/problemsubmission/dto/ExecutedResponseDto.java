package codecluster.problemsubmission.dto;

import java.util.List;

public class ExecutedResponseDto {
    private int errorCode;
    private int totalNoOfTestCases;
    private int noOfPassedTestCases;
    private boolean isSuccessful;
    private String errorMessage;
    private List<TestCaseResponseDto> testCases;

    public List<TestCaseResponseDto> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCaseResponseDto> testCases) {
        this.testCases = testCases;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public int getNoOfPassedTestCases() {
        return noOfPassedTestCases;
    }

    public void setNoOfPassedTestCases(int noOfPassedTestCases) {
        this.noOfPassedTestCases = noOfPassedTestCases;
    }

    public int getTotalNoOfTestCases() {
        return totalNoOfTestCases;
    }

    public void setTotalNoOfTestCases(int totalNoOfTestCases) {
        this.totalNoOfTestCases = totalNoOfTestCases;
    }

}
