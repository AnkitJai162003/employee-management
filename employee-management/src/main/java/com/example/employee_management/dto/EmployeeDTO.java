package com.example.employee_management.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;

    @NotBlank(message="Name is mandatory")
    @Size(min=2,max=50,message="Name should be 2 to 50 characters long")
    private String name;

    @Email(message="Email should be valid")
    @NotBlank(message="Email is mandatory")
    private String email;

    @NotBlank(message="Department is mandatory")
    private String department;
}
