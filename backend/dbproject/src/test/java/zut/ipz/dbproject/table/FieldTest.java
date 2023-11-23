package zut.ipz.dbproject.table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(Field.class)
class FieldTest {


    @ParameterizedTest
    // Multi parameters for testing
    @CsvSource({
            "VARCHAR, Field, true, false, false, VARCHAR Field pk",
            "INT, AnotherField, false, true, false, INT AnotherField fk",
            "DATE, ThirdField, false, false, true, DATE ThirdField uk",
            "VARCHAR, FourthField, true, true, true, 'VARCHAR FourthField pk,fk,uk'",
    })
     void returns_parsers_field_when_given_attributes_is_valid(
            String type,
            String name,
            boolean isPrimaryKey,
            boolean isForeignKey,
            boolean isUnique,
            String expectedString
    ) {
        // given

            type = type;
            name = name;
            isPrimaryKey = isPrimaryKey;
            isForeignKey = isForeignKey;
            isUnique = isUnique;
            expectedString = expectedString;

        // when

        Field field = new Field(name, type, isPrimaryKey, isForeignKey, isUnique);

        String actualString = field.toString();

        // then

        assert expectedString.equals(actualString);

    }

}
