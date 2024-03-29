package zut.ipz.dbproject.table;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(Field.class)
class FieldTest {
    @ParameterizedTest
    @CsvSource({
            "VARCHAR, Field, true, false, false, VARCHAR Field pk",
            "INT, AnotherField, false, true, false, INT AnotherField fk",
            "DATE, ThirdField, false, false, true, DATE ThirdField uk",
            "VARCHAR, FourthField, true, true, true, 'VARCHAR FourthField pk,fk,uk'",
    })
    void ShouldReturnValidField(
            String type,
            String name,
            boolean isPrimaryKey,
            boolean isForeignKey,
            boolean isUnique,
            String expectedString
    ) {
        // given
        Field field = new Field(name, type, isPrimaryKey, isForeignKey, isUnique);

        // when
        String actualString = field.toString();

        // then
        Assertions.assertThat(expectedString).isEqualTo(actualString);
    }
}
