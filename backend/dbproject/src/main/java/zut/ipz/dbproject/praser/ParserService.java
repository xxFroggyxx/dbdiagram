package zut.ipz.dbproject.praser;

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
import java.util.Arrays;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * This class contains main logic for parsing.
 * This class includes two methods:
 * 1. parse(String input) - parses a string
 * 2. parseSql(MultipartFile file) - changes a file to string and parses it
 */
@Service
public class ParserService {
    private enum DataBaseDataType {
        STRING, INT, FLOAT, DATE, TIME, DATETIME, BOOLEAN, VARCHAR, DOUBLE, DECIMAL, TEXT
    }

    private final Logger logger = LoggerFactory.getLogger(ParserService.class.getName());

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
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(sqlFile.getInputStream()))) {
            lines = br.lines().toList();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        boolean foundCreateTable = false;
        List<Table> tables = new ArrayList<>();
        Table tempTable = null;
        for (String line : lines) {
            line = line.trim();
            if (line.contains("CREATE TABLE")) {
                foundCreateTable = true;
                tempTable = new Table(line.split(" ")[2]);
                continue;
            }
            if (foundCreateTable) {
                if (line.contains("(") && line.split(" ").length == 1) {
                    continue;
                }
                if (line.contains(");")) {
                    foundCreateTable = false;
                    tables.add(tempTable);
                    continue;
                }
                if (line.contains("FOREIGN KEY")) {
                    String[] splittedLine = line.split(" ");
                    String referencedTable = splittedLine[4];
                    boolean isOneToOne = false;


                    Table referencedTableObject = tables.stream().
                            filter(table -> table.getName().equals(referencedTable)).findFirst().orElse(null);
                    if (referencedTableObject == null) {
                        logger.error("Referenced table not found");
                        throw new IllegalFormatCodePointException(0);
                    }

                    Field referencedField = referencedTableObject.getFields().stream().
                            filter(field -> field.getName().equals(splittedLine[5]
                                    .replace("(", "")
                                    .replace(")", "")
                                    .replace(",",""))).

                            findFirst().orElse(null);
                    Field field = tempTable.getFields().stream().
                            filter(field1 -> field1.getName().equals(splittedLine[2]
                                    .replace("(", "")
                                    .replace(")", ""))).
                            findFirst().orElse(null);

                    if (referencedField == null || field == null) {
                        logger.error("Referenced field not found");
                        throw new IllegalFormatCodePointException(0);
                    }
                    if (referencedField.isPrimaryKey() && field.isPrimaryKey()) {
                        isOneToOne = true;
                    }
                    tempTable.addForeignKey(new ForeignKey(referencedTable, isOneToOne, tempTable.getName()));
                    continue;
                }

                boolean isPrimaryKey = false;
                boolean isForeignKey = false;
                boolean isUnique = false;

                String[] splittedLine = line.split(" ");
                splittedLine = Arrays.stream(splittedLine).filter(s -> !s.isEmpty()).toArray(String[]::new);
                String fieldName;
                String fieldType;
                if (splittedLine[0].contains("(")) {
                    fieldName = splittedLine[1];
                    fieldType = splittedLine[2].replace(",", "");

                } else {
                    fieldName = splittedLine[0];
                    fieldType = splittedLine[1].replace(",", "");
                }
                if (line.contains("PRIMARY KEY")) {
                    isPrimaryKey = true;
                }
                if (line.contains("FOREIGN KEY")) {
                    isForeignKey = true;
                }
                if (line.contains("UNIQUE")) {
                    isUnique = true;
                }
                tempTable.addField(new Field(fieldName, fieldType, isPrimaryKey, isForeignKey, isUnique));


            }

        }

        for (Table table : tables) {
            output.append(table.toString());
        }


        return output.toString();
    }


}
