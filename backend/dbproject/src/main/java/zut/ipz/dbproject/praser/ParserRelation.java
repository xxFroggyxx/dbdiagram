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
    private String referencedFieldName;
    private String currentTableFieldName;
    private String referencedTableName;
    private String currentTableName;
    private String relationLine;

    private static final ParserUtilities parserUtilities = new ParserUtilities();

    public ParserRelation(String line, String currentTableName) {
        this.currentTableName = currentTableName;
        String[] lineInfo = parserUtilities.getLineInformationFrom(line);

        if (line.contains("CONSTRAINT")) {
            referencedFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[7]);
            currentTableFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[4]);
            referencedTableName = parserUtilities.removeAllSpecialSigns(lineInfo[6]);
        } else {
            referencedFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[5]);
            currentTableFieldName = parserUtilities.removeAllSpecialSigns(lineInfo[2]);
            referencedTableName = parserUtilities.removeAllSpecialSigns(lineInfo[4]);
        }

    }

}
