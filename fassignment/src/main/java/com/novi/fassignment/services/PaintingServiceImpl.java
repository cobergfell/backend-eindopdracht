package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.MusicFileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.FileStorageInDataBaseRepository;
import com.novi.fassignment.repositories.PaintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class PaintingServiceImpl implements PaintingService {

    @Autowired
    private PaintingRepository paintingRepository;

    @Autowired
    private FileStorageInDataBaseServiceImpl storageService;

    @Autowired
    private MusicFileStorageInDataBaseServiceImpl musicStorageService;

    @Autowired
    private PaintingServiceImpl paintingService;

    @Autowired
    UserService userService;

    @Autowired
    private FileStorageInDataBaseRepository fileStorageInDataBaseRepository;

    @Autowired
    FileUploadService fileUploadService;


    private FileStorageInDataBaseServiceImpl fileStorageInDataBaseService;
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

    public List<Painting> getAllPaintingsByAscId() {
        return paintingRepository.findAll(Sort.by("paintingId").ascending());
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
    public Painting createPaintingWithoutAttachment(Painting painting) { return paintingRepository.save(painting); }

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
        painting.setDescription(dto.description);
        painting.setImage(dto.image);
        painting.setDateTimePosted(dto.dateTimePosted);
        painting.setLastUpdate(dto.dateTimePosted);

//        paintingRepository.save(painting);
//
//        List<Painting> sortedPaintings = paintingService.getAllPaintingsByDescId();
//        //Painting[] sortedPaintingsArray= (Painting[]) sortedPaintings.toArray();//cast array of objects into array of paintings
//        Painting mostRecentPainting = sortedPaintings.get(0);
//        //Painting mostRecentPainting=sortedPaintingsArray[0];
//        Long mostRecentPaintingId = mostRecentPainting.getPaintingId();


        Painting paintingToBuildUp = paintingService.createPaintingWithoutAttachment(painting);

        List<String> fileNames = new ArrayList<>();
        Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
        List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();

        if (dto.files!=null){
            Arrays.asList(dto.files).stream().forEach(theFile -> multipartFiles.add(theFile));
            //MultipartFile extra=multipartFiles.get(multipartFiles.size()-1);
            //multipartFiles.add(extra);//this is a trick because of a bug part 1: you have to repeat the last element and remove it later in order to have setPainting work, I still don't get why
            // solution of the bug was:    in FileStorageInDataBaseServiceImpl.java, public FileStoredInDataBase storeAttachedFile I returnred return fileStoredInDataBase;
            // instead of  return fileStorageInDataBaseRepository.save(fileStoredInDataBase);


            //Arrays.asList(dto.files).stream().forEach(file -> {
            multipartFiles.stream().forEach(file -> {
                        String message3 = "";
                        try {
                            FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                            fileStoredInDataBaseInputDto.setFile(file);
                            fileStoredInDataBaseInputDto.setPainting(paintingToBuildUp);// when is the database commit triggered following a setPainting command?
                            FileStoredInDataBase fileStoredInDataBase = storageService.storeAttachedFile(fileStoredInDataBaseInputDto);
                            //long fileId=fileStoredInDataBase.getFileId();
                            //if (fileId==multipartFiles.size()){storageService.deleteFileStoredInDataBaseById(fileId);}// trick part 2: remove extra file
                            // it took me hours ti find out that this trick to add the last file of the list and remove it
                            // later is the way to associate the last multipart file to the painting
                            // I do not now why it does not work without this trick, think about it later and fix it

                            attachedFiles.add(fileStoredInDataBase);
                            message3 = "Files successfully added to painting object: " + fileNames;
                        } catch (Exception e) {
                            message3 = "Fail to attach files to painting object!";
                        }
                    }

            );

        }


        List<String> musicFileNames = new ArrayList<>();
        Set<MusicFileStoredInDataBase> attachedMusicFiles = new HashSet<>();
        List<MultipartFile> musicMultipartFiles = new ArrayList<MultipartFile>();

        if (dto.audioFiles!=null){
            Arrays.asList(dto.audioFiles).stream().forEach(theFile -> musicMultipartFiles.add(theFile));
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
                            musicFileStoredInDataBaseInputDto.setPainting(paintingToBuildUp);// when is the database commit triggered following a setPainting command?
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

    }


    @Override
    public void updatePainting(PaintingInputDto dto, Painting painting) {

        System.out.print("198 dto"+dto);

        Painting updatedPainting= new Painting();
        Optional<User> user = userService.getUser(dto.username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(dto.username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        updatedPainting.setUser(userFromCustomUser);
        updatedPainting.setPaintingId(dto.paintingId);
        updatedPainting.setTitle(dto.title);
        updatedPainting.setArtist(dto.artist);
        updatedPainting.setDescription(dto.description);
        updatedPainting.setImage(dto.image);
        updatedPainting.setDateTimePosted(dto.dateTimePosted);
        //painting.setDateTimePosted(dto.dateTimePosted.toString());
        updatedPainting.setLastUpdate(dto.dateTimePosted);
        paintingRepository.save(updatedPainting);


        List<String> fileNames = new ArrayList<>();
        if (dto.files!=null){
            Arrays.asList(dto.files).stream().forEach(file -> {
                String message = "";
                try {
                    //storageService.store(file);
                    //storageService.storeQuestionFile(file, question);
                    FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                    fileStoredInDataBaseInputDto.setFile(file);
                    fileStoredInDataBaseInputDto.setPainting(updatedPainting);
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

        //if (dto.musicFiles!=null  ){System.out.println("hello1");}

        if (dto.audioFiles!=null ){
            List<String> musicFileNames = new ArrayList<>();
            Arrays.asList(dto.audioFiles).stream().forEach(file -> {
                String message = "";
                try {
                    //storageService.store(file);
                    //storageService.storeQuestionFile(file, question);
                    MusicFileStoredInDataBaseInputDto musicFileStoredInDataBaseInputDto = new MusicFileStoredInDataBaseInputDto();
                    musicFileStoredInDataBaseInputDto.setFile(file);
                    musicFileStoredInDataBaseInputDto.setPainting(updatedPainting);
                    MusicFileStoredInDataBase musicFileStoredInDataBase=musicStorageService.storeAttachedFile(musicFileStoredInDataBaseInputDto);

                    musicFileNames.add(file.getOriginalFilename());
                    message = "Uploaded the files successfully: " + musicFileNames;
                } catch (Exception e) {
                    message = "Fail to upload files!";
                }
            });

            Set<MusicFileStoredInDataBase> attachedMusicFiles = new HashSet<>();
            List<MusicFileStoredInDataBase> sortedMusicFiles = musicStorageService.getAllFilesByDescId();
            //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedQuestions.toArray();//cast array of objects into array of questions
            for (int i = 0; i < musicFileNames.size(); i++) {
                MusicFileStoredInDataBase mostRecentFile = sortedMusicFiles.get(i);
                attachedMusicFiles.add(mostRecentFile);
            }
            painting.setMusicFiles(attachedMusicFiles);

            }
        else{
            painting.setMusicFiles(null);
        }

    }

}





