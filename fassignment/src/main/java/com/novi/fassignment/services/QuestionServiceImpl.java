package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.QuestionInputDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FileStorageInDataBaseService storageService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    UserService userService;

    @Autowired
    PaintingService paintingService;

    @Autowired
    MusicPieceService musicPieceService;


    private FileStorageInDataBaseService fileStorageInDataBaseService;
    //private List<Question> questions = new ArrayList<>();

/*    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }*/

    @Override
    public Question getQuestionById(Long id) {
        var optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            return optionalQuestion.get();
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }


    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getAllQuestionsByDescId() {
        return questionRepository.findAll(Sort.by("questionId").descending());
    }

    @Override
    public Question createQuestionWithoutAttachment(Question question) { return questionRepository.save(question); }


    @Override
    public void deleteQuestionById(Long id) {
        var optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            questionRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }

    @Override
    public void deleteAllQuestions() {
        questionRepository.deleteAll();
    }


    @Override
    public void createQuestion(QuestionInputDto dto) {

        Question question = new Question();
        Optional<User> user = userService.getUser(dto.username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(dto.username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        question.setUser(userFromCustomUser);
        question.setTitle(dto.title);
        // question.setDatePosted(datePosted.toString());
        question.setContent(dto.content);
        question.setTags(dto.tags);
        Long idRelatedItem=dto.idRelatedItem;
        if(dto.questionRelatedTo.equals("painting")){
            Painting painting = paintingService.getPaintingById(Long.valueOf(idRelatedItem));
            question.setPainting(painting);
        }
        else{
            MusicPiece musicPiece = musicPieceService.getMusicPieceById(Long.valueOf(idRelatedItem));
            question.setMusicPiece(musicPiece);
        }

        // test, remove line below
        //Painting painting = paintingService.getPaintingById(Long.valueOf(1));
        //question.setPainting(painting);

        questionRepository.save(question);

        List<Question> sortedQuestions = questionService.getAllQuestionsByDescId();
        //Question[] sortedQuestionsArray= (Question[]) sortedQuestions.toArray();//cast array of objects into array of questions
        Question mostRecentQuestion = sortedQuestions.get(0);
        //Question mostRecentQuestion=sortedQuestionsArray[0];
        Long mostRecentQuestionId = mostRecentQuestion.getQuestionId();

        //storageService.store(file);

        List<String> fileNames = new ArrayList<>();

        Arrays.asList(dto.files).stream().forEach(file -> {
            String message2 = "";
            try {
                //storageService.store(file);
                //storageService.storeQuestionFile(file, question);
                FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                fileStoredInDataBaseInputDto.setFile(file);
                fileStoredInDataBaseInputDto.setQuestion(question);
                FileStoredInDataBase fileStoredInDataBase=storageService.storeAttachedFile(fileStoredInDataBaseInputDto);


                fileNames.add(file.getOriginalFilename());
                message2 = "Uploaded the files successfully: " + fileNames;
            } catch (Exception e) {
                message2 = "Fail to upload files!";
            }
        });

        Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
        List<FileStoredInDataBase> sortedFiles = storageService.getAllFilesByDescId();
        //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedQuestions.toArray();//cast array of objects into array of questions
        for (int i = 0; i < fileNames.size(); i++) {
            FileStoredInDataBase mostRecentFile = sortedFiles.get(i);
            attachedFiles.add(mostRecentFile);
        }

        mostRecentQuestion.setFiles(attachedFiles);

    }


    @Override
    public void updateQuestion(QuestionInputDto dto, Question question) {

        Optional<User> user = userService.getUser(dto.username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(dto.username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        question.setUser(userFromCustomUser);
        question.setTitle(dto.title);
        // question.setDatePosted(datePosted.toString());
        question.setContent(dto.content);
        question.setTags(dto.tags);
        //question.setId(dto.id);
        questionRepository.save(question);


        List<String> fileNames = new ArrayList<>();

        Arrays.asList(dto.files).stream().forEach(file -> {
            String message = "";
            try {
                //storageService.store(file);
                //storageService.storeQuestionFile(file, question);
                FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                fileStoredInDataBaseInputDto.setFile(file);
                fileStoredInDataBaseInputDto.setQuestion(question);
                FileStoredInDataBase fileStoredInDataBase=storageService.storeAttachedFile(fileStoredInDataBaseInputDto);



                fileNames.add(file.getOriginalFilename());
                message = "Uploaded the files successfully: " + fileNames;
            } catch (Exception e) {
                message = "Fail to upload files!";
            }
        });

        Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
        List<FileStoredInDataBase> sortedFiles = storageService.getAllFilesByDescId();
        //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedQuestions.toArray();//cast array of objects into array of questions
        for (int i = 0; i < fileNames.size(); i++) {
            FileStoredInDataBase mostRecentFile = sortedFiles.get(i);
            attachedFiles.add(mostRecentFile);
        }

        question.setFiles(attachedFiles);

    }



}




