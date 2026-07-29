package codecluster.problemsubmission.dao;

import codecluster.problemsubmission.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestCasesRepo extends JpaRepository<TestCase, Long> {
    Optional<List<TestCase>> findByCodingQuestionQuestionId(Long questionId);
}
