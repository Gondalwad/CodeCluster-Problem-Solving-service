package codecluster.problemsubmission.exception;

public class NoSuchProblemException extends RuntimeException {
    public NoSuchProblemException(String noProgrammingLanguageFound) {
        super(noProgrammingLanguageFound);
    }
}
