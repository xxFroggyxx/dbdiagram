package zut.ipz.dbproject.constant;

import lombok.Getter;

@Getter
public enum MermaidConstant {
    PRIMARY_KEY("pk"),
    FOREIGN_KEY("fk"),
    UNIQUE("uk"),
    ONE_TO_ONE(" ||--||"),
    ONE_TO_MANY(" ||--|{"),
    EMPTY_RELATION_NAME(": \" \"")
    ;

    private final String symbol;

    MermaidConstant(String symbol) {
        this.symbol = symbol;
    }
}
