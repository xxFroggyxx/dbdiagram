package zut.ipz.dbproject.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import static zut.ipz.dbproject.constant.SpecialSign.*;
import static zut.ipz.dbproject.constant.MermaidConstant.*;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Getter
@Setter
public class Field {
    private String name;
    private String type;
    private boolean isPrimaryKey;
    private boolean isForeignKey;
    private boolean isUnique;

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public boolean equalsToName(String name) {
        return this.name.equals(name);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder = appendFieldType(stringBuilder);
        stringBuilder = appendFieldName(stringBuilder);
        stringBuilder = appendConstraints(stringBuilder);

        return stringBuilder.toString();
    }

    private StringBuilder appendFieldType(StringBuilder stringBuilder) {
        return stringBuilder.append(type).append(SPACE.getSymbol());
    }

    private StringBuilder appendFieldName(StringBuilder stringBuilder) {
        return stringBuilder.append(name).append(SPACE.getSymbol());
    }

    private StringBuilder appendConstraints(StringBuilder stringBuilder) {
        boolean isAddedConstraint = false;

        if (isPrimaryKey) {
            isAddedConstraint = true;
            stringBuilder = appendPrimaryKey(stringBuilder);
        }
        if (isForeignKey) {
            isAddedConstraint = true;
            stringBuilder = appendForeignKey(stringBuilder);
        }
        if (isUnique) {
            isAddedConstraint = true;
            stringBuilder = appendUnique(stringBuilder);
        }

        //  If we have comma at the end of the string, we need to remove it,
        //  because it will cause an error in the mermaid query.
        if (isAddedConstraint) {
            deleteCommaAtTheEndOfConstraints(stringBuilder);
        }

        return stringBuilder;
    }

    private StringBuilder appendPrimaryKey(StringBuilder stringBuilder) {
        return stringBuilder.append(PRIMARY_KEY.getSymbol()).append(COMMA.getSymbol());
    }

    private StringBuilder appendForeignKey(StringBuilder stringBuilder) {
        return stringBuilder.append(FOREIGN_KEY.getSymbol()).append(COMMA.getSymbol());
    }

    private StringBuilder appendUnique(StringBuilder stringBuilder) {
        return stringBuilder.append(UNIQUE.getSymbol()).append(COMMA.getSymbol());
    }

    private void deleteCommaAtTheEndOfConstraints(StringBuilder stringBuilder) {
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    }
}