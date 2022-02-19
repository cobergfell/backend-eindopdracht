package com.novi.fassignment;

import com.novi.fassignment.services.FilesStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
class FassignmentApplicationTests {

    @MockBean
    FilesStorageService storageService;

    @Test
    void contextLoads() {
    }




}
