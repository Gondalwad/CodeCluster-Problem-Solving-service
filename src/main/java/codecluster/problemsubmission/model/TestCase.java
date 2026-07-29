package codecluster.problemsubmission.model;

import codecluster.problemsubmission.model.CodingQuestion;
import codecluster.problemsubmission.util.SnowflakeId;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "test_cases")
public class TestCase {

    @Id
    @SnowflakeId
    private Long testCaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private CodingQuestion codingQuestion;

    @Column(name = "input", nullable = false, columnDefinition = "TEXT")
    private String input;

    @Column(name = "expected_output", nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(name = "is_sample", nullable = false)
    private Boolean isSample;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "display_order", nullable = false)
    private Short displayOrder;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public Long getTestCaseId() {
        return testCaseId;
    }

    public CodingQuestion getCodingQuestion() {
        return codingQuestion;
    }

    public String getInput() {
        return input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public Boolean getSample() {
        return isSample;
    }

    public Integer getPoints() {
        return points;
    }

    public Short getDisplayOrder() {
        return displayOrder;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setDisplayOrder(Short displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setSample(Boolean sample) {
        isSample = sample;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setCodingQuestion(CodingQuestion codingQuestion) {
        this.codingQuestion = codingQuestion;
    }

    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    @Override
    public String toString() {
        return "TestCases{" +
                "testCaseId:" + testCaseId +
                ", input:'" + input + '\'' +
                ", expectedOutput:'" + expectedOutput + '\'' +
                ", isSample:" + isSample +
                ", points:" + points +
                ", displayOrder:" + displayOrder +
                ", createdAt:" + createdAt +
                '}';
    }
}