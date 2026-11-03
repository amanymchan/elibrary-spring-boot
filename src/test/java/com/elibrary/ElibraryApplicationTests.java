package com.elibrary;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ElibraryApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void catalogLoadsSeededBooks() throws Exception {
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(content().string(containsString("Pride and Prejudice")));
    }

    @Test
    void digitalCatalogAndReaderLoad() throws Exception {
        mockMvc.perform(get("/digital"))
                .andExpect(status().isOk())
                .andExpect(view().name("digital"))
                .andExpect(content().string(containsString("The Secret Garden")));

        mockMvc.perform(get("/books/1/digital"))
                .andExpect(status().isOk())
                .andExpect(view().name("reader"));
    }

    @Test
    void catalogSearchAndFilteringLoad() throws Exception {
        mockMvc.perform(get("/book").param("q", "jane").param("filter", "digital"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Pride and Prejudice")));
    }

    @Test
    void borrowRedirectUsesTheForwardedBrowserHost() throws Exception {
        mockMvc.perform(post("/books/1/borrow")
                        .header("X-Forwarded-Host", "library.example")
                        .header("X-Forwarded-Proto", "https")
                        .header("X-Forwarded-Port", "443"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://library.example/book"));

        mockMvc.perform(post("/books/1/return")
                        .header("X-Forwarded-Host", "library.example")
                        .header("X-Forwarded-Proto", "https")
                        .header("X-Forwarded-Port", "443"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://library.example/borrowed-books"));
    }
}
