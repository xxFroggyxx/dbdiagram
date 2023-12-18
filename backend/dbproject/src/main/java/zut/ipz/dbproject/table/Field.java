package zut.ipz.dbproject.table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class Field {
    private String name;
    private String type;
    private boolean isPrimaryKey;
    private boolean isForeignKey;
    private boolean isUnique;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type).append(" ");
        stringBuilder.append(name).append(" ");
        boolean isAddedConstrain = false;
        if (isPrimaryKey) {
            isAddedConstrain = true;
            stringBuilder.append("pk").append(",");
        }
        if (isForeignKey) {
            isAddedConstrain = true;
            stringBuilder.append("fk").append(",");
        }
        if (isUnique) {
            isAddedConstrain = true;
            stringBuilder.append("uk").append(",");
        }

        /**
         * If we have comma at the end of the string, we need to remove it.
         * because it will cause an error in the mermaid query.
         * We can skip this step if we add uk to the end of the string.
         * because we don't have comma at the end of the string.
         */
        if (isAddedConstrain) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        }
        return stringBuilder.toString();
    }
}