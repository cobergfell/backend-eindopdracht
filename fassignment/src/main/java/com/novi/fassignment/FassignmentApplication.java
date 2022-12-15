package com.novi.fassignment;

//import com.novi.fassignment.services.FilesStorageService;
import com.novi.fassignment.services.FilesStorageService;
import com.novi.fassignment.services.NoviMethod1FileUploadServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;


@SpringBootApplication
public class FassignmentApplication implements CommandLineRunner {

	@Resource
	//FilesStorageService storageService;
	NoviMethod1FileUploadServiceImpl storageService;


	public static void main(String[] args) {
		SpringApplication.run(FassignmentApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}
}