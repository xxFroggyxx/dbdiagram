package zut.ipz.dbproject.table;

import lombok.*;

import static zut.ipz.dbproject.constant.MermaidConstant.*;
import static zut.ipz.dbproject.constant.SpecialSign.SPACE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ForeignKey {
    private String currentTable;
    private String referencedTable;
    private boolean isOneToOne;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(referencedTable);

        appendRelationType(stringBuilder);

        stringBuilder.append(SPACE.getSymbol()).append(currentTable);

        appendRelationName(stringBuilder, EMPTY_RELATION_NAME.getSymbol());

        return stringBuilder.toString();
    }

    private void appendRelationType(StringBuilder stringBuilder) {
        if (isOneToOne) {
            stringBuilder.append(ONE_TO_ONE.getSymbol());
        } else {
            stringBuilder.append(ONE_TO_MANY.getSymbol());
        }
    }

    private void appendRelationName(StringBuilder stringBuilder, String name) {
        stringBuilder.append(name);
    }
}