package com.javacode.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacode.core.entity.Order;
import com.javacode.core.entity.User;
import com.javacode.core.exception.UserNotFoundException;
import com.javacode.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setName("User One");
        user1.setEmail("user1@example.com");

        user2 = new User();
        user2.setId(2L);
        user2.setName("User Two");
        user2.setEmail("user2@example.com");

        Order order1 = new Order();
        order1.setId(1L);
        order1.setAmount(100.0);
        order1.setStatus("PAID");
        order1.setUser(user1);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setAmount(50.0);
        order2.setStatus("PENDING");
        order2.setUser(user1);

        user1.setOrders(Arrays.asList(order1, order2));
    }

    @Test
    public void getAllUsers_shouldReturnUserList() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].name").value(user1.getName()))
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].name").value(user2.getName()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()));
    }

    @Test
    public void getUserById_withOrders_shouldReturnUserDetails() throws Exception {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        String jsonResponse = mockMvc.perform(get("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(jsonResponse);

        mockMvc.perform(get("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId()))
                .andExpect(jsonPath("$.name").value(user1.getName()))
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.orders[0].id").value(1L))
                .andExpect(jsonPath("$.orders[0].amount").value(100.0))
                .andExpect(jsonPath("$.orders[0].status").value("PAID"))
                .andExpect(jsonPath("$.orders[1].id").value(2L))
                .andExpect(jsonPath("$.orders[1].amount").value(50.0))
                .andExpect(jsonPath("$.orders[1].status").value("PENDING"));
    }


    @Test
    public void getUserById_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        Mockito.when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException(1L));

        mockMvc.perform(get("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
