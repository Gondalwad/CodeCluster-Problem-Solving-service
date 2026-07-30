package codecluster.problemsubmission.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum SubmissionStatus {
    ACCEPTED("accepted", 1),
    COMPILE_TIME_ERROR("compile_time_error", -1),
    RUNTIME_EXCEPTION("runtime_exception", -2),
    WRONG_ANSWER("wrong_answer", -3),
    TIME_LIMIT_EXCEEDED("time_limit_exceeded", -4),
    MEMORY_LIMIT_EXCEEDED("memory_limit_exceeded", -5);
    private final String dbValue;
    private final int code;

    SubmissionStatus(String dbValue, int code) {
        this.dbValue = dbValue;
        this.code = code;
    }

    public String getDbValue() {
        return dbValue;
    }

    public int getCode() {
        return code;
    }

    public static SubmissionStatus fromCode(int code) {
        for (SubmissionStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public static SubmissionStatus fromDbValue(String dbValue) {
        for (SubmissionStatus status : values()) {
            if (status.getDbValue().equals(dbValue)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Unknown database value: " + dbValue);
    }
}

