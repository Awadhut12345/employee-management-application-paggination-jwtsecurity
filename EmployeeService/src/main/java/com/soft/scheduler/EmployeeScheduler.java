package com.soft.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class EmployeeScheduler {

    @Autowired
    private EmployeeSyncService employeeSyncService;

    // Example: runs every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void runEmployeeSync() {
        System.out.println("=== Employee Scheduler Triggered ===");
        employeeSyncService.processEmployees();
    }
}