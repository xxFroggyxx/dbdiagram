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

        List<Field> fields = List.of(
                new Field("FirstField", "VARCHAR", true, false, false),
                new Field("SecondField", "INT", false, true, false),
                new Field("ThirdField", "DATE", false, false, true),
                new Field("FourthField", "VARCHAR", true, true, true)
        );

        List<ForeignKey> foreignKeys = List.of(
                new ForeignKey("Tabela2", "Tabela1", false),
                new ForeignKey("Tabela1", "Tabela3", false)
        );

        Table table = new Table(name, fields, foreignKeys);

        String expectedString =
                """
                    Tabela1{
                    VARCHAR FirstField pk
                    INT SecondField fk
                    DATE ThirdField uk
                    VARCHAR FourthField pk,fk,uk
                    }
                    Tabela2 ||--|{ Tabela1: " "
                    Tabela1 ||--|{ Tabela3: " "
                """;
        // when
        String actualString = table.toString();

        // then
        assert expectedString.equals(actualString);
    }
}
