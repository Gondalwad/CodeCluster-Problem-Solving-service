package codecluster.problemsubmission.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coding_questions")
public class CodingQuestion {
    @Id
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    public Long getId() {
        return questionId;
    }

    public void setId(Long questionId) {
        this.questionId = questionId;
    }


}