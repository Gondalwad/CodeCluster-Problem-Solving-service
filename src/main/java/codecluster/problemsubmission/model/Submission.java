package codecluster.problemsubmission.model;

import codecluster.problemsubmission.enums.SubmissionStatus;
import codecluster.problemsubmission.util.SnowflakeId;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @Column(name = "submission_id", nullable = false)
    @SnowflakeId
    private Long submissionId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private CodingQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id")
    private Assessments assessment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "submission_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private SubmissionStatus status;

    @Column(name = "marks_obtained", precision = 5, scale = 2)
    private BigDecimal marksObtained;

    @Column(name = "submitted_at")
    private OffsetDateTime submittedAt;

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public CodingQuestion getQuestion() {
        return question;
    }

    public void setQuestion(CodingQuestion question) {
        this.question = question;
    }

    public Assessments getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessments assessment) {
        this.assessment = assessment;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public BigDecimal getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(BigDecimal marksObtained) {
        this.marksObtained = marksObtained;
    }

    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}