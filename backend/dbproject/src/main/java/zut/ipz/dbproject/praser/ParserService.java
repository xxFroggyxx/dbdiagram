package zut.ipz.dbproject.praser;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zut.ipz.dbproject.file.FileUtils;
import zut.ipz.dbproject.table.Field;
import zut.ipz.dbproject.table.ForeignKey;
import zut.ipz.dbproject.table.Table;

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
    private static List<ParserRelation> relations = new ArrayList<>();
    private static List<Table> tables = new ArrayList<>();
    private static boolean existingTable = false;
    private static StringBuilder output = new StringBuilder();

    /**
     * This method parses a string.
     *
     * @param sqlFile is a file that will be parsed
     * @return a string for mermaid.js
     * @see "https://mermaid.js.org/syntax/entityRelationshipDiagram.html"
     */
    public String parse(MultipartFile sqlFile) {
        cleanBeforeNextSqlData();

        List<String> lines = FileUtils.getLines(sqlFile);

        createTablesFromLines(lines);
        addForeignKeysToTables();
        addAllTablesToOutput();

        return output.toString();
    }

    private static void cleanBeforeNextSqlData() {
        relations = new ArrayList<>();
        tables = new ArrayList<>();
        existingTable = false;
        output = new StringBuilder().append("erDiagram\n");
    }

    private static void createTablesFromLines(List<String> lines) {
        Table table = null;

        for (String line : lines) {
            line = line.trim();

            if (isBeginningOfTable(line)) {
                table = createTableFrom(line);
                continue;
            }

            if (existingTable) {
                parseUntilTableExists(table, line);
            }
        }
    }

    private static boolean isBeginningOfTable(String line) {
        return line.contains("CREATE TABLE");
    }

    private static Table createTableFrom(String line) {
        existingTable = true;
        String[] lineInfo = parserUtilities.getLineInformationFrom(line);

        return new Table(lineInfo[TableConstant.TABLE_NAME.index]);
    }

    private static void parseUntilTableExists(Table table, String line) {
        boolean tableExists = parseFieldsAndRelations(table, line);
        existingTable = tableExists;
    }

    private static boolean parseFieldsAndRelations(Table table, String line) {
        if (containsSingleBracket(line)) {
            return true;
        }

        if (isEndOfTable(line)) {
            tables.add(table);
            return false;
        }

        if (isForeignKey(line)) {
            String tableName = table.getName();
            addNewRelation(line, tableName);
        } else if (isPrimaryKeyWithBracket(line)) {
            parsePrimaryKey(line, table);
        } else {
            Field tempField = createField(line);
            table.addField(tempField);
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

    private static void addNewRelation(String line, String tableName) {
        relations.add(new ParserRelation(line, tableName));
    }

    private static boolean isPrimaryKeyWithBracket(String line) {
        return line.contains("PRIMARY KEY (");
    }

    private static void parsePrimaryKey(String line, Table table) {
        String[] primaryKeys = getPrimaryKeysFromLine(line);
        String[] primaryKeysWithoutSpecialSigns = removeAllSpecialSigns(primaryKeys);

        for (String primaryKey : primaryKeysWithoutSpecialSigns) {
            Field field = findField(table, primaryKey);
            field.setPrimaryKey(true);
        }
    }

    private static String[] getPrimaryKeysFromLine(String line) {
        String[] lineInfo = parserUtilities.getLineInformationFrom(line);

        String lineOfPrimaryKeys = lineInfo[TableConstant.PRIMARY_KEY_NAME.index];
        String[] primaryKeys = parserUtilities.splitByComma(lineOfPrimaryKeys);

        return primaryKeys;
    }

    private static String[] removeAllSpecialSigns(String[] primaryKeys) {
        String[] primaryKeysWithoutSpecialSigns = new String[primaryKeys.length];

        for (int i = 0; i < primaryKeys.length; i++) {
            primaryKeysWithoutSpecialSigns[i] = parserUtilities.removeAllSpecialSigns(primaryKeys[i]);
        }

        return primaryKeysWithoutSpecialSigns;
    }

    private static Field findField(Table table, String primaryKey) {
        Field field = parserUtilities.findFieldInTableBy(table, primaryKey);

        if (fieldNotExists(field)) {
            loggerError("Primary key not found", 1);
        }

        return field;
    }

    private static boolean fieldNotExists(Field field) {
        return field == null;
    }
    private static Field createField(String line) {
        String[] lineInfo = parserUtilities.getLineInformationFrom(line);

        Field field = new Field();
        setNameAndType(field, lineInfo);
        setConstrains(field, line);

        return field;
    }

    private static void setNameAndType(Field field, String[] lineInfo) {
        if (startsWithBracket(lineInfo)) {
            setParametersSkippingBracket(field, lineInfo);
        } else {
            setParameters(field, lineInfo);
        }
    }

    private static boolean startsWithBracket(String[] lineElements) {
        return lineElements[0].contains("(");
    }

    private static void setParametersSkippingBracket(Field field, String[] lineInfo) {
        int indexNeededToSkipBracket = 1;

        String fieldName = lineInfo[TableConstant.FIELD_NAME.index + indexNeededToSkipBracket];
        String fieldType = removeComma(lineInfo[TableConstant.FIELD_TYPE.index + indexNeededToSkipBracket]);

        setNameAndTypeToField(fieldName, fieldType, field);
    }

    private static void setParameters(Field field, String[] lineInfo) {
        String fieldName = lineInfo[TableConstant.FIELD_NAME.index];
        String fieldType = removeComma(lineInfo[TableConstant.FIELD_TYPE.index]);

        setNameAndTypeToField(fieldName, fieldType, field);
    }

    private static String removeComma(String element) {
        return element.replace(",", "");
    }

    private static void setNameAndTypeToField(String fieldName, String fieldType, Field field) {
        field.setName(fieldName);
        field.setType(fieldType);
    }

    private static void setConstrains(Field field, String line) {
        if (isPrimaryKey(line)) {
            field.setPrimaryKey(true);
        }

        if (isUnique(line)) {
            field.setUnique(true);
        }
    }

    public static boolean isPrimaryKey(String line) {
        return line.contains("PRIMARY KEY");
    }

    public static boolean isUnique(String line) {
        return line.contains("UNIQUE");
    }

    private static void addForeignKeysToTables() {
        for (ParserRelation relation : relations) {
            addForeignKeyToTable(relation);
        }
    }

    private static void addForeignKeyToTable(ParserRelation relation) {
        ForeignKey foreignKey = createForeignKey(relation);
        Table table = parserUtilities.findTableByName(tables, relation.getCurrentTableName());
        table.addForeignKey(foreignKey);
    }

    private static ForeignKey createForeignKey(ParserRelation relation) {
        Table referencedTable = parserUtilities.findTableByName(tables, relation.getReferencedTableName());
        Table currentTable = parserUtilities.findTableByName(tables, relation.getCurrentTableName());
        notNullOrLogError(referencedTable);
        notNullOrLogError(currentTable);

        Field referencedField = parserUtilities.findFieldInTableBy(referencedTable, relation.getReferencedFieldName());
        Field currentField = parserUtilities.findFieldInTableBy(currentTable, relation.getCurrentTableFieldName());
        notNullOrLogError(referencedField);
        notNullOrLogError(currentField);

        boolean isOneToOne = isOneToOneRelation(currentField, referencedField);

        setForeignKeyInCurrentField(currentField);

        return new ForeignKey(relation.getReferencedTableName(), isOneToOne, relation.getCurrentTableName());
    }

    private static void notNullOrLogError(Table table) {
        if (table == null) {
            loggerError("Referenced table not found", 0);
        }
    }

    private static void notNullOrLogError(Field field) {
        if (field == null) {
            loggerError("Referenced field not found", 1);
        }
    }

    private static boolean isOneToOneRelation(Field currentField, Field referencedField) {
        return currentField.isPrimaryKey() && referencedField.isPrimaryKey();
    }

    private static void setForeignKeyInCurrentField(Field field) {
        field.setForeignKey(true);
    }

    private static void loggerError(String message, int errorCode) {
        logger.error(message);
        throw new IllegalFormatCodePointException(errorCode);
    }

    private static void addAllTablesToOutput() {
        for (Table table : tables) {
            output.append(table.toString());
        }
    }

    enum TableConstant {
        TABLE_NAME(2),
        PRIMARY_KEY_NAME(2),
        FIELD_NAME(0),
        FIELD_TYPE(1)
        ;

        private final int index;

        TableConstant(int index) {
            this.index = index;
        }
    }
}