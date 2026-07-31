package codecluster.problemsubmission.aop;

import codecluster.problemsubmission.dto.ErrorResponseDto;
import codecluster.problemsubmission.exception.NoSuchProblemException;
import codecluster.problemsubmission.exception.ProgrammingLanguageNotSupportedException;
import codecluster.problemsubmission.model.ProgrammingLanguage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
    *  This is to handle exception caused during the request and response for running
    *  test cases against specific problem.
    *  They include "programming language not found", problemNotFound
    * */
    @ExceptionHandler(NoSuchProblemException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuchProblemException(NoSuchProblemException e) {
        ErrorResponseDto errorDto = new ErrorResponseDto(
                -1,
                e.getMessage(),
                "Please Select Valid Problem Id!"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ProgrammingLanguageNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuchProblemException(ProgrammingLanguageNotSupportedException e) {
        ErrorResponseDto errorDto = new ErrorResponseDto(
                -1,
                e.getMessage(),
                "Please Select Another Programming Language for this Problem"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> anyRunTimeException(RuntimeException e) {
        e.printStackTrace();
        ErrorResponseDto errorDto = new ErrorResponseDto(
                500,
                e.getMessage(),
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }


}