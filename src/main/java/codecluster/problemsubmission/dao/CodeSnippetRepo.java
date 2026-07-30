package codecluster.problemsubmission.dao;

import codecluster.problemsubmission.model.CodeSnippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeSnippetRepo extends JpaRepository<CodeSnippet, Long> {
    Optional<CodeSnippet> findByCodingQuestionQuestionIdAndProgrammingLanguageLanguageId(Long problemId, Short programmingLanguageId);
}
