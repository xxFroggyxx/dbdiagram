package zut.ipz.dbproject.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import static zut.ipz.dbproject.constant.RelationConstant.*;


@AllArgsConstructor
@NoArgsConstructor
@Component
@Getter
public class ParserRelation {
    private String referencedFieldName;
    private String currentFieldName;
    private String referencedTableName;
    private String currentTableName;
    private String relationLine;

    private static final ParserUtilities parserUtilities = new ParserUtilities();

    public ParserRelation(String currentTableName, String line) {
        this.currentTableName = currentTableName;

        createRelationByAssigningComponents(line);
    }

    private void createRelationByAssigningComponents(String line) {
        String[] lineInfo = parserUtilities.getLineInformationFrom(line);

        if (line.contains("CONSTRAINT")) {
            int indexNeededToSkipConstraintWord = 2;
            assignComponents(lineInfo, indexNeededToSkipConstraintWord);
        } else {
            assignComponents(lineInfo);
        }
    }

    private void assignComponents(String[] lineInfo) {
        currentFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[CURRENT_FIELD_NAME.getIndex()]);

        referencedFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[REFERENCED_FIELD_NAME.getIndex()]);
        referencedTableName = parserUtilities.removeAllSpecialSigns(lineInfo[REFERENCED_TABLE_NAME.getIndex()]);

    }

    private void assignComponents(String[] lineInfo, int skip) {
        currentFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[CURRENT_FIELD_NAME.getIndex() + skip]);

        referencedFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[REFERENCED_FIELD_NAME.getIndex() + skip]);
        referencedTableName = parserUtilities.removeAllSpecialSigns(lineInfo[REFERENCED_TABLE_NAME.getIndex() + skip]);
    }
}