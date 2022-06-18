package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.MusicFileStoredInDataBaseInputDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private FileStorageInDataBaseServiceImpl storageService;

    @Autowired
    private MusicFileStorageInDataBaseServiceImpl musicStorageService;

    @Override
    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    @Override
    public List<Answer> getAllAnswersByDescId() {
        return answerRepository.findAll(Sort.by("answerId").descending());
    }

    @Override
    public Answer createAnswerWithoutAttachment(Answer answer) { return answerRepository.save(answer); }

    @Override
    public void deleteAllAnswers() {
        answerRepository.deleteAll();
    }

    @Override
    public Answer getAnswerById(Long id) {
        var optionalAnswer = answerRepository.findById(id);
        if (optionalAnswer.isPresent()) {
            return optionalAnswer.get();
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }


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
        Long idRelatedItem=dto.idRelatedItem;

        Question question= questionService.getQuestionById(Long.valueOf(idRelatedItem));
        answer.setQuestion(question);

        Answer answerToBuildUp = answerService.createAnswerWithoutAttachment(answer);

        if(dto.files!=null){
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
            answerToBuildUp.setFiles(attachedFiles);

        }


        if(dto.musicFiles!=null){
            List<String> fileNames = new ArrayList<>();
            Arrays.asList(dto.musicFiles).stream().forEach(file -> {
                String message = "";
                try {
                    //storageService.store(file);
                    //storageService.storeAnswerFile(file, answer);
                    MusicFileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new MusicFileStoredInDataBaseInputDto();
                    fileStoredInDataBaseInputDto.setFile(file);
                    fileStoredInDataBaseInputDto.setAnswer(answer);
                    MusicFileStoredInDataBase fileStoredInDataBase=musicStorageService.storeAttachedFile(fileStoredInDataBaseInputDto);



                    fileNames.add(file.getOriginalFilename());
                    message = "Uploaded the files successfully: " + fileNames;
                } catch (Exception e) {
                    message = "Fail to upload files!";
                }
            });

            Set<MusicFileStoredInDataBase> attachedFiles = new HashSet<>();
            List<MusicFileStoredInDataBase> sortedFiles = musicStorageService.getAllFilesByDescId();
            //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedAnswers.toArray();//cast array of objects into array of answers
            for (int i = 0; i < fileNames.size(); i++) {
                MusicFileStoredInDataBase mostRecentFile = sortedFiles.get(i);
                attachedFiles.add(mostRecentFile);
            }
            answerToBuildUp.setMusicFiles(attachedFiles);

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
        answer.setImage(dto.image);
        //answer.setId(dto.id);

        answerRepository.save(answer);

        if(dto.files!=null){
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


        if(dto.musicFiles!=null){
            List<String> fileNames = new ArrayList<>();
            Arrays.asList(dto.musicFiles).stream().forEach(file -> {
                String message = "";
                try {
                    //storageService.store(file);
                    //storageService.storeAnswerFile(file, answer);
                    MusicFileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new MusicFileStoredInDataBaseInputDto();
                    fileStoredInDataBaseInputDto.setFile(file);
                    fileStoredInDataBaseInputDto.setAnswer(answer);
                    MusicFileStoredInDataBase fileStoredInDataBase=musicStorageService.storeAttachedFile(fileStoredInDataBaseInputDto);



                    fileNames.add(file.getOriginalFilename());
                    message = "Uploaded the files successfully: " + fileNames;
                } catch (Exception e) {
                    message = "Fail to upload files!";
                }
            });

            Set<MusicFileStoredInDataBase> attachedFiles = new HashSet<>();
            List<MusicFileStoredInDataBase> sortedFiles = musicStorageService.getAllFilesByDescId();
            //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedAnswers.toArray();//cast array of objects into array of answers
            for (int i = 0; i < fileNames.size(); i++) {
                MusicFileStoredInDataBase mostRecentFile = sortedFiles.get(i);
                attachedFiles.add(mostRecentFile);
            }
            answer.setMusicFiles(attachedFiles);

        }




    }



}
