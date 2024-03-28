package zut.ipz.dbproject.constant;

import lombok.Getter;

@Getter
public enum SpecialSign {
    COMMA(","),
    OPEN_BRACKET("("),
    CLOSE_BRACKET(")"),
    BACKTICK("`"),
    SPACE(" ");
    private final String symbol;

    SpecialSign(String sign) {
        this.symbol = sign;
    }
}
