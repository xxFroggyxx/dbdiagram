package zut.ipz.dbproject.praser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@NoArgsConstructor
@Component
@Getter
public class ParserRelation {
    String referencedFieldName;
    String currentTableFieldName;
    String referencedTableName;
    String currentTableName;
    String relationLine;

    private static final ParserUtilities parserUtilities = new ParserUtilities();

    public ParserRelation(String line, String currentTableName) {
        this.currentTableName = currentTableName;
        String[] lineInfo = parserUtilities.getLineInformationFrom(line);

        if (line.contains("CONSTRAINT")) {
            referencedFieldName = parserUtilities.removeSpecialSigns(lineInfo[7]);
            currentTableFieldName = parserUtilities.removeSpecialSigns(lineInfo[4]);
            referencedTableName = parserUtilities.removeSpecialSigns(lineInfo[6]);
        } else {
            referencedFieldName = parserUtilities.removeSpecialSigns(lineInfo[5]);
            currentTableFieldName = parserUtilities.removeSpecialSigns(lineInfo[2]);
            referencedTableName = parserUtilities.removeSpecialSigns(lineInfo[4]);
        }

    }

}
