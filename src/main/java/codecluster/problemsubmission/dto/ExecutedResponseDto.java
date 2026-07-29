package codecluster.problemsubmission.dto;

public class ExecutedResponseDto {
    private String error;
    private String totalNoOfTestCases;
    private String passedTestcases;
    private boolean isSuccessful;
    private String errorMessage;


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

    public String getPassedTestcases() {
        return passedTestcases;
    }

    public void setPassedTestcases(String passedTestcases) {
        this.passedTestcases = passedTestcases;
    }

    public String getTotalNoOfTestCases() {
        return totalNoOfTestCases;
    }

    public void setTotalNoOfTestCases(String totalNoOfTestCases) {
        this.totalNoOfTestCases = totalNoOfTestCases;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
