package com.javacode.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacode.core.controller.EmployeeController;
import com.javacode.core.entity.Department;
import com.javacode.core.entity.Employee;
import com.javacode.core.service.EmployeeService;
import com.javacode.core.utils.EmployeeProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        Department department = new Department();
        department.setId(1);
        department.setName("HR");

        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setPosition("Manager");
        employee1.setSalary(50000);
        employee1.setDepartment(department);

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Doe");
        employee2.setPosition("Developer");
        employee2.setSalary(60000);
        employee2.setDepartment(department);
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employee1, employee2));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void shouldGetEmployeeByIdWhenFound() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee1));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeByIdNotFound() throws Exception {
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee1);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAllEmployeeProjections() throws Exception {

        EmployeeProjection projection1 = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "Джон Дон";
            }

            @Override
            public String getPosition() {
                return "Манагер";
            }

            @Override
            public String getDepartmentName() {
                return "ЭйчАр";
            }
        };

        EmployeeProjection projection2 = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "Рон Дон";
            }

            @Override
            public String getPosition() {
                return "Разработчик";
            }

            @Override
            public String getDepartmentName() {
                return "ЭйчАр";
            }
        };

        when(employeeService.getAllEmployeeProjections()).thenReturn(Arrays.asList(projection1, projection2));

        mockMvc.perform(get("/api/employees/projections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Джон Дон"))
                .andExpect(jsonPath("$[0].position").value("Манагер"))
                .andExpect(jsonPath("$[0].departmentName").value("ЭйчАр"))
                .andExpect(jsonPath("$[1].fullName").value("Рон Дон"))
                .andExpect(jsonPath("$[1].position").value("Разработчик"))
                .andExpect(jsonPath("$[1].departmentName").value("ЭйчАр"));
    }
}