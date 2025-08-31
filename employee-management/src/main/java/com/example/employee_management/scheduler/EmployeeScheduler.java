package com.example.employee_management.scheduler;


import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmployeeScheduler {

    private final EmployeeRepository employeeRepository;

    public EmployeeScheduler(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }

//    Run every 2 seconds
    @Scheduled(fixedRate=2000)
    public void logEmployeeCount(){
        long count=employeeRepository.count();
        System.out.println("Total employees " + count + " (Logged every 2 seconds)");
    }
}
