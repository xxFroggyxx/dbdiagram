package zut.ipz.dbproject.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Table {
    private String name;
    private List<Field> fields;
    private List<ForeignKey> foreignKeys;

    public boolean equalsToName(String name) {
        return this.name.equals(name);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("{\n");
        for (Field field : fields) {
            stringBuilder.append(field.toString()).append("\n");
        }
        stringBuilder.append("}\n");
        for (ForeignKey foreignKey : foreignKeys) {
            stringBuilder.append(foreignKey.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    public Table(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
        this.foreignKeys = new ArrayList<>();
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    public void addForeignKey(ForeignKey foreignKey) {
        this.foreignKeys.add(foreignKey);
    }
}
