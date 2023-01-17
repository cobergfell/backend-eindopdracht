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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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

		String pathImageFileKandinskyVIII1923 = new ClassPathResource("/assets/Kandinsky_Composition_VIII_1923.png").getFile().getAbsolutePath();
		File imageFileKandinskyVIII1923 = new File(pathImageFileKandinskyVIII1923);
		FileInputStream inputStreamImageKandinskyVIII1923 =  new FileInputStream(imageFileKandinskyVIII1923);
		MultipartFile imageKandinskyVIII1923 = new MockMultipartFile("Kandinsky_Composition_VIII_1923.png", "Kandinsky_Composition_VIII_1923.png","image/jpeg",IOUtils.toByteArray(inputStreamImageKandinskyVIII1923));

		String pathImageFileKandinskyIX1936 = new ClassPathResource("/assets/Kandinsky_Composition_IX_1936.png").getFile().getAbsolutePath();
		File imageFileKandinskyIX1936 = new File(pathImageFileKandinskyIX1936);
		FileInputStream inputStreamImageKandinskyIX1936 =  new FileInputStream(imageFileKandinskyIX1936);
		MultipartFile imageKandinskyIX1936 = new MockMultipartFile("Kandinsky_Composition_IX_1936.png", "Kandinsky_Composition_IX_1936.png","image/jpeg",IOUtils.toByteArray(inputStreamImageKandinskyIX1936));

		String pathImageFileKandinskyVII1913 = new ClassPathResource("/assets/Kandinsky_Composition_VII_1913.png").getFile().getAbsolutePath();
		File imageFileKandinskyVII1913 = new File(pathImageFileKandinskyVII1913);
		FileInputStream inputStreamImageKandinskyVII1913 =  new FileInputStream(imageFileKandinskyVII1913);
		MultipartFile imageKandinskyVII1913 = new MockMultipartFile("Kandinsky_Composition_VII_1913.png", "Kandinsky_Composition_VII_1913.png","image/jpeg",IOUtils.toByteArray(inputStreamImageKandinskyVII1913));

		String pathImageFileKandinskyIV1911 = new ClassPathResource("/assets/Kandinsky_Composition_IV_1911.png").getFile().getAbsolutePath();
		File imageFileKandinskyIV1911 = new File(pathImageFileKandinskyIV1911);
		FileInputStream inputStreamImageKandinskyIV1911 =  new FileInputStream(imageFileKandinskyIV1911);
		MultipartFile imageKandinskyIV1911 = new MockMultipartFile("Kandinsky_Composition_IV_1911.png", "Kandinsky_Composition_IV_1911.png","image/jpeg",IOUtils.toByteArray(inputStreamImageKandinskyIV1911));

		String pathImageFileKandinskyV1911 = new ClassPathResource("/assets/Kandinsky_Composition_V_1911.png").getFile().getAbsolutePath();
		File imageFileKandinskyV1911 = new File(pathImageFileKandinskyV1911);
		FileInputStream inputStreamImageKandinskyV1911 =  new FileInputStream(imageFileKandinskyV1911);
		MultipartFile imageKandinskyV1911 = new MockMultipartFile("Kandinsky_Composition_V_1911.png", "Kandinsky_Composition_V_1911.png","image/jpeg",IOUtils.toByteArray(inputStreamImageKandinskyV1911));

		String pathImageFileKandinskyVI1913 = new ClassPathResource("/assets/Kandinsky_Composition_VI_1913.png").getFile().getAbsolutePath();
		File imageFileKandinskyVI1913 = new File(pathImageFileKandinskyVI1913);
		FileInputStream inputStreamImageKandinskyVI1913 =  new FileInputStream(imageFileKandinskyVI1913);
		MultipartFile imageKandinskyVI1913 = new MockMultipartFile("Kandinsky_Composition_VI_1913.png", "Kandinsky_Composition_VI_1913.png","image/jpeg",IOUtils.toByteArray(inputStreamImageKandinskyVI1913));



		String pathImageChagallFiddler = new ClassPathResource("/assets/Chagall_The_Fiddler.png").getFile().getAbsolutePath();
		File imageFileChagallFiddler = new File(pathImageChagallFiddler);
		FileInputStream inputStreamImageChagallFiddler =  new FileInputStream(imageFileChagallFiddler);
		MultipartFile imageChagallFiddler1913 = new MockMultipartFile("Chagall_The_Fiddler.png", "Chagall_The_Fiddler.png","image/jpeg",IOUtils.toByteArray(inputStreamImageChagallFiddler));



		String pathDemoText1 = new ClassPathResource("/assets/demo_text_1.txt").getFile().getAbsolutePath();
		File demoText1File = new File(pathDemoText1);
		FileInputStream inputStreamDemoText1 =  new FileInputStream(demoText1File);
		MultipartFile demoText1 = new MockMultipartFile("demo_text_1.txt", "demo_text_1.txt","text/plain",IOUtils.toByteArray(inputStreamDemoText1 ));

		String pathDemoText2 = new ClassPathResource("/assets/demo_text_2.txt").getFile().getAbsolutePath();
		File demoText2File = new File(pathDemoText2);
		FileInputStream inputStreamDemoText2 =  new FileInputStream(demoText2File);
		MultipartFile demoText2 = new MockMultipartFile("demo_text_2.txt", "demo_text_2.txt","text/plain",IOUtils.toByteArray(inputStreamDemoText2 ));


		String pathDemoAudio1 = new ClassPathResource("/assets/rachmaninov.mp3").getFile().getAbsolutePath();
		File demoAudio1File = new File(pathDemoAudio1);
		FileInputStream inputStreamDemoAudio1 =  new FileInputStream(demoAudio1File);
		MultipartFile demoAudio1 = new MockMultipartFile("rachmaninov.mp3", "rachmaninov.mp3","audio/mp3",IOUtils.toByteArray(inputStreamDemoAudio1));

		String pathDemoAudio2 = new ClassPathResource("/assets/Anna_Thorvaldsdottir_Rhizoma_Hrim.mp3").getFile().getAbsolutePath();
		File demoAudio2File = new File(pathDemoAudio2);
		FileInputStream inputStreamDemoAudio2 =  new FileInputStream(demoAudio2File);
		MultipartFile demoAudio2 = new MockMultipartFile("Anna_Thorvaldsdottir_Rhizoma_Hrim.mp3", "Anna_Thorvaldsdottir_Rhizoma_Hrim.mp3","audio/mp3",IOUtils.toByteArray(inputStreamDemoAudio2));

		String pathDemoAudio3 = new ClassPathResource("/assets/Gurdjieff_by_Jeroen_van_Veen.mp3").getFile().getAbsolutePath();
		File demoAudio3File = new File(pathDemoAudio3);
		FileInputStream inputStreamDemoAudio3 =  new FileInputStream(demoAudio3File);
		MultipartFile demoAudio3 = new MockMultipartFile("Gurdjieff_by_Jeroen_van_Veen.mp3", "Gurdjieff_by_Jeroen_van_Veen.mp3","audio/mp3",IOUtils.toByteArray(inputStreamDemoAudio3));


		String pathDemoAudioLeraAuerbachTrack1 = new ClassPathResource("/assets/01_Lera_Auerbach_plays_her_preludes_and_dreams_1.mp3").getFile().getAbsolutePath();
		File demoAudioLeraAuerbachTrack1File = new File(pathDemoAudioLeraAuerbachTrack1);
		FileInputStream inputStreamDemoLeraAuerbachTrack1 =  new FileInputStream(demoAudioLeraAuerbachTrack1File);
		MultipartFile demoAudioLeraAuerbachTrack1 = new MockMultipartFile("01_Lera_Auerbach_plays_her_preludes_and_dreams_1.mp3", "01_Lera_Auerbach_plays_her_preludes_and_dreams_1.mp3","audio/mp3",IOUtils.toByteArray(inputStreamDemoLeraAuerbachTrack1));

		String pathDemoAudioLeraAuerbachTrack2 = new ClassPathResource("/assets/02_Lera_Auerbach_plays_her_preludes_and_dreams_2.mp3").getFile().getAbsolutePath();
		File demoAudioLeraAuerbachTrack2File = new File(pathDemoAudioLeraAuerbachTrack2);
		FileInputStream inputStreamDemoLeraAuerbachTrack2 =  new FileInputStream(demoAudioLeraAuerbachTrack2File);
		MultipartFile demoAudioLeraAuerbachTrack2 = new MockMultipartFile("02_Lera_Auerbach_plays_her_preludes_and_dreams_2.mp3", "02_Lera_Auerbach_plays_her_preludes_and_dreams_2.mp3","audio/mp3",IOUtils.toByteArray(inputStreamDemoLeraAuerbachTrack2));

		String pathDemoAudioLeraAuerbachTrack3 = new ClassPathResource("/assets/03_Lera_Auerbach_plays_her_preludes_and_dreams_3.mp3").getFile().getAbsolutePath();
		File demoAudioLeraAuerbachTrack3File = new File(pathDemoAudioLeraAuerbachTrack3);
		FileInputStream inputStreamDemoLeraAuerbachTrack3 =  new FileInputStream(demoAudioLeraAuerbachTrack3File);
		MultipartFile demoAudioLeraAuerbachTrack3 = new MockMultipartFile("03_Lera_Auerbach_plays_her_preludes_and_dreams_3.mp3", "03_Lera_Auerbach_plays_her_preludes_and_dreams_3.mp3","audio/mp3",IOUtils.toByteArray(inputStreamDemoLeraAuerbachTrack3));

		String pathDemoAudioLeraAuerbachTrack4 = new ClassPathResource("/assets/04_Lera_Auerbach_plays_her_preludes_and_dreams_4.mp3").getFile().getAbsolutePath();
		File demoAudioLeraAuerbachTrack4File = new File(pathDemoAudioLeraAuerbachTrack4);
		FileInputStream inputStreamDemoLeraAuerbachTrack4 =  new FileInputStream(demoAudioLeraAuerbachTrack4File);
		MultipartFile demoAudioLeraAuerbachTrack4 = new MockMultipartFile("04_Lera_Auerbach_plays_her_preludes_and_dreams_4.mp3", "03_Lera_Auerbach_plays_her_preludes_and_dreams_3.mp3","audio/mp3",IOUtils.toByteArray(inputStreamDemoLeraAuerbachTrack4));



		MultipartFile[] files = new MultipartFile[] {demoText1,demoText2};
		MultipartFile[] audioFiles1 = new MultipartFile[] {demoAudio1,demoAudio2};
		MultipartFile[] audioFiles2 = new MultipartFile[] {demoAudio2,demoAudio3};
		MultipartFile[] audioFilesDemoAudioLeraAuerbachTrack12 = new MultipartFile[] {demoAudioLeraAuerbachTrack1,demoAudioLeraAuerbachTrack2};
		MultipartFile[] audioFilesDemoAudioLeraAuerbachTrack3 = new MultipartFile[] {demoAudioLeraAuerbachTrack3};
		MultipartFile[] audioFilesDemoAudioLeraAuerbachTrack4 = new MultipartFile[] {demoAudioLeraAuerbachTrack4};


		String pathStringDemoKandinskyVII1913 = new ClassPathResource("/assets/demo_text_Kandinsky_VII_1913.txt").getFile().getAbsolutePath();
		Path pathDemoKandinskyVII1913 = Paths.get(pathStringDemoKandinskyVII1913);
		String descriptionKandinskyVII1913 = Files.readString(pathDemoKandinskyVII1913);

		String pathStringDemoKandinskyVIII1923 = new ClassPathResource("/assets/demo_text_Kandinsky_VIII_1923.txt").getFile().getAbsolutePath();
		Path pathDemoKandinskyVIII1923 = Paths.get(pathStringDemoKandinskyVIII1923);
		String descriptionKandinskyVIII1923 = Files.readString(pathDemoKandinskyVIII1923);

		String pathStringDemoKandinskyIX1936 = new ClassPathResource("/assets/demo_text_Kandinsky_IX_1936.txt").getFile().getAbsolutePath();
		Path pathDemoKandinskyIX1936 = Paths.get(pathStringDemoKandinskyIX1936);
		String descriptionKandinskyIX1936 = Files.readString(pathDemoKandinskyIX1936);

		String pathStringDemoKandinskyIV1911 = new ClassPathResource("/assets/demo_text_Kandinsky_IV_1911.txt").getFile().getAbsolutePath();
		Path pathDemoKandinskyIV1911 = Paths.get(pathStringDemoKandinskyIV1911);
		String descriptionKandinskyIV1911 = Files.readString(pathDemoKandinskyIV1911);

		String pathStringDemoKandinskyV1911 = new ClassPathResource("/assets/demo_text_Kandinsky_V_1911.txt").getFile().getAbsolutePath();
		Path pathDemoKandinskyV1911 = Paths.get(pathStringDemoKandinskyV1911);
		String descriptionKandinskyV1911 = Files.readString(pathDemoKandinskyV1911);

		String pathStringDemoKandinskyVI1913 = new ClassPathResource("/assets/demo_text_Kandinsky_VI_1913.txt").getFile().getAbsolutePath();
		Path pathDemoKandinskyVI1913 = Paths.get(pathStringDemoKandinskyVI1913);
		String descriptionKandinskyVI1913 = Files.readString(pathDemoKandinskyVI1913);

		String pathStringChagallFiddler1913 = new ClassPathResource("/assets/demo_text_Chagall_Fiddler.txt").getFile().getAbsolutePath();
		Path pathDemoChagallFiddler1913 = Paths.get(pathStringChagallFiddler1913);
		String descriptionChagallFiddler1913 = Files.readString(pathDemoChagallFiddler1913);


		initialDataLoader.createInitialProject(
				demoUsername,
				"Kandinsky_VII_1913",
				"Kandinsky",
				descriptionKandinskyVII1913,
				imageKandinskyVII1913,
				files,
				audioFilesDemoAudioLeraAuerbachTrack12);

		initialDataLoader.addQuestionToInitialProject(
				1L,
				demoUsername,
				"Question_about_Kandinsky_VII_1913",
				"Is the tone colour of the attached music file not a bit more suited than the ones initially sent? At least they fit quite well the painting attached with this question",
				imageKandinskyVIII1923,
				files,
				audioFilesDemoAudioLeraAuerbachTrack3);

		initialDataLoader.addAnswerToQuestion(
				1L,
				demoUsername,
				"Answer_about_Kandinsky_VII_1913",
				"Very good suggestion, thanks! Please find attached another interesting match between the attached painting and the attached music",
				imageKandinskyIX1936,
				files,
				audioFilesDemoAudioLeraAuerbachTrack4);


		initialDataLoader.createInitialProject(
				demoUsername,
				"Kandinsky_VIII_1923",
				"Kandinsky",
				descriptionKandinskyVIII1923,
				imageKandinskyVIII1923,
				files,
				audioFiles1);

		initialDataLoader.addQuestionToInitialProject(
				2L,
				demoUsername,
				"Question_about_Kandinsky_VIII_1923",
				"Is the tone colour of the attached music file not a bit more suited than the ones initially sent? At least they fit quite well the painting attached with this question",
				imageKandinskyIX1936,
				files,
				audioFiles2);

		initialDataLoader.addAnswerToQuestion(
				2L,
				demoUsername,
				"Answer_about_Kandinsky_VIII_1923",
				"Very good suggestion, thanks! Please find attached another interesting match between the attached painting and the attached music",
				imageKandinskyVIII1923,
				files,
				audioFiles1);


		initialDataLoader.createInitialProject(
				demoUsername,
				"Kandinsky_IX_1936",
				"Kandinsky",
				descriptionKandinskyIX1936,
				imageKandinskyIX1936,
				files,
				audioFiles1);

		initialDataLoader.addQuestionToInitialProject(
				3L,
				demoUsername,
				"Question_about_Kandinsky_VIII_1923",
				"Is the tone colour of the attached music file not a bit more suited than the ones initially sent? At least they fit quite well the painting attached with this question",
				imageKandinskyIX1936,
				files,
				audioFiles2);

		initialDataLoader.addAnswerToQuestion(
				3L,
				demoUsername,
				"Answer_about_Kandinsky_VIII_1923",
				"Very good suggestion, thanks! Please find attached another interesting match between the attached painting and the attached music",
				imageKandinskyVIII1923,
				files,
				audioFiles1);

		initialDataLoader.createInitialProject(
				demoUsername,
				"Kandinsky_IV_1911",
				"Kandinsky",
				descriptionKandinskyIV1911,
				imageKandinskyIV1911,
				files,
				audioFiles1);

		initialDataLoader.addQuestionToInitialProject(
				4L,
				demoUsername,
				"Question_about_Kandinsky_IV_1911",
				"Is the tone colour of the attached music file not a bit more suited than the ones initially sent? At least they fit quite well the painting attached with this question",
				imageKandinskyIX1936,
				files,
				audioFiles2);

		initialDataLoader.addAnswerToQuestion(
				4L,
				demoUsername,
				"Answer_about_Kandinsky_IV_1911",
				"Very good suggestion, thanks! Please find attached another interesting match between the attached painting and the attached music",
				imageKandinskyVIII1923,
				files,
				audioFiles1);

		initialDataLoader.createInitialProject(
				demoUsername,
				"Kandinsky_V_1911",
				"Kandinsky",
				descriptionKandinskyV1911,
				imageKandinskyV1911,
				files,
				audioFiles1);

		initialDataLoader.addQuestionToInitialProject(
				5L,
				demoUsername,
				"Question_about_Kandinsky_V_1911",
				"Is the tone colour of the attached music file not a bit more suited than the ones initially sent? At least they fit quite well the painting attached with this question",
				imageKandinskyIX1936,
				files,
				audioFiles2);

		initialDataLoader.addAnswerToQuestion(
				5L,
				demoUsername,
				"Answer_about_Kandinsky_V_1911",
				"Very good suggestion, thanks! Please find attached another interesting match between the attached painting and the attached music",
				imageKandinskyVIII1923,
				files,
				audioFiles1);

		initialDataLoader.createInitialProject(
				demoUsername,
				"Kandinsky_VI_1913",
				"Kandinsky",
				descriptionKandinskyVI1913,
				imageKandinskyVI1913,
				files,
				audioFiles1);

		initialDataLoader.addQuestionToInitialProject(
				6L,
				demoUsername,
				"Question_about_Kandinsky_VI_1913",
				"Is the tone colour of the attached music file not a bit more suited than the ones initially sent? At least they fit quite well the painting attached with this question",
				imageKandinskyVI1913,
				files,
				audioFiles2);

		initialDataLoader.addAnswerToQuestion(
				6L,
				demoUsername,
				"Answer_about_Kandinsky_V_1911",
				"Very good suggestion, thanks! Please find attached another interesting match between the attached painting and the attached music",
				imageKandinskyVIII1923,
				files,
				audioFiles1);

		initialDataLoader.createInitialProject(
				demoUsername,
				"Chagall_the_Fiddler_1913",
				"Kandinsky",
				descriptionChagallFiddler1913,
				imageChagallFiddler1913,
				files,
				audioFiles1);

		initialDataLoader.addQuestionToInitialProject(
				7L,
				demoUsername,
				"Question_about_Kandinsky_VI_1913",
				"Is the tone colour of the attached music file not a bit more suited than the ones initially sent? At least they fit quite well the painting attached with this question",
				imageKandinskyVI1913,
				files,
				audioFiles2);

		initialDataLoader.addAnswerToQuestion(
				7L,
				demoUsername,
				"Answer_about_Kandinsky_V_1911",
				"Very good suggestion, thanks! Please find attached another interesting match between the attached painting and the attached music",
				imageKandinskyVIII1923,
				files,
				audioFiles1);

		}


}