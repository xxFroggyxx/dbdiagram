package zut.ipz.dbproject.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import zut.ipz.dbproject.formatter.LineFormatter;

import static zut.ipz.dbproject.constant.RelationConstant.*;


@AllArgsConstructor
@NoArgsConstructor
@Component
@Getter
public class Relation {
    private String referencedFieldName;
    private String currentFieldName;
    private String referencedTableName;
    private String currentTableName;
    private String relationLine;

    public Relation(String currentTableName, String line) {
        this.currentTableName = currentTableName;

        createRelationByAssigningComponents(line);
    }

    private void createRelationByAssigningComponents(String line) {
        String[] lineInfo = LineFormatter.getLineInformationFrom(line);

        if (line.contains("CONSTRAINT")) {
            int indexNeededToSkipConstraintWord = 2;
            assignComponents(lineInfo, indexNeededToSkipConstraintWord);
        } else {
            assignComponents(lineInfo);
        }
    }

    private void assignComponents(String[] lineInfo) {
        currentFieldName = LineFormatter.removeAllSpecialSigns(lineInfo[CURRENT_FIELD_NAME.getIndex()]);

        referencedFieldName = LineFormatter.removeAllSpecialSigns(lineInfo[REFERENCED_FIELD_NAME.getIndex()]);
        referencedTableName = LineFormatter.removeAllSpecialSigns(lineInfo[REFERENCED_TABLE_NAME.getIndex()]);

    }

    private void assignComponents(String[] lineInfo, int skip) {
        currentFieldName = LineFormatter.removeAllSpecialSigns(lineInfo[CURRENT_FIELD_NAME.getIndex() + skip]);

        referencedFieldName = LineFormatter.removeAllSpecialSigns(lineInfo[REFERENCED_FIELD_NAME.getIndex() + skip]);
        referencedTableName = LineFormatter.removeAllSpecialSigns(lineInfo[REFERENCED_TABLE_NAME.getIndex() + skip]);
    }
}