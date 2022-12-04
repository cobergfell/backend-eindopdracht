package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.*;
import com.novi.fassignment.exceptions.FileStorageException;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.PaintingRepository;
import com.novi.fassignment.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FileStorageInDataBaseServiceImpl storageService;

    @Autowired
    private MusicFileStorageInDataBaseServiceImpl musicStorageService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    UserService userService;

    @Autowired
    PaintingService paintingService;

    @Autowired
    private PaintingRepository paintingRepository;


    @Override
    public QuestionDto getQuestionById(Long id) {
        var dto = new QuestionDto();
        var optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            Question question =optionalQuestion.get();
            dto=QuestionDto.fromQuestionToDto(question);
            return dto;
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }


    @Override
    public List<QuestionDto> getQuestionsByPaintingId(Long paintingId) {
        var dtos = new ArrayList<QuestionDto>();
        var optionalPainting = paintingRepository.findById(paintingId);
        if (optionalPainting.isPresent()) {
            Painting painting = optionalPainting.get();
            var questions = painting.getQuestions();
            for (Question question : questions) {
                dtos.add(QuestionDto.fromQuestionToDto(question));
            }
            return dtos;
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }




//    @Override
//    public List<Question> getAllQuestions() {
//        return questionRepository.findAll();
//    }

    @Override
    public List<QuestionDto> getAllQuestions() {
        var dtos = new ArrayList<QuestionDto>();
        var questions = questionRepository.findAll();
        List<FileStoredInDataBase> filesStoredInDataBase = storageService.getAllFilesAsList();

        for (Question question : questions) {
            //paintingService.getFiles(painting);
            dtos.add(QuestionDto.fromQuestionToDto(question));
        }
        return dtos;
    }



/*    @Override
    public List<Question> getAllQuestionsByDescId() {
        return questionRepository.findAll(Sort.by("questionId").descending());
    }*/

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
        Optional<User> optionalUser = userService.getUser(dto.username);
        if (optionalUser.isPresent()) {
            String password = optionalUser.get().getPassword();
            String email = optionalUser.get().getEmail();
            User userFromCustomUser = new User();
            userFromCustomUser.setUsername(dto.username);
            userFromCustomUser.setPassword(password);
            userFromCustomUser.setEmail(email);
            question.setUser(userFromCustomUser);
            question.setTitle(dto.title);
            question.setContent(dto.content);
            question.setImage(dto.image);
            Long idRelatedItem=dto.idRelatedItem;

            PaintingDto paintingDto = paintingService.getPaintingById(Long.valueOf(idRelatedItem));
            var optionalPainting = paintingRepository.findById(Long.valueOf(idRelatedItem));
            if (optionalPainting.isPresent()) {
                var painting= optionalPainting.get();
                question.setPainting(painting);

            } else {
                throw new RecordNotFoundException("Painting id does not exist");
            }

        } else {
            throw new RecordNotFoundException("User id does not exist");
        }


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
            // solution of the bug was:    in FileStorageInDataBaseServiceImpl.java, public FileStoredInDataBase storeAttachedFile I returnred return fileStoredInDataBase;
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
        Optional<User> optionalUser = userService.getUser(dto.username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String password = user.getPassword();
            String email = user.getEmail();
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
                // solution of the bug was:    in FileStorageInDataBaseServiceImpl.java, public FileStoredInDataBase storeAttachedFile I returnred return fileStoredInDataBase;
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

        } else {
            throw new RecordNotFoundException("User id does not exist");
        }
    }
}




