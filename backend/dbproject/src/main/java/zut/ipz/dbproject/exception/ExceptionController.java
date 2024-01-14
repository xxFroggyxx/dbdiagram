package zut.ipz.dbproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.IllegalFormatCodePointException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<String> handleMultipartException(MultipartException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No file provided!");
    }

    @ExceptionHandler(IllegalFormatCodePointException.class)
    protected ResponseEntity<String> handleIllegalFormatCodePointException(IllegalFormatCodePointException e) {
        if (e.getCodePoint() == 0) {
            return ResponseEntity.badRequest().body("No table found");
        } else if (e.getCodePoint() == 1) {
            return ResponseEntity.badRequest().body("No field found");
        } else if (e.getCodePoint() == 2) {
            return ResponseEntity.internalServerError().body("Error while reading file");
        }
        return ResponseEntity.badRequest().body("Unknown error");
    }

}
