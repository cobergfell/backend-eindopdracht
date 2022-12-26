package com.novi.fassignment;


import com.novi.fassignment.services.NoviMethod1FileUploadServiceImpl;
import com.novi.fassignment.services.UserService;
import com.novi.fassignment.utils.InitialDataLoaderImpl;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;



@SpringBootApplication
public class FassignmentApplication implements CommandLineRunner {

	@Resource
	NoviMethod1FileUploadServiceImpl storageService;

	@Autowired
	private UserService userService;

	@Autowired
	private InitialDataLoaderImpl initialDataLoader;



	public static void main(String[] args) {
		SpringApplication.run(FassignmentApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
		String demoUsername = "guest";

		initialDataLoader.createInitialUser(demoUsername,"superSecret","be.myguest@novi-education.nl",true,true);

		String pathImageFile = new ClassPathResource("/assets/Kandinsky_Composition_VIII_1923.PNG").getFile().getAbsolutePath();
		File imageFile = new File(pathImageFile);
		FileInputStream inputStreamImage =  new FileInputStream(imageFile);
		MultipartFile image = new MockMultipartFile("Kandinsky_Composition_VIII_1923.PNG", "Kandinsky_Composition_VIII_1923.PNG","image/jpeg",IOUtils.toByteArray(inputStreamImage));

		String pathDemoText1 = new ClassPathResource("/assets/demo_text_1.txt").getFile().getAbsolutePath();
		File demoText1File = new File(pathDemoText1);
		FileInputStream inputStreamDemoText1 =  new FileInputStream(demoText1File);
		MultipartFile demoText1 = new MockMultipartFile("demo_text_2.txt", "demo_text_2.txt","text/plain",IOUtils.toByteArray(inputStreamDemoText1 ));

		String pathDemoText2 = new ClassPathResource("/assets/demo_text_2.txt").getFile().getAbsolutePath();
		File demoText2File = new File(pathDemoText2);
		FileInputStream inputStreamDemoText2 =  new FileInputStream(demoText1File);
		MultipartFile demoText2 = new MockMultipartFile("demo_text_2.txt", "demo_text_2.txt","text/plain",IOUtils.toByteArray(inputStreamDemoText2 ));


		String pathDemoAudio1 = new ClassPathResource("/assets/rachmaninov.mp3").getFile().getAbsolutePath();
		File demoAudio1File = new File(pathDemoAudio1);
		FileInputStream inputStreamDemoAudio1 =  new FileInputStream(demoAudio1File);
		MultipartFile demoAudio1 = new MockMultipartFile("rachmaninov.mp3", "rachmaninov.mp3","audio/mp3",IOUtils.toByteArray(inputStreamDemoAudio1));

		String pathDemoAudio2 = new ClassPathResource("/assets/rachmaninov.mp3").getFile().getAbsolutePath();
		File demoAudio2File = new File(pathDemoAudio1);
		FileInputStream inputStreamDemoAudio2 =  new FileInputStream(demoAudio2File);
		MultipartFile demoAudio2 = new MockMultipartFile("rachmaninov.mp3", "rachmaninov.mp3","audio/mp3",IOUtils.toByteArray(inputStreamDemoAudio1));

		MultipartFile[] files = new MultipartFile[] {demoText1,demoText2};
		MultipartFile[] audioFiles = new MultipartFile[] {demoAudio1,demoAudio2};

		initialDataLoader.createInitialProject(
				demoUsername,
				"MyFirstProject",
				"Kandinsky",
				"Some text",
				image,
				files,
				audioFiles);

		initialDataLoader.addQuestionToInitialProject(
				1L,
				demoUsername,
				"MyFirstQuestion",
				"Some text",
				image,
				files,
				audioFiles);

		initialDataLoader.addAnswerToQuestion(
				1L,
				demoUsername,
				"MyFirstAnswer",
				"Some text",
				image,
				files,
				audioFiles);


		}
}