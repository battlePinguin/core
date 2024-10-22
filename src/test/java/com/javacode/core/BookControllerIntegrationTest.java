package com.javacode.core;

import com.javacode.core.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate();

    private String baseUrl() {
        return "http://localhost:" + port + "/api/books";
    }

    @Test
    public void shouldGetAllBooks() {
        ResponseEntity<Book[]> response = restTemplate.getForEntity(baseUrl(), Book[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Book> books = Arrays.asList(response.getBody());
        assertThat(books).isNotEmpty();
    }

    @Test
    public void shouldGetBookById() {

        String url = baseUrl() + "/1";

        ResponseEntity<Book> response = restTemplate.getForEntity(url, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("The Lord of the Rings");
    }

    @Test
    public void shouldCreateBook() {

        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setPublicationYear(2024);

        ResponseEntity<Book> response = restTemplate.postForEntity(baseUrl(), newBook, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo("New Book");
    }

    @Test
    public void shouldUpdateBook() {
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setPublicationYear(2022);

        String url = baseUrl() + "/1";

        HttpEntity<Book> requestUpdate = new HttpEntity<>(updatedBook);
        ResponseEntity<Book> response = restTemplate.exchange(url, HttpMethod.PUT, requestUpdate, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Title");
    }

    @Test
    public void shouldDeleteBook() {
        String url = baseUrl() + "/1";

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}