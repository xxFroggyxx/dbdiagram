package zut.ipz.dbproject.praser;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zut.ipz.dbproject.table.Field;
import zut.ipz.dbproject.table.ForeignKey;
import zut.ipz.dbproject.table.Table;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * This class contains main logic for parsing.
 * This class includes one public method:
 * parse(MultipartFile sqlFile) - parses a string
 */
@Service
@AllArgsConstructor
public class ParserService {
    private static final ParserUtilities parserUtilities = new ParserUtilities();
    private static final Logger logger = LoggerFactory.getLogger(ParserService.class.getName());

    /**
     * This method parses a string.
     *
     * @param sqlFile is a file that will be parsed
     * @return a string for mermaid.js
     * @see "https://mermaid.js.org/syntax/entityRelationshipDiagram.html"
     */


    public String parse(MultipartFile sqlFile) {
        StringBuilder output = new StringBuilder();
        output.append("erDiagram\n");
        List<String> lines = getLines(sqlFile);
        List<ParserRelation> parserRelations = new ArrayList<>();
        boolean foundCreateTable = false;
        List<Table> tables = new ArrayList<>();
        Table tempTable = null;

        for (String line : lines) {
            line = line.trim();
            if (line.contains("CREATE TABLE")) {
                foundCreateTable = true;
                line = ParserService.parserUtilities.removeBacktickSign(line);
                tempTable = new Table(parserUtilities.splitBySpaceAndRemoveEmptyElements(line)[TableConstant.TABLE_NAME.index]);
                continue;
            }
            if (foundCreateTable) {
                foundCreateTable = parseFieldsAndRelations(tempTable, line, tables, parserRelations);
            }

        }

        for (ParserRelation parserRelation : parserRelations) {
            ForeignKey foreignKey = getForeignKey(parserRelation, tables);
            Table table = parserUtilities.findTableByName(parserRelation.currentTableName, tables);
            table.addForeignKey(foreignKey);
        }
        for (Table table : tables) {
            output.append(table.toString());
        }

        return output.toString();
    }

    /**
     * This method reads a file and returns a list of strings.
     *
     * @param sqlFile is a file that will be read
     * @return a list of strings
     */
    private static List<String> getLines(MultipartFile sqlFile) {
        List<String> lines;
        try (var br = new BufferedReader(new InputStreamReader(sqlFile.getInputStream()))) {
            lines = br.lines().toList();

        } catch (IOException e) {
            throw new IllegalFormatCodePointException(2);
        }

//        List<String> lines = null;
//        try (Stream<String> streamLines = Files.lines(Path.of(sqlFile.getName()))) {
//            lines = streamLines.toList();
//        } catch (IOException e) {
//            System.err.println(e.getMessage());
//        }

        return lines;
    }

    private static boolean parseFieldsAndRelations(Table tempTable, String line, List<Table> tables, List<ParserRelation> parserRelations) {
        if (containsSingleBracket(line)) {
            return true;
        }

        if (isEndOfTable(line)) {
            tables.add(tempTable);
            return false;
        }

        if (isForeignKey(line)) {
            parserRelations.add(new ParserRelation(line, tempTable.getName()));
        } else if (isPrimaryKey(line)) {
            parsePrimaryKey(line, tempTable);
        } else {
            Field tempField = getField(line);
            tempTable.addField(tempField);
        }

        return true;
    }

    private static boolean containsSingleBracket(String line) {
        return line.contains("(") && line.split(" ").length == 1;
    }

    private static boolean isEndOfTable(String line) {
        return line.contains(")") && line.contains(";");
    }

    private static boolean isForeignKey(String line) {
        return line.contains("FOREIGN KEY");
    }

    private static boolean isPrimaryKey(String line) {
        return line.contains("PRIMARY KEY (");
    }

    private static void parsePrimaryKey(String line, Table tempTable) {
        line = ParserService.parserUtilities.removeBacktickSign(line);
        String[] splittedLine = parserUtilities.splitBySpaceAndRemoveEmptyElements(line);

        String[] primaryKeys = splittedLine[TableConstant.PRIMARY_KEY_NAME.index].split(",");
        String[] primaryKeysWithoutSpecialSigns = removeAllSpecialSigns(primaryKeys);

        for (String primaryKey : primaryKeysWithoutSpecialSigns) {
            Field field = parserUtilities.findFieldByName(primaryKey, tempTable);
            if (field == null) {
                logger.error("Primary key not found");
                throw new IllegalFormatCodePointException(1);
            }
            field.setPrimaryKey(true);
        }
    }

    private static String[] removeAllSpecialSigns(String[] primaryKeys) {
        String[] primaryKeysWithoutSpecialSigns = new String[primaryKeys.length];

        for (int i = 0; i < primaryKeys.length; i++) {
            primaryKeysWithoutSpecialSigns[i] = parserUtilities.removeSpecialSigns(primaryKeys[i]);
        }

        return primaryKeysWithoutSpecialSigns;
    }

    private static ForeignKey getForeignKey(ParserRelation parserRelation, List<Table> tables) {

        boolean isOneToOne = false;


        Table referencedTable = parserUtilities.findTableByName(parserRelation.getReferencedTableName(), tables);
        Table currentTable = parserUtilities.findTableByName(parserRelation.getCurrentTableName(), tables);
        if (referencedTable == null || currentTable == null) {
            logger.error("Referenced table not found");
            throw new IllegalFormatCodePointException(0);
        }

        Field referencedField = parserUtilities.findFieldByName(parserRelation.referencedFieldName, referencedTable);
        Field field = parserUtilities.findFieldByName(parserRelation.currentTableFieldName, currentTable);

        if (referencedField == null || field == null) {
            logger.error("Referenced field not found");
            throw new IllegalFormatCodePointException(1);
        }
        if (referencedField.isPrimaryKey() && field.isPrimaryKey()) {
            isOneToOne = true;
        }
        field.setForeignKey(true);
        return new ForeignKey(parserRelation.getReferencedTableName(), isOneToOne, parserRelation.currentTableName);

    }

    private static Field getField(String line) {
        String fieldName;
        String fieldType;

        line = ParserService.parserUtilities.removeBacktickSign(line);
        String[] splittedLine = parserUtilities.splitBySpaceAndRemoveEmptyElements(line);

        if (splittedLine[0].contains("(")) {
            fieldName = splittedLine[1];
            fieldType = splittedLine[2].replace(",", "");

        } else {
            fieldName = splittedLine[0];
            fieldType = splittedLine[1].replace(",", "");
        }

        Field tempField = new Field(fieldName, fieldType);
        if (line.contains("PRIMARY KEY")) {
            tempField.setPrimaryKey(true);
        }
        if (line.contains("UNIQUE")) {
            tempField.setUnique(true);
        }
        return tempField;
    }


    enum TableConstant {
        TABLE_NAME(2),
        PRIMARY_KEY_NAME(2)
        ;

        private int index;

        TableConstant(int index) {
            this.index = index;
        }
    }
}
