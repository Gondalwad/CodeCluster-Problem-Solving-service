package codecluster.problemsubmission.dto;

public class QuestionResponse {

    private String questionId;

    private String title;


    public QuestionResponse(String questionId, String title) {
        this.questionId = questionId;
        this.title = title;
    }
    public QuestionResponse(){};

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

