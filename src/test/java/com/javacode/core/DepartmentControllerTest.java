package com.javacode.core;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacode.core.controller.DepartmentController;
import com.javacode.core.entity.Department;
import com.javacode.core.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Department department1;
    private Department department2;

    @BeforeEach
    void setUp() {
        department1 = new Department();
        department1.setId(1);
        department1.setName("HR");

        department2 = new Department();
        department2.setId(2);
        department2.setName("Finance");
    }

    @Test
    void shouldGetAllDepartments() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(department1, department2));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("HR"))
                .andExpect(jsonPath("$[1].name").value("Finance"));
    }

    @Test
    void shouldGetDepartmentByIdWhenFound() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(Optional.of(department1));

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    void shouldReturnNotFoundWhenDepartmentByIdNotFound() throws Exception {
        when(departmentService.getDepartmentById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/departments/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateDepartment() throws Exception {
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(department1);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(department1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    void shouldDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());
    }
}