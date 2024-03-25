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
        String[] splittedLine = parserUtilities.splitBySpaceAndRemoveEmptyElements(line);

        if (line.contains("CONSTRAINT")) {
            referencedFieldName = parserUtilities.removeSpecialSigns(splittedLine[7]);
            currentTableFieldName = parserUtilities.removeSpecialSigns(splittedLine[4]);
            referencedTableName = parserUtilities.removeSpecialSigns(splittedLine[6]);
        } else {
            referencedFieldName = parserUtilities.removeSpecialSigns(splittedLine[5]);
            currentTableFieldName = parserUtilities.removeSpecialSigns(splittedLine[2]);
            referencedTableName = parserUtilities.removeSpecialSigns(splittedLine[4]);
        }

    }

}
