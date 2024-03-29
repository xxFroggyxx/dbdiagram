package zut.ipz.dbproject.table;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@Import({Table.class, Field.class, ForeignKey.class})
class TableTest {

    @Test
    void shouldReturnValidParsedTable() {

        // given
        String name = "Table1";

        List<Field> fields = List.of(
                new Field("FirstField", "VARCHAR", true, false, false),
                new Field("SecondField", "INT", false, true, false),
                new Field("ThirdField", "DATE", false, false, true),
                new Field("FourthField", "VARCHAR", true, true, true)
        );

        List<ForeignKey> foreignKeys = List.of(
                new ForeignKey("Table1", "Table2", false),
                new ForeignKey("Table3", "Table1", false)
        );

        Table table = new Table(name, fields, foreignKeys);

        String expectedString =
                """
                    Table1{
                    VARCHAR FirstField pk
                    INT SecondField fk
                    DATE ThirdField uk
                    VARCHAR FourthField pk,fk,uk
                    }
                    Table2 ||--|{ Table1: " "
                    Table1 ||--|{ Table3: " "
                    """;

        // when
        String actualString = table.toString();

        // then
        Assertions.assertThat(expectedString).isEqualTo(actualString);
    }
}
