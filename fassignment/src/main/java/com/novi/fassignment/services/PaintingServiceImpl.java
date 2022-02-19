package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.PaintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaintingServiceImpl implements PaintingService {

    @Autowired
    private PaintingRepository paintingRepository;

    @Autowired
    private FileStorageInDataBaseService storageService;

    @Autowired
    private PaintingServiceImpl paintingService;

    @Autowired
    UserService userService;

    private FileStorageInDataBaseService fileStorageInDataBaseService;
    //private List<Painting> paintings = new ArrayList<>();

/*    @Autowired
    public PaintingServiceImpl(PaintingRepository paintingRepository) {
        this.paintingRepository = paintingRepository;
    }*/

    @Override
    public Painting getPaintingById(Long id) {
        var optionalPainting = paintingRepository.findById(id);
        if (optionalPainting.isPresent()) {
            return optionalPainting.get();
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }


    public List<Painting> getAllPaintings() {
        return paintingRepository.findAll();
    }

    public List<Painting> getAllPaintingsByDescId() {
        return paintingRepository.findAll(Sort.by("paintingId").descending());
    }

/*    @Override
    public Painting createPaintingWithoutAttachment(Painting painting) { return paintingRepository.save(painting); }*/


    @Override
    public void deletePaintingById(Long id) {
        var optionalPainting = paintingRepository.findById(id);
        if (optionalPainting.isPresent()) {
            paintingRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }

    @Override
    public void deleteAllPaintings() {
        paintingRepository.deleteAll();
    }


    @Override
    public void createPainting(PaintingInputDto dto) {

        Painting painting = new Painting();
        Optional<User> user = userService.getUser(dto.username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(dto.username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        painting.setUser(userFromCustomUser);
        painting.setTitle(dto.title);
        painting.setArtist(dto.artist);
        // painting.setDatePosted(datePosted.toString());
        painting.setDescription(dto.description);
        painting.setCreationYear(dto.creationYear);



        paintingRepository.save(painting);

        List<Painting> sortedPaintings = paintingService.getAllPaintingsByDescId();
        //Painting[] sortedPaintingsArray= (Painting[]) sortedPaintings.toArray();//cast array of objects into array of paintings
        Painting mostRecentPainting = sortedPaintings.get(0);
        //Painting mostRecentPainting=sortedPaintingsArray[0];
        Long mostRecentPaintingId = mostRecentPainting.getPaintingId();

        //storageService.store(file);

        List<String> fileNames = new ArrayList<>();

        Arrays.asList(dto.files).stream().forEach(file -> {
            String message2 = "";
            try {
                //storageService.store(file);
                //storageService.storeQuestionFile(file, question);
                FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                fileStoredInDataBaseInputDto.setFile(file);
                fileStoredInDataBaseInputDto.setPainting(painting);
                FileStoredInDataBase fileStoredInDataBase=storageService.storeAttachedFile(fileStoredInDataBaseInputDto);

                fileNames.add(file.getOriginalFilename());
                message2 = "Uploaded the files successfully: " + fileNames;
            } catch (Exception e) {
                message2 = "Fail to upload files!";
            }
        });

        Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
        List<FileStoredInDataBase> sortedFiles = storageService.getAllFilesByDescId();
        //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedPaintings.toArray();//cast array of objects into array of paintings
        for (int i = 0; i < fileNames.size(); i++) {
            FileStoredInDataBase mostRecentFile = sortedFiles.get(i);
            attachedFiles.add(mostRecentFile);
        }

        mostRecentPainting.setFiles(attachedFiles);
    }


    @Override
    public void updatePainting(PaintingInputDto dto, Painting painting) {

        Optional<User> user = userService.getUser(dto.username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(dto.username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        painting.setUser(userFromCustomUser);
        painting.setTitle(dto.title);
        painting.setArtist(dto.artist);
        // painting.setDatePosted(datePosted.toString());
        painting.setDescription(dto.description);
        painting.setCreationYear(dto.creationYear);
        paintingRepository.save(painting);


        List<String> fileNames = new ArrayList<>();

        Arrays.asList(dto.files).stream().forEach(file -> {
            String message = "";
            try {
                //storageService.store(file);
                //storageService.storeQuestionFile(file, question);
                FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                fileStoredInDataBaseInputDto.setFile(file);
                fileStoredInDataBaseInputDto.setPainting(painting);
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

        painting.setFiles(attachedFiles);

    }



}





