package com.example.employee_management.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public void sendEmployeeNotification(String employeeName) {
        System.out.println("Sending notification for: " + employeeName);
        try {
            Thread.sleep(5000); // Simulating delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Notification sent for: " + employeeName);
    }
}