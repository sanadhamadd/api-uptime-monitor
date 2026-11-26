package io.github.sanadhamadd.uptimemonitor.hello;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void greetsTheWorldByDefault() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));
    }

    @Test
    void greetsTheGivenName() throws Exception {
        mockMvc.perform(get("/hello").param("name", "Sanad"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Sanad!"));
    }

    @Test
    void alwaysAnswersAsPlainTextEvenWhenABrowserAsksForHtml() throws Exception {
        mockMvc.perform(get("/hello").param("name", "<b>Sanad</b>").accept(MediaType.TEXT_HTML, MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Hello, <b>Sanad</b>!"));
    }
}
