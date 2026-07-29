package codecluster.problemsubmission.dao;

import codecluster.problemsubmission.model.CodingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodingQuestionsRepo extends JpaRepository<CodingQuestion, Long> {
}
