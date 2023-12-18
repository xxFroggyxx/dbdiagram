package zut.ipz.dbproject.table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    private String name;
    private List<Field> fields;
    private List<ForeignKey> foreignKeys;


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("{\n");
        for (Field field : fields) {
            stringBuilder.append(field.toString()).append("\n");
        }
        stringBuilder.append("}\n");
        for (ForeignKey foreignKey : foreignKeys) {
            stringBuilder.append(name).append(" ||--{ ").
                    append(foreignKey.getReferencedTable()).append(": \" \"" + "\n");
        }
        return stringBuilder.toString();
    }
}
