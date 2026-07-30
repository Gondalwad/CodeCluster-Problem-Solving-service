package codecluster.problemsubmission.dto;

import codecluster.problemsubmission.model.CodeSubmission;
import codecluster.problemsubmission.model.Submission;
import codecluster.problemsubmission.util.CodeExecutionResult;

public class SubmitCodeWithEvaluationDto {
    private CodeExecutionResult result;
    private Submission submission;
    private int noOfTestcases;
    private CodeSubmission codeSubmission;

    public SubmitCodeWithEvaluationDto(CodeSubmission codeSubmission, Submission submission, CodeExecutionResult result, int noOfTestcases) {
        this.codeSubmission = codeSubmission;
        this.noOfTestcases = noOfTestcases;
        this.submission = submission;
        this.result = result;
    }

    public CodeExecutionResult getResult() {
        return result;
    }

    public void setResult(CodeExecutionResult result) {
        this.result = result;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public int getNoOfTestcases() {
        return noOfTestcases;
    }

    public void setNoOfTestcases(int noOfTestcases) {
        this.noOfTestcases = noOfTestcases;
    }

    public CodeSubmission getCodeSubmission() {
        return codeSubmission;
    }

    public void setCodeSubmission(CodeSubmission codeSubmission) {
        this.codeSubmission = codeSubmission;
    }
}
