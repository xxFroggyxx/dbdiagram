package zut.ipz.dbproject.praser;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zut.ipz.dbproject.common.configuration.ApiConfiguration;

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
    public String parser(@RequestParam("sqlFile") String sqlFile) {
        return parserService.parse(sqlFile);
    }
}
