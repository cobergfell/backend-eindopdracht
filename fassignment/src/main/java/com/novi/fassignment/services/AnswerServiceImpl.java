package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.AnswerRepository;
import com.novi.fassignment.repositories.FileStorageInDataBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private FileStorageInDataBaseRepository fileStorageInDataBaseRepository;


    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private FileStorageInDataBaseService storageService;


    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    UserService userService;

    @Autowired
    PaintingService paintingService;

    @Autowired
    MusicPieceService musicPieceService;

    private FileStorageInDataBaseService fileStorageInDataBaseService;
    //private List<Answer> answers = new ArrayList<>();

/*    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }*/

    @Override
    public Answer getAnswerById(Long id) {
        var optionalAnswer = answerRepository.findById(id);
        if (optionalAnswer.isPresent()) {
            return optionalAnswer.get();
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }


    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public List<Answer> getAllAnswersByDescId() {
        return answerRepository.findAll(Sort.by("answerId").descending());
    }

    @Override
    public Answer createAnswerWithoutAttachment(Answer answer) { return answerRepository.save(answer); }


    @Override
    public void deleteAnswerById(Long id) {
        var optionalAnswer = answerRepository.findById(id);
        if (optionalAnswer.isPresent()) {
            answerRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }

    @Override
    public void deleteAllAnswers() {
        answerRepository.deleteAll();
    }


    @Override
    public void createAnswer(AnswerInputDto dto) {

        Answer answer = new Answer();
        Optional<User> user = userService.getUser(dto.username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(dto.username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        answer.setUser(userFromCustomUser);
        answer.setTitle(dto.title);
        // answer.setDatePosted(datePosted.toString());
        answer.setContent(dto.content);
        answer.setTags(dto.tags);
        Long idRelatedItem=dto.idRelatedItem;

        if(idRelatedItem>=0){
            if(dto.answerRelatedTo.equals("question")){
                Question question= questionService.getQuestionById(Long.valueOf(idRelatedItem));
                answer.setQuestion(question);
                answer.setPainting(null);
                answer.setMusicPiece(null);

            }
//            else if (dto.answerRelatedTo.equals("musicPiece")){
//                MusicPiece musicPiece = musicPieceService.getMusicPieceById(Long.valueOf(idRelatedItem));
//                answer.setMusicPiece(musicPiece);
//                answer.setPainting(null);
//            }

        }


        else{
            answer.setQuestion(null);
            answer.setPainting(null);
            answer.setMusicPiece(null);
        }

        // test, remove line below
        //Painting painting = paintingService.getPaintingById(Long.valueOf(1));
        //answer.setPainting(painting);

        //answerRepository.save(answer);
        Answer mostRecentAnswer = answerService.createAnswerWithoutAttachment(answer);



        if(dto.files!=null){
/*           List<Answer> sortedAnswers = answerService.getAllAnswersByDescId();
            //Answer[] sortedAnswersArray= (Answer[]) sortedAnswers.toArray();//cast array of objects into array of answers
            Answer mostRecentAnswer = sortedAnswers.get(0);
            //Answer mostRecentAnswer=sortedAnswersArray[0];
            Long mostRecentAnswerId = mostRecentAnswer.getAnswerId();*/

            //storageService.store(file);

            List<String> fileNames = new ArrayList<>();

            Arrays.asList(dto.files).stream().forEach(file -> {
                String message2 = "";
                try {
                    //storageService.store(file);
                    //storageService.storeAnswerFile(file, answer);
                    FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                    fileStoredInDataBaseInputDto.setFile(file);
                    fileStoredInDataBaseInputDto.setAnswer(answer);
                    FileStoredInDataBase fileStoredInDataBase=storageService.storeAttachedFile(fileStoredInDataBaseInputDto);


                    fileNames.add(file.getOriginalFilename());
                    message2 = "Uploaded the files successfully: " + fileNames;
                } catch (Exception e) {
                    message2 = "Fail to upload files!";
                }
            });

            Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
            List<FileStoredInDataBase> sortedFiles = storageService.getAllFilesByDescId();
            //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedAnswers.toArray();//cast array of objects into array of answers
            for (int i = 0; i < fileNames.size(); i++) {
                FileStoredInDataBase mostRecentFile = sortedFiles.get(i);
                attachedFiles.add(mostRecentFile);
            }

            mostRecentAnswer.setFiles(attachedFiles);


        }


    }


    @Override
    public void updateAnswer(AnswerInputDto dto, Answer answer) {

        Optional<User> user = userService.getUser(dto.username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(dto.username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        answer.setUser(userFromCustomUser);
        answer.setTitle(dto.title);
        // answer.setDatePosted(datePosted.toString());
        answer.setContent(dto.content);
        answer.setTags(dto.tags);
        //answer.setId(dto.id);
        answerRepository.save(answer);


        List<String> fileNames = new ArrayList<>();

        Arrays.asList(dto.files).stream().forEach(file -> {
            String message = "";
            try {
                //storageService.store(file);
                //storageService.storeAnswerFile(file, answer);
                FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                fileStoredInDataBaseInputDto.setFile(file);
                fileStoredInDataBaseInputDto.setAnswer(answer);
                FileStoredInDataBase fileStoredInDataBase=storageService.storeAttachedFile(fileStoredInDataBaseInputDto);



                fileNames.add(file.getOriginalFilename());
                message = "Uploaded the files successfully: " + fileNames;
            } catch (Exception e) {
                message = "Fail to upload files!";
            }
        });

        Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
        List<FileStoredInDataBase> sortedFiles = storageService.getAllFilesByDescId();
        //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedAnswers.toArray();//cast array of objects into array of answers
        for (int i = 0; i < fileNames.size(); i++) {
            FileStoredInDataBase mostRecentFile = sortedFiles.get(i);
            attachedFiles.add(mostRecentFile);
        }

        answer.setFiles(attachedFiles);

    }

}





