package codecluster.problemsubmission.model;

import codecluster.problemsubmission.enums.Difficulty;
import codecluster.problemsubmission.enums.QuestionType;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "questions")
public class Questions {

    @Id
    @Column(name = "question_id")
    private Long questionId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(name = "default_marks", nullable = false)
    private Integer marks;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "created_by_user_id", nullable = false)
    private UUID createdByUserId;

    @Column(name = "created_by_institute_id", nullable = false)
    private UUID createdByInstituteId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;
}
