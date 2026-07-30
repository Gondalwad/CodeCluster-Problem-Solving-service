package codecluster.problemsubmission.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "code_snippets",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_question_language",
                        columnNames = {"question_id", "language_id"}
                )
        }
)
public class CodeSnippet {

    @Id
    @Column(name = "snippet_id", nullable = false)
    private Long snippetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private CodingQuestion codingQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private ProgrammingLanguage programmingLanguage;

    @Column(name = "starter_code", nullable = false, columnDefinition = "TEXT")
    private String starterCode;

    @Column(name = "driver_code", columnDefinition = "TEXT")
    private String driverCode;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // Generate getters, setters, constructors and toString() from IntelliJ.
    // Do NOT include codingQuestion or programmingLanguage in toString()
    // to avoid LazyInitializationException.
}