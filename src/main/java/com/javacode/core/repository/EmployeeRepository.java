package com.javacode.core.repository;

import com.javacode.core.entity.Employee;
import com.javacode.core.utils.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e")
    List<EmployeeProjection> findAllByProjection();
}
