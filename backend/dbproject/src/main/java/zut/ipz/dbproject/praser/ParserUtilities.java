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
    public Table findTableByName(List<Table> tables, String name) {
        return tables.stream().
                filter(table -> table.equalsToName(name)).findFirst().orElse(null);
    }

    /**
     * This method finds a field by name.
     *
     * @param findBy is a name of the field
     * @param table is a table
     * @return a field
     */
    public Field findFieldInTableBy(Table table, String findBy) {
        String name = removeAllSpecialSigns(findBy);

        return table.getFields().stream().
                filter(field -> field.equalsToName(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * This method removes special signs from a string.
     * @param line is a string that will be changed
     * @return a string without special signs
     */
    public String removeAllSpecialSigns(String line) {
        line = removeCommaSign(line);
        line = removeOpenBracketSign(line);
        line = removeCloseBracketSign(line);
        line = removeBacktickSign(line);

        return line;
    }

    public String[] getLineInformationFrom(String line) {
        String lineWithoutBackticks = removeBacktickSign(line);
        return splitBySpaceAndRemoveEmptyElements(lineWithoutBackticks);
    }

    private String removeCommaSign(String line) {
        return line.replace(specialSigns.COMMA.getSign(),"");
    }

    private String removeOpenBracketSign(String line) {
        return line.replace(specialSigns.OPEN_BRACKET.getSign(),"");
    }

    private String removeCloseBracketSign(String line) {
        return line.replace(specialSigns.CLOSE_BRACKET.getSign(),"");
    }

    private String removeBacktickSign(String line) {
        return line.replace(specialSigns.BACKTICK.getSign(),"");
    }

    public String[] splitByComma(String line) {
        return line.split(specialSigns.COMMA.getSign());
    }

    public String[] splitBySpace(String line) {
        return line.split(specialSigns.SPACE.getSign());
    }

    /**
     * This method splits a string by space and removes empty elements.
     * @param line is a string that will be split
     * @return a string array
     */
    private String[] splitBySpaceAndRemoveEmptyElements(String line) {
        return Arrays.stream(splitBySpace(line))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }
}
