package zut.ipz.dbproject.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zut.ipz.dbproject.praser.ParserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(SpringExtension.class)
@Import(ParserService.class)
class ParserServiceTest {

    @Autowired
    private ParserService parserService;

    @BeforeEach
    void setup() {
        ParserService parserService = new ParserService();
    }

    @Test
    void returns_parsed_string_when_given_string_is_valid() {
        // given
        final String givenString = """
                CREATE TABLE CUSTOMER (
                    custNumber VARCHAR(255) PRIMARY KEY,
                    name VARCHAR(255),
                    sector VARCHAR(255)
                );
                                                      
                CREATE TABLE ORDER (
                    orderNumber INT PRIMARY KEY AUTO_INCREMENT,
                    customerCustNumber VARCHAR(255),
                    deliveryAddress VARCHAR(255),
                    FOREIGN KEY (customerCustNumber) REFERENCES CUSTOMER(custNumber)
                );
                                                      
                CREATE TABLE LINE_ITEM (
                    orderNumber INT,
                    productCode VARCHAR(255),
                    quantity INT,
                    pricePerUnit FLOAT,
                    PRIMARY KEY (orderNumber, productCode),
                    FOREIGN KEY (orderNumber) REFERENCES ORDER(orderNumber)
                );
                """;

        // when
        final String actualString = parserService.parse(givenString);

        // then
        assertThat(actualString).isEqualTo("""
                erDiagram
                    CUSTOMER ||--o{ ORDER : places
                    CUSTOMER {
                        string name
                        string custNumber
                        string sector
                    }
                    ORDER ||--|{ LINE-ITEM : contains
                    ORDER {
                        int orderNumber
                        string deliveryAddress
                    }
                    LINE-ITEM {
                        string productCode
                        int quantity
                        float pricePerUnit
                    }
                                """);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "This is not sql",
    })
    void throwing_exception_when_given_string_is_invalid() {

        // given

        final String givenString = "";

        // when

        final Throwable thrown = catchThrowable(() -> parserService.parse(givenString));

        // then

        assertThat(thrown).isNotNull();

    }

    @Test
    void throwing_exception_when_given_string_is_null() {
        // given
        final String givenString = null;

        // when
        final Throwable thrown = catchThrowable(() -> parserService.parse(givenString));

        // then
        assertThat(thrown).isNotNull();
    }


}
