package com.javacode.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacode.core.controller.BookController;
import com.javacode.core.entity.Book;
import com.javacode.core.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("The Lord of the Rings");
        book1.setAuthor("J.R.R. Tolkien");
        book1.setPublicationYear(1954);

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("1984");
        book2.setAuthor("George Orwell");
        book2.setPublicationYear(1949);
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Lord of the Rings"))
                .andExpect(jsonPath("$[1].title").value("1984"));
    }

    @Test
    void shouldGetBookByIdWhenFound() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(book1);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Lord of the Rings"))
                .andExpect(jsonPath("$.author").value("J.R.R. Tolkien"));
    }

    @Test
    void shouldReturnNotFoundWhenBookByIdNotFound() throws Exception {
        when(bookService.getBookById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBook() throws Exception {
        when(bookService.addBook(any(Book.class))).thenReturn(book1);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("The Lord of the Rings"))
                .andExpect(jsonPath("$.author").value("J.R.R. Tolkien"));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(book1);
        when(bookService.updateBook(any(Book.class))).thenReturn(anyInt());

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Lord of the Rings"))
                .andExpect(jsonPath("$.author").value("J.R.R. Tolkien"));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(1);
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}