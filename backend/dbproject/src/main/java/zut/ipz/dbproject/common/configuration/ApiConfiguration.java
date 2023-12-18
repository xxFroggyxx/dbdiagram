package zut.ipz.dbproject.common.configuration;


import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains all the constants for the API.
 * Use this class to avoid hardcoding the API paths.
 */
@Configuration
@NoArgsConstructor
public class ApiConfiguration {


    /**
     * This is the base path for the API.
     */
    public static final String API = "/api/v1";

    /**
     * This is the base path for the test endpoint.
     */
    public static final String TEST = "/test";

    /**
     * This is the base path for the parser endpoint.
     */
    public static final String PARSER = "/parser";
}
