package zut.ipz.dbproject.formatter;

import java.util.Arrays;

import static zut.ipz.dbproject.constant.SpecialSign.*;
import static zut.ipz.dbproject.constant.SpecialSign.SPACE;

public class LineFormatter {
    public static String[] removeAllSpecialSigns(String[] lineArray) {
        String[] primaryKeysWithoutSpecialSigns = new String[lineArray.length];

        for (int i = 0; i < lineArray.length; i++) {
            primaryKeysWithoutSpecialSigns[i] = removeAllSpecialSigns(lineArray[i]);
        }

        return primaryKeysWithoutSpecialSigns;
    }

    public static String removeAllSpecialSigns(String line) {
        line = removeCommaSign(line);
        line = removeOpenBracketSign(line);
        line = removeCloseBracketSign(line);
        line = removeBacktickSign(line);

        return line;
    }

    public static String[] getLineInformationFrom(String line) {
        String lineWithoutBackticks = removeBacktickSign(line);
        return splitBySpaceAndRemoveEmptyElements(lineWithoutBackticks);
    }

    private static String[] splitBySpaceAndRemoveEmptyElements(String line) {
        return Arrays.stream(splitBySpace(line))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }

    public static String removeCommaSign(String line) {
        return line.replace(COMMA.getSymbol(),"");
    }

    public static String removeOpenBracketSign(String line) {
        return line.replace(OPEN_BRACKET.getSymbol(),"");
    }

    public static String removeCloseBracketSign(String line) {
        return line.replace(CLOSE_BRACKET.getSymbol(),"");
    }

    public static String removeBacktickSign(String line) {
        return line.replace(BACKTICK.getSymbol(),"");
    }

    public static String[] splitByComma(String line) {
        return line.split(COMMA.getSymbol());
    }

    public static String[] splitBySpace(String line) {
        return line.split(SPACE.getSymbol());
    }
}
