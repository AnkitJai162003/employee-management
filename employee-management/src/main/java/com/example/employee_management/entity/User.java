package com.example.employee_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String username;

    @Column(nullable=false)
    private String password; // BCrypt encoded

    @Column(nullable=false, length=30)
    private String role; // e.g. "ROLE_ADMIN" or "ROLE_USER"
}
