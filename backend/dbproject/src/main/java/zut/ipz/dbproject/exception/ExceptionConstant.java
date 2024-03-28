package zut.ipz.dbproject.exception;

import lombok.Getter;

@Getter
public enum ExceptionConstant {
    NO_TABLE_FOUND(0, "No table found"),
    NO_FIELD_FOUND(1, "No field found"),
    PRIMARY_KEY_NOT_FOUND(2, "Primary key not found"),
    ERROR_WHILE_READING_FILE(3, "Error while reading file")
    ;

    private final int errorCode;
    private final String message;

    ExceptionConstant(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
