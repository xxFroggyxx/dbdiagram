package zut.ipz.dbproject.constant;

import lombok.Getter;

@Getter
public enum RelationConstant {
    REFERENCED_FIELD_NAME(5),
    REFERENCED_TABLE_NAME(4),
    CURRENT_FIELD_NAME(2)
    ;

    private final int index;

    RelationConstant(int index) {
        this.index = index;
    }
}