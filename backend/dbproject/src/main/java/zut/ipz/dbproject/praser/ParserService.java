package zut.ipz.dbproject.praser;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class contains main logic for parsing.
 * This class includes two methods:
 * 1. parse(String input) - parses a string
 * 2. parseSql(MultipartFile file) - changes a file to string and parses it
 */
@Service
public class ParserService {
    private enum DataBaseDataType {
        STRING, INT, FLOAT, DATE, TIME, DATETIME, BOOLEAN,VARCHAR
    }

    /**
     * This method parses a string.
     * @param input is a string that will be parsed
     * @return a string for mermaid.js
     * @see "https://mermaid.js.org/syntax/entityRelationshipDiagram.html"
     */
    public String parse(String input) {
        //TODO: implement parser
        StringBuilder output = new StringBuilder();
        return output.toString();
    }

    /**
     * This method changes a file to string and parses it.
     * @param file is a file that will be parsed
     * @return a string for mermaid.js
     * @see "https://mermaid.js.org/syntax/entityRelationshipDiagram.html"
     */
    public  String parseSql(MultipartFile file) {
        return parse(file.toString());
    }
}
