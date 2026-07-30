package codecluster.problemsubmission.dto;

public class ExecuteCodeDto {
    private String program;
    private Short programmingLanguageId;
    private String userId;

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Short getProgrammingLanguageId() {
        return programmingLanguageId;
    }

    public void setProgrammingLanguageId(Short programmingLanguageId) {
        this.programmingLanguageId = programmingLanguageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
