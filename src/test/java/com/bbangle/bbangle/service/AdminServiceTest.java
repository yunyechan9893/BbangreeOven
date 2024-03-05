package com.bbangle.bbangle.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminServiceTest {

    private final AdminService adminService;

    public AdminServiceTest(
            @Autowired
            AdminService adminService) {
        this.adminService = adminService;
    }

    @Test
    public void uploadStoreTest(){

    }
}
