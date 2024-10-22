package com.javacode.core.service;

import com.javacode.core.entity.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
    @Transactional
    public int updateBook(Book book) {
        return bookRepository.update(book);
    }
    @Transactional
    public int deleteBook(Long id) {
        return bookRepository.deleteById(id);
    }
}