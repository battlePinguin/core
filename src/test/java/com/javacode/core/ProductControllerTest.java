package com.javacode.core;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacode.core.controller.ProductController;
import com.javacode.core.repository.ProductRepository;
import com.javacode.core.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);
        product.setQuantityInStock(50);
    }

    @Test
    public void getProductById_whenProductExists_shouldReturnProduct() throws Exception {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.name").value("Test product"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.quantityInStock").value(50));
    }

    @Test
    public void getProductById_whenProductDoesNotExist_shouldThrowProductNotFoundException() throws Exception {
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Продукт с айДИ 1 не найден!"));
    }

    @Test
    public void createProduct_shouldReturnCreatedProduct() throws Exception {
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test product"));
    }
}
