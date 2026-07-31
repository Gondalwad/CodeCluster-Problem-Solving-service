package codecluster.problemsubmission.dto;

public class TestCaseResponseDto {
    private Long testCaseId;
    private short displayOrder;
    private String input;
    private String output;
    private String expectedOutput;
    private boolean isPassed;
    private int codeClusterErrorCode;

    public TestCaseResponseDto(Long testCaseId, short displayOrder, String input, String output, String expectedOutput, boolean isPassed, int codeClusterErrorCode) {
        this.testCaseId = testCaseId;
        this.displayOrder = displayOrder;
        this.input = input;
        this.output = output;
        this.expectedOutput = expectedOutput;
        this.isPassed = isPassed;
        this.codeClusterErrorCode = codeClusterErrorCode;
    }

    public TestCaseResponseDto(){};

    public Long getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    public int getCodeClusterErrorCode() {
        return codeClusterErrorCode;
    }

    public void setCodeClusterErrorCode(int codeClusterErrorCode) {
        this.codeClusterErrorCode = codeClusterErrorCode;
    }

    public short getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(short displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }
}
