package zut.ipz.dbproject.constant;

import lombok.Getter;

@Getter
public enum TableConstant {
    TABLE_NAME(2),
    PRIMARY_KEY_NAME(2),
    FIELD_NAME(0),
    FIELD_TYPE(1)
    ;

    private final int index;

    TableConstant(int index) {
        this.index = index;
    }
}