package com.javacode.core.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotEmpty(message = "Имя не может быть пустым!")
    private String firstName;

    @NotEmpty(message = "Фамилия не может быть пустой!")
    private String lastName;

    @Email(message = "Некорректный формат почты! Вот корректный - ^[A-Za-z0-9+_.-]+@(.+)$", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @NotEmpty(message = "Контактный номер не может быть пустой!")
    private String contactNumber;
}
