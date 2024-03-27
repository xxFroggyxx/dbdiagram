package zut.ipz.dbproject.parser;

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

    public Table findTableByName(List<Table> tables, String name) {
        return tables.stream().
                filter(table -> table.equalsToName(name)).findFirst().orElse(null);
    }

    public Field findFieldInTableBy(Table table, String findBy) {
        String name = removeAllSpecialSigns(findBy);

        return table.getFields().stream().
                filter(field -> field.equalsToName(name))
                .findFirst()
                .orElse(null);
    }

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

    private String[] splitBySpaceAndRemoveEmptyElements(String line) {
        return Arrays.stream(splitBySpace(line))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
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
}
