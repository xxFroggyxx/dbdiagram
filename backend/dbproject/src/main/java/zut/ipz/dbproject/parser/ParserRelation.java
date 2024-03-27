package zut.ipz.dbproject.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


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
        currentFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[RelationConstant.CURRENT_FIELD_NAME.getIndex()]);

        referencedFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[RelationConstant.REFERENCED_FIELD_NAME.getIndex()]);
        referencedTableName = parserUtilities.removeAllSpecialSigns(lineInfo[RelationConstant.REFERENCED_TABLE_NAME.getIndex()]);

    }

    private void assignComponents(String[] lineInfo, int skip) {
        currentFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[RelationConstant.CURRENT_FIELD_NAME.getIndex() + skip]);

        referencedFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[RelationConstant.REFERENCED_FIELD_NAME.getIndex() + skip]);
        referencedTableName = parserUtilities.removeAllSpecialSigns(lineInfo[RelationConstant.REFERENCED_TABLE_NAME.getIndex() + skip]);
    }

    @Getter
    enum RelationConstant {
        REFERENCED_FIELD_NAME(5),
        REFERENCED_TABLE_NAME(4),
        CURRENT_FIELD_NAME(2)
        ;

        private final int index;

        RelationConstant(int index) {
            this.index = index;
        }
    }
}