package zut.ipz.dbproject.table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@Import({Table.class, Field.class, ForeignKey.class})

class TableTest {

    @Test
    void returns_parsed_table_when_given_valid_sql_file() {
        // given
        String name = "Tabela1";
        List<Field> fields = List.of(new Field("Field", "VARCHAR", true, false, false),
                new Field("AnotherField", "INT", false, true, false),
                new Field("ThirdField", "DATE", false, false, true),
                new Field("FourthField", "VARCHAR", true, true, true));
        List<ForeignKey> foreignKey = List.of(new ForeignKey("Tabela2"), new ForeignKey("Tabela3"));
        Table table = new Table(name, fields, foreignKey);
        String expectedString = """
                Tabela1{
                VARCHAR Field pk
                INT AnotherField fk
                DATE ThirdField uk
                VARCHAR FourthField pk,fk,uk
                }
                Tabela1 ||--{ Tabela2: " "
                Tabela1 ||--{ Tabela3: " "
                """;
        // when
        String actualString = table.toString();

        // then

        assert expectedString.equals(actualString);
    }
}
