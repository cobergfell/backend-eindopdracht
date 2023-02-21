package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.*;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.AnswerRepository;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private FileStorageInDataBaseServiceImpl storageService;

    @Autowired
    private MusicFileStorageInDataBaseServiceImpl musicStorageService;

    @Override
    public List<AnswerDto> getAllAnswers() {
        var dtos = new ArrayList<AnswerDto>();
        var answers = answerRepository.findAll();
        for (Answer answer : answers) {
            dtos.add(AnswerDto.fromAnswerToDto(answer));
        }
        return dtos;
    }

    @Override
    public AnswerDto getAnswerById(Long id) {
        var optionalAnswer = answerRepository.findById(id);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            AnswerDto dto = AnswerDto.fromAnswerToDto(answer);
            return dto;
        } else {
            throw new RecordNotFoundException("Answer id does not exist");
        }
    }


    @Override
    public List<AnswerDto> getAnswersByQuestionId(Long questionId) {
        var dtos = new ArrayList<AnswerDto>();
        var optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            var answers= question.getAnswers();
            for (Answer answer : answers) {
                dtos.add(AnswerDto.fromAnswerToDto(answer));
            }
            return dtos;
        } else {
            throw new RecordNotFoundException("Question id does not exist");
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
        Optional<User> optionalUser = userService.getUser(dto.username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String password = user.getPassword();
            String email = user.getEmail();
            User userFromCustomUser = new User();
            userFromCustomUser.setUsername(dto.username);
            userFromCustomUser.setPassword(password);
            userFromCustomUser.setEmail(email);
            answer.setUser(userFromCustomUser);
            answer.setTitle(dto.title);
            // answer.setDatePosted(datePosted.toString());
            answer.setContent(dto.content);
            Long idRelatedItem=dto.idRelatedItem;


            var optionalQuestion = questionRepository.findById(Long.valueOf(idRelatedItem));
            if (optionalQuestion.isPresent()) {
                var question= optionalQuestion.get();
                answer.setQuestion(question);

            } else {
                throw new RecordNotFoundException("Question id does not exist");
            }

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


        } else {
            throw new RecordNotFoundException("User id does not exist");
        }



    }

    @Override
    public void updateAnswer(AnswerInputDto dto) {

        Optional<Answer> currentAnswer = answerRepository.findById(dto.answerId);

        if (currentAnswer.isPresent()) {
            Answer answer = currentAnswer.get();
            Optional<User> optionalUser = userService.getUser(dto.username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String password = user.getPassword();
                String email = user.getEmail();
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

            } else {
                throw new RecordNotFoundException("User id does not exist");
            }
        } else {
            throw new RecordNotFoundException("Answer does not exist");
        }

    }

    @Override
    public Answer createAnswerWithoutAttachment(Answer answer) { return answerRepository.save(answer); }

    @Override
    public void deleteAllAnswers() {
        answerRepository.deleteAll();
    }


}
