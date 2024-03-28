package zut.ipz.dbproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.IllegalFormatCodePointException;

import static zut.ipz.dbproject.exception.ExceptionConstant.*;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<String> handleMultipartException(MultipartException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No file provided!");
    }

    @ExceptionHandler(IllegalFormatCodePointException.class)
    protected ResponseEntity<String> handleIllegalFormatCodePointException(IllegalFormatCodePointException e) {
        if (e.getCodePoint() == NO_TABLE_FOUND.getErrorCode()) {
            return ResponseEntity.badRequest().body(NO_TABLE_FOUND.getMessage());
        } else if (e.getCodePoint() == NO_FIELD_FOUND.getErrorCode()) {
            return ResponseEntity.badRequest().body(NO_FIELD_FOUND.getMessage());
        } else if (e.getCodePoint() == PRIMARY_KEY_NOT_FOUND.getErrorCode()) {
            return ResponseEntity.badRequest().body(PRIMARY_KEY_NOT_FOUND.getMessage());
        } else if (e.getCodePoint() == ERROR_WHILE_READING_FILE.getErrorCode()) {
            return ResponseEntity.internalServerError().body(ERROR_WHILE_READING_FILE.getMessage());
        }
        return ResponseEntity.badRequest().body("Unknown error");
    }

}
