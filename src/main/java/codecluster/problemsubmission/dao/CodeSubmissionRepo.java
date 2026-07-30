package codecluster.problemsubmission.dao;

import codecluster.problemsubmission.model.CodeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeSubmissionRepo extends JpaRepository<CodeSubmission, Long> {
}
