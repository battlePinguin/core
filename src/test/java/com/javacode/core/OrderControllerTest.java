package com.javacode.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacode.core.controller.OrderController;
import com.javacode.core.repository.OrderRepository;
import com.javacode.core.entity.Order;
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

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(1L);
        order.setShippingAddress("123 Java Street");
        order.setTotalPrice(150.0);
        order.setOrderStatus("PENDING");
    }

    @Test
    public void getOrderById_whenOrderExists_shouldReturnOrder() throws Exception {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.shippingAddress").value("123 Java Street"))
                .andExpect(jsonPath("$.totalPrice").value(150.0))
                .andExpect(jsonPath("$.orderStatus").value("PENDING"));
    }

    @Test
    public void getOrderById_whenOrderDoesNotExist_shouldThrowOrderNotFoundException() throws Exception {
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Заказ с айДИ 1 не найден!"));
    }

    @Test
    public void createOrder_shouldReturnCreatedOrder() throws Exception {
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shippingAddress").value("123 Java Street"));
    }
}