package zut.ipz.dbproject.parser;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zut.ipz.dbproject.common.configuration.ApiConfiguration;

import java.util.Objects;


/**
 * This class is the main controller for the API.
 * It contains the test endpoint.
 * It contains the parser endpoint.
 */
@RestController
@CrossOrigin
@RequestMapping(ApiConfiguration.API)
@AllArgsConstructor
public class ParserController {
    private static final String FILE_NULL = "File is null";
    private static final String FILE_EMPTY = "File is empty";
    private static final String FILE_NOT_SQL = "File is not sql";
    private static final String FILE_TOO_BIG = "File is too big";
    private static final int FILE_SIZE_LIMIT = 10485760;

    private final Logger logger = LoggerFactory.getLogger(ParserController.class.getName());

    private final ParserService parserService;

    /**
     * This is the test endpoint.
     * Its only purpose is to test if the API is working.
     * Its works only in the dev profile.
     * If u want to test the API, run application in the dev profile.
     * Application.yaml -> spring.profiles.active: dev
     * @return a test string for mermaid.js
     * @see "https://mermaid.js.org/syntax/entityRelationshipDiagram.html"
     */
    @Profile("dev")
    @GetMapping(ApiConfiguration.TEST)
    public String test() {
        return """
                erDiagram
                    CAR ||--o{ NAMED-DRIVER : allows
                    CAR {
                        string registrationNumber
                        string make
                        string model
                    }
                    PERSON ||--o{ NAMED-DRIVER : is
                    PERSON {
                        string firstName
                        string lastName
                        int age
                    }
                """;

    }

    /**
     * This is the parser endpoint.
     * @param sqlFile is a string that will be parsed
     * @return a string for mermaid.js
     * @see "https://mermaid.js.org/syntax/entityRelationshipDiagram.html"
     */
    @PostMapping(ApiConfiguration.PARSER)
    public ResponseEntity<String> parser(@RequestPart(name = "sqlFile") MultipartFile sqlFile) {

        if (sqlFile == null) {
            logger.error(FILE_NULL);
            return ResponseEntity.badRequest().body(FILE_NULL);
        }
        if (sqlFile.isEmpty()) {
            logger.error(FILE_EMPTY);
            return ResponseEntity.badRequest().body(FILE_EMPTY);
        }
        if (!Objects.requireNonNull(sqlFile.getOriginalFilename()).endsWith(".sql")) {
            logger.error(FILE_NOT_SQL);
            return ResponseEntity.badRequest().body(FILE_NOT_SQL);
        }
        if (sqlFile.getSize() > FILE_SIZE_LIMIT) {
            logger.error(FILE_TOO_BIG);
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(FILE_TOO_BIG);
        }
        return ResponseEntity.ok().body(parserService.parse(sqlFile));
    }
}
