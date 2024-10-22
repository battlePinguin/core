package com.javacode.core.entity;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {

    public interface Views {
        interface UserSummary {}
        interface UserDetails extends UserSummary {}
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserSummary.class)
    private Long id;

    @JsonView(Views.UserSummary.class)
    @NotEmpty(message = "Имя не может быть пустым!")
    private String name;

    @JsonView(Views.UserSummary.class)
    @Email(message = "Некорректный формат почты! Вот корректный - ^[A-Za-z0-9+_.-]+@(.+)$", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonView(Views.UserDetails.class)
    private List<Order> orders;

}
