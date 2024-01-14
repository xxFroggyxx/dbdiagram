package zut.ipz.dbproject.table;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ForeignKey {
    private String referencedTable;
    private boolean isOneToOne;
    private String fieldTableName;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(referencedTable);
        if (isOneToOne) {
            stringBuilder.append(" ||--||");
        } else {
            stringBuilder.append(" ||--|{");
        }
        stringBuilder.append(" ").append(fieldTableName).append(": \" \"");
        return stringBuilder.toString();
    }
}