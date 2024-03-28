package zut.ipz.dbproject.table;

import lombok.*;

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
        if (isOneToOne) {
            stringBuilder.append(" ||--||");
        } else {
            stringBuilder.append(" ||--|{");
        }
        stringBuilder.append(" ").append(currentTable).append(": \" \"");
        return stringBuilder.toString();
    }
}