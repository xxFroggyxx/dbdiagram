package zut.ipz.dbproject.constant;

import lombok.Getter;

@Getter
public enum SpecialSign {
    COMMA(","),
    OPEN_BRACKET("("),
    CLOSE_BRACKET(")"),
    BACKTICK("`"),
    SPACE(" ");
    private final String sign;

    SpecialSign(String sign) {
        this.sign = sign;
    }
}
