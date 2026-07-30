package codecluster.problemsubmission.dto;

import java.util.UUID;

public class ExecuteCodeDto {
    private String program;
    private Short programmingLanguageId;
    private UUID userId;

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

}
