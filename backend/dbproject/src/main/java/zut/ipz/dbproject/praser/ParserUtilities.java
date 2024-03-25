package zut.ipz.dbproject.praser;

import lombok.Getter;
import org.springframework.stereotype.Component;
import zut.ipz.dbproject.table.Field;
import zut.ipz.dbproject.table.Table;

import java.util.Arrays;
import java.util.List;

/**
 * This class contains methods that are used in ParserService.
 *
 */
@Component
public class ParserUtilities {

    @Getter
    private enum specialSigns {
        COMMA(","),
        OPEN_BRACKET("("),
        CLOSE_BRACKET(")"),
        BACKTICK("`"),
        SPACE(" ");
        private final String sign;

        specialSigns(String sign) {
            this.sign = sign;
        }

    }
/**
     * This method finds a table by name.
     *
     * @param name   is a name of the table
     * @param tables is a list of tables
     * @return a table
     */
    public Table findTableByName(String name, List<Table> tables) {
        return tables.stream().
                filter(table -> table.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * This method finds a field by name.
     *
     * @param name  is a name of the field
     * @param table is a table
     * @return a field
     */
    public Field findFieldByName(String name, Table table) {
        return table.getFields().stream().
                filter(field -> field.getName().equals(name
                        .replace(specialSigns.OPEN_BRACKET.getSign(), "")
                        .replace(specialSigns.CLOSE_BRACKET.getSign(), "")
                        .replace(specialSigns.COMMA.getSign(), ""))).findFirst().orElse(null);
    }

    /**
     * This method removes special signs from a string.
     * @param line is a string that will be changed
     * @return a string without special signs
     */
    public String removeSpecialSigns(String line) {
        return line.replace(specialSigns.COMMA.getSign(), "")
                .replace(specialSigns.OPEN_BRACKET.getSign(), "")
                .replace(specialSigns.CLOSE_BRACKET.getSign(), "")
                .replace(specialSigns.BACKTICK.getSign(), "");
    }
    /**
     * This method splits a string by space and removes empty elements.
     * @param line is a string that will be split
     * @return a string array
     */
    public String[] splitBySpaceAndRemoveEmptyElements(String line) {
//        line = line.replace(specialSigns.BACKTICK.getSign(),"");

        return Arrays.stream(
                line.split(specialSigns.SPACE.getSign()))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }

    public String removeBacktickSign(String line) {
        return line.replace(specialSigns.BACKTICK.getSign(),"");
    }
}
