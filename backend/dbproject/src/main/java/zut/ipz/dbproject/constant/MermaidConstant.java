package zut.ipz.dbproject.constant;

import lombok.Getter;

@Getter
public enum MermaidConstant {
    PRIMARY_KEY("pk"),
    FOREIGN_KEY("fk"),
    UNIQUE("uk")
    ;

    private final String symbol;

    MermaidConstant(String symbol) {
        this.symbol = symbol;
    }
}
