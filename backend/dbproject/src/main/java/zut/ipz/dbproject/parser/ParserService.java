package zut.ipz.dbproject.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
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

import static zut.ipz.dbproject.exception.ExceptionConstant.*;

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

        return new Table(lineInfo[TableConstant.TABLE_NAME.getIndex()]);
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
            addNewTable(table);
            return false;
        }

        if (isForeignKey(line)) {
            String tableName = table.getName();
            addNewRelation(tableName, line);
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

    private static void addNewTable(Table table) {
        tables.add(table);
    }

    private static boolean isForeignKey(String line) {
        return line.contains("FOREIGN KEY");
    }

    private static void addNewRelation(String tableName, String line) {
        relations.add(new ParserRelation(tableName, line));
    }

    private static boolean isPrimaryKeyWithBracket(String line) {
        return line.contains("PRIMARY KEY (");
    }

    private static void parsePrimaryKey(String line, Table table) {
        String[] primaryKeys = getPrimaryKeysFromLine(line);
        String[] primaryKeysWithoutSpecialSigns = parserUtilities.removeAllSpecialSigns(primaryKeys);

        for (String primaryKey : primaryKeysWithoutSpecialSigns) {
            Field field = findPrimaryKeyField(table, primaryKey);
            field.setPrimaryKey(true);
        }
    }

    private static String[] getPrimaryKeysFromLine(String line) {
        String[] lineInfo = parserUtilities.getLineInformationFrom(line);

        String lineOfPrimaryKeys = lineInfo[TableConstant.PRIMARY_KEY_NAME.getIndex()];
        String[] primaryKeys = parserUtilities.splitByComma(lineOfPrimaryKeys);

        return primaryKeys;
    }

    private static Field findPrimaryKeyField(Table table, String primaryKey) {
        Field field = parserUtilities.findFieldInTableBy(table, primaryKey);

        if (fieldNotExists(field)) {
            loggerError(PRIMARY_KEY_NOT_FOUND.getMessage(), PRIMARY_KEY_NOT_FOUND.getErrorCode());
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

        String fieldName = lineInfo[TableConstant.FIELD_NAME.getIndex() + indexNeededToSkipBracket];
        String fieldType = removeComma(lineInfo[TableConstant.FIELD_TYPE.getIndex() + indexNeededToSkipBracket]);

        setNameAndTypeToField(fieldName, fieldType, field);
    }

    private static void setParameters(Field field, String[] lineInfo) {
        String fieldName = lineInfo[TableConstant.FIELD_NAME.getIndex()];
        String fieldType = removeComma(lineInfo[TableConstant.FIELD_TYPE.getIndex()]);

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
        Field currentField = parserUtilities.findFieldInTableBy(currentTable, relation.getCurrentFieldName());
        notNullOrLogError(referencedField);
        notNullOrLogError(currentField);

        boolean isOneToOne = isOneToOneRelation(currentField, referencedField);

        setForeignKeyInCurrentField(currentField);

        return new ForeignKey(relation.getReferencedTableName(), isOneToOne, relation.getCurrentTableName());
    }

    private static void notNullOrLogError(Table table) {
        if (table == null) {
            loggerError(NO_TABLE_FOUND.getMessage(), NO_TABLE_FOUND.getErrorCode());
        }
    }

    private static void notNullOrLogError(Field field) {
        if (field == null) {
            loggerError(NO_FIELD_FOUND.getMessage(), NO_FIELD_FOUND.getErrorCode());
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

    @Getter
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