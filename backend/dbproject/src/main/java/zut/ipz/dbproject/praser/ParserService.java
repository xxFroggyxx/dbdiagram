package zut.ipz.dbproject.praser;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ParserService {
    private enum DataBaseDataType {
        STRING, INT, FLOAT, DATE, TIME, DATETIME, BOOLEAN,VARCHAR
    }

    public String parse(String input) {
        //TODO: implement parser
        StringBuilder output = new StringBuilder();
        return output.toString();
    }
}
