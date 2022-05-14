package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.controllers.dto.MusicFileStoredInDataBaseInputDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.AnswerRepository;
import com.novi.fassignment.repositories.FileStorageInDataBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private MusicFileStorageInDataBaseService musicStorageService;

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    UserService userService;

    @Autowired
    PaintingService paintingService;


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
        Long idRelatedItem=dto.idRelatedItem;

        Question question= questionService.getQuestionById(Long.valueOf(idRelatedItem));
        answer.setQuestion(question);

        Answer answerToBuildUp = answerService.createAnswerWithoutAttachment(answer);


        if(dto.files!=null){
            List<String> fileNames = new ArrayList<>();
            Arrays.asList(dto.files).stream().forEach(file -> {
                String message2 = "";
                try {
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
            answerToBuildUp.setFiles(attachedFiles);
        }



        if (dto.musicFiles!=null ){
            List<String> musicFileNames = new ArrayList<>();
            Set<MusicFileStoredInDataBase> attachedMusicFiles = new HashSet<>();
            List<MultipartFile> musicMultipartFiles = new ArrayList<MultipartFile>();
            Arrays.asList(dto.musicFiles).stream().forEach(theFile -> musicMultipartFiles.add(theFile));
            //MultipartFile extra=multipartFiles.get(multipartFiles.size()-1);
            //multipartFiles.add(extra);//this is a trick because of a bug part 1: you have to repeat the last element and remove it later in order to have setPainting work, I still don't get why
            // solution of the bug was:    in FileStorageInDataBaseService.java, public FileStoredInDataBase storeAttachedFile I returned return fileStoredInDataBase;
            // instead of  return fileStorageInDataBaseRepository.save(fileStoredInDataBase);


            //Arrays.asList(dto.files).stream().forEach(file -> {
            musicMultipartFiles.stream().forEach(file -> {
                        String message3 = "";
                        try {
                            MusicFileStoredInDataBaseInputDto musicFileStoredInDataBaseInputDto = new MusicFileStoredInDataBaseInputDto();
                            musicFileStoredInDataBaseInputDto.setFile(file);
                            musicFileStoredInDataBaseInputDto.setAnswer(answerToBuildUp);// when is the database commit triggered following a setPainting command?
                            MusicFileStoredInDataBase musicFileStoredInDataBase = musicStorageService.storeAttachedFile(musicFileStoredInDataBaseInputDto);
                            //long fileId=fileStoredInDataBase.getFileId();
                            //if (fileId==multipartFiles.size()){storageService.deleteFileStoredInDataBaseById(fileId);}// trick part 2: remove extra file
                            // it took me hours ti find out that this trick to add the last file of the list and remove it
                            // later is the way to associate the last multipart file to the painting
                            // I do not now why it does not work without this trick, think about it later and fix it



                            attachedMusicFiles.add(musicFileStoredInDataBase);
                            message3 = "Files successfully added to painting object: " + musicFileNames;
                        } catch (Exception e) {
                            message3 = "Fail to attach files to painting object!";
                        }
                    }

            );

        }
        else{
            answer.setMusicFiles(null);
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
        if (dto.musicFiles!=null ){
            List<String> musicFileNames = new ArrayList<>();
            Set<MusicFileStoredInDataBase> attachedMusicFiles = new HashSet<>();
            List<MultipartFile> musicMultipartFiles = new ArrayList<MultipartFile>();
            Arrays.asList(dto.musicFiles).stream().forEach(theFile -> musicMultipartFiles.add(theFile));
            //MultipartFile extra=multipartFiles.get(multipartFiles.size()-1);
            //multipartFiles.add(extra);//this is a trick because of a bug part 1: you have to repeat the last element and remove it later in order to have setPainting work, I still don't get why
            // solution of the bug was:    in FileStorageInDataBaseService.java, public FileStoredInDataBase storeAttachedFile I returnred return fileStoredInDataBase;
            // instead of  return fileStorageInDataBaseRepository.save(fileStoredInDataBase);


            //Arrays.asList(dto.files).stream().forEach(file -> {
            musicMultipartFiles.stream().forEach(file -> {
                        String message3 = "";
                        try {
                            MusicFileStoredInDataBaseInputDto musicFileStoredInDataBaseInputDto = new MusicFileStoredInDataBaseInputDto();
                            musicFileStoredInDataBaseInputDto.setFile(file);
                            musicFileStoredInDataBaseInputDto.setAnswer(answer);// when is the database commit triggered following a setPainting command?
                            MusicFileStoredInDataBase musicFileStoredInDataBase = musicStorageService.storeAttachedFile(musicFileStoredInDataBaseInputDto);
                            //long fileId=fileStoredInDataBase.getFileId();
                            //if (fileId==multipartFiles.size()){storageService.deleteFileStoredInDataBaseById(fileId);}// trick part 2: remove extra file
                            // it took me hours ti find out that this trick to add the last file of the list and remove it
                            // later is the way to associate the last multipart file to the painting
                            // I do not now why it does not work without this trick, think about it later and fix it



                            attachedMusicFiles.add(musicFileStoredInDataBase);
                            message3 = "Files successfully added to painting object: " + musicFileNames;
                        } catch (Exception e) {
                            message3 = "Fail to attach files to painting object!";
                        }
                    }

            );

        }
        else{
            answer.setMusicFiles(null);
        }

    }

}





