package zut.ipz.dbproject.finder;

import org.springframework.stereotype.Component;
import zut.ipz.dbproject.formatter.LineFormatter;
import zut.ipz.dbproject.table.Field;
import zut.ipz.dbproject.table.Table;

import java.util.List;

@Component
public class Finder {
    public static Table findTableByName(List<Table> tables, String name) {
        return tables.stream().
                filter(table -> table.equalsToName(name)).findFirst().orElse(null);
    }

    public static Field findFieldInTableBy(Table table, String findBy) {
        String name = LineFormatter.removeAllSpecialSigns(findBy);

        return table.getFields().stream().
                filter(field -> field.equalsToName(name))
                .findFirst()
                .orElse(null);
    }
}

