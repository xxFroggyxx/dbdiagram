package zut.ipz.dbproject.parser;

import org.springframework.stereotype.Component;
import zut.ipz.dbproject.table.Field;
import zut.ipz.dbproject.table.Table;

import java.util.Arrays;
import java.util.List;

import static zut.ipz.dbproject.constant.SpecialSign.*;

/**
 * This class contains methods that are used in ParserService.
 *
 */
@Component
public class ParserUtilities {
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

    public String[] removeAllSpecialSigns(String[] lineArray) {
        String[] primaryKeysWithoutSpecialSigns = new String[lineArray.length];

        for (int i = 0; i < lineArray.length; i++) {
            primaryKeysWithoutSpecialSigns[i] = removeAllSpecialSigns(lineArray[i]);
        }

        return primaryKeysWithoutSpecialSigns;
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

    public String removeCommaSign(String line) {
        return line.replace(COMMA.getSymbol(),"");
    }

    public String removeOpenBracketSign(String line) {
        return line.replace(OPEN_BRACKET.getSymbol(),"");
    }

    public String removeCloseBracketSign(String line) {
        return line.replace(CLOSE_BRACKET.getSymbol(),"");
    }

    public String removeBacktickSign(String line) {
        return line.replace(BACKTICK.getSymbol(),"");
    }

    public String[] splitByComma(String line) {
        return line.split(COMMA.getSymbol());
    }

    public String[] splitBySpace(String line) {
        return line.split(SPACE.getSymbol());
    }
}

