package codecluster.problemsubmission.exception;

import codecluster.problemsubmission.model.ProgrammingLanguage;

public class ProgrammingLanguageNotSupportedException extends RuntimeException {
    public ProgrammingLanguageNotSupportedException(String message){
        super(message);
    }
}
