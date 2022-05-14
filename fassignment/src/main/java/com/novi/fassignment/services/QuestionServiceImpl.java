package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.MusicFileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.QuestionInputDto;
import com.novi.fassignment.exceptions.FileStorageException;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FileStorageInDataBaseService storageService;

    @Autowired
    private MusicFileStorageInDataBaseService musicStorageService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    UserService userService;

    @Autowired
    PaintingService paintingService;


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
        question.setImage(dto.image);
        Long idRelatedItem=dto.idRelatedItem;

        Painting painting = paintingService.getPaintingById(Long.valueOf(idRelatedItem));
        question.setPainting(painting);

        // test, remove line below
        //Painting painting = paintingService.getPaintingById(Long.valueOf(1));
        //question.setPainting(painting);

        //questionRepository.save(question);
        Question questionToBuildUp = questionService.createQuestionWithoutAttachment(question);



        if(dto.files!=null){
/*           List<Question> sortedQuestions = questionService.getAllQuestionsByDescId();
            //Question[] sortedQuestionsArray= (Question[]) sortedQuestions.toArray();//cast array of objects into array of questions
            Question mostRecentQuestion = sortedQuestions.get(0);
            //Question mostRecentQuestion=sortedQuestionsArray[0];
            Long mostRecentQuestionId = mostRecentQuestion.getQuestionId();*/

            //storageService.store(file);

            List<String> fileNames = new ArrayList<>();

            Arrays.asList(dto.files).stream().forEach(file -> {
                String message2 = "";
                try {
                    //storageService.store(file);
                    //storageService.storeQuestionFile(file, question);
                    FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                    fileStoredInDataBaseInputDto.setFile(file);
                    fileStoredInDataBaseInputDto.setQuestion(questionToBuildUp);
                    FileStoredInDataBase fileStoredInDataBase=storageService.storeAttachedFile(fileStoredInDataBaseInputDto);


                    fileNames.add(file.getOriginalFilename());
                    message2 = "Uploaded the files successfully: " + fileNames;
                } catch (Exception e) {
                    //message2 = "Fail to upload files!";
                    throw new FileStorageException("Fail to upload files!");

                }
            });

            Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
            List<FileStoredInDataBase> sortedFiles = storageService.getAllFilesByDescId();
            //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedQuestions.toArray();//cast array of objects into array of questions
            for (int i = 0; i < fileNames.size(); i++) {
                FileStoredInDataBase mostRecentFile = sortedFiles.get(i);
                attachedFiles.add(mostRecentFile);
            }

            questionToBuildUp.setFiles(attachedFiles);
        }


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
                            musicFileStoredInDataBaseInputDto.setQuestion(questionToBuildUp);// when is the database commit triggered following a setPainting command?
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
            question.setMusicFiles(null);
        }




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
                            musicFileStoredInDataBaseInputDto.setQuestion(question);// when is the database commit triggered following a setPainting command?
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
            question.setMusicFiles(null);
        }


    }



}




