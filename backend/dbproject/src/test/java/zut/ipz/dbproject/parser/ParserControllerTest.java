package zut.ipz.dbproject.parser;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zut.ipz.dbproject.common.configuration.ApiConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import(ParserController.class)
class ParserControllerTest {

    @Autowired
    ParserController parserController;


    @MockBean
    ParserService parserService;
    @BeforeEach
    void setup() {
        parserController = new ParserController(parserService);
    }
    @Test
    void when_file_is_null_then_bad_request_status() throws Exception {
        // given
        MockMultipartFile file = null;
        // when
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(parserController).build();
        mockMvc.perform(multipart(ApiConfiguration.API+ApiConfiguration.PARSER)).andExpect(status().isBadRequest());
        // then
    }
    @Test
    void when_file_is_not_sql_then_bad_request_status() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        // when
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(parserController).build();
        mockMvc.perform(multipart(ApiConfiguration.API+ApiConfiguration.PARSER).file(file)).andExpect(status().isBadRequest());
        // then
    }


}
