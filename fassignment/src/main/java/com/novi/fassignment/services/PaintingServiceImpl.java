package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.*;
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


    private FileStorageInDataBaseServiceImpl fileStorageInDataBaseService;

    @Override
    public Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @Override
    public PaintingDto getPaintingById(Long id) {
        var optionalPainting = paintingRepository.findById(id);
        if (optionalPainting.isPresent()) {
            var painting= optionalPainting.get();
            var dto = new PaintingDto();
            dto=PaintingDto.fromPaintingToDto(painting);
            return dto;
        } else {
            throw new RecordNotFoundException("Painting id does not exist");
        }
    }


    @Override
    public List<PaintingDto> getAllPaintings() {
        var dtos = new ArrayList<PaintingDto>();
        var paintings = paintingRepository.findAll();
        for (Painting painting : paintings) {
            dtos.add(PaintingDto.fromPaintingToDto(painting));
        }
        return dtos;
    }

    @Override
    public List<PaintingDto> getAllPaintingsByAscId() {
        var dtos = new ArrayList<PaintingDto>();
        var paintings = paintingRepository.findAll(Sort.by("paintingId").ascending());

        for (Painting painting : paintings) {
            dtos.add(PaintingDto.fromPaintingToDto(painting));
        }
        return dtos;
    }



    @Override
    public List<Painting> getAllPaintingsByDescId() {
        return paintingRepository.findAll(Sort.by("paintingId").descending());
    }

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
        Optional<User> optionalUser = userService.getUser(dto.username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String password = user.getPassword();
            String email = user.getEmail();
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


            Painting paintingToBuildUp = paintingService.createPaintingWithoutAttachment(painting);

            List<String> fileNames = new ArrayList<>();
            Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
            List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();

            if (dto.files!=null){
                Arrays.asList(dto.files).stream().forEach(theFile -> multipartFiles.add(theFile));

                multipartFiles.stream().forEach(file -> {
                            String message3 = "";
                            try {
                                FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                                fileStoredInDataBaseInputDto.setFile(file);
                                fileStoredInDataBaseInputDto.setPainting(paintingToBuildUp);// when is the database commit triggered following a setPainting command?
                                FileStoredInDataBase fileStoredInDataBase = storageService.storeAttachedFile(fileStoredInDataBaseInputDto);


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
                musicMultipartFiles.stream().forEach(file -> {
                            String message3 = "";
                            try {
                                MusicFileStoredInDataBaseInputDto musicFileStoredInDataBaseInputDto = new MusicFileStoredInDataBaseInputDto();
                                musicFileStoredInDataBaseInputDto.setFile(file);
                                musicFileStoredInDataBaseInputDto.setPainting(paintingToBuildUp);// when is the database commit triggered following a setPainting command?
                                MusicFileStoredInDataBase musicFileStoredInDataBase = musicStorageService.storeAttachedFile(musicFileStoredInDataBaseInputDto);

                                attachedMusicFiles.add(musicFileStoredInDataBase);
                                message3 = "Files successfully added to painting object: " + musicFileNames;
                            } catch (Exception e) {
                                message3 = "Fail to attach files to painting object!";
                            }
                        }
                );
            }

        } else {
            throw new RecordNotFoundException("User id does not exist");
        }

    }


    @Override
    public void updatePainting(PaintingInputDto dto) {

        Optional<Painting> currentPainting = paintingRepository.findById(dto.paintingId);

        if (currentPainting.isPresent()) {
            Painting painting = currentPainting.get();
            Optional<User> optionalUser = userService.getUser(dto.username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String password = user.getPassword();
                String email = user.getEmail();
                User userFromCustomUser = new User();
                userFromCustomUser.setUsername(dto.username);
                userFromCustomUser.setPassword(password);
                userFromCustomUser.setEmail(email);
                painting.setUser(userFromCustomUser);
                painting.setPaintingId(dto.paintingId);
                painting.setTitle(dto.title);
                painting.setArtist(dto.artist);
                painting.setDescription(dto.description);
                painting.setImage(dto.image);
                painting.setDateTimePosted(dto.dateTimePosted);
                painting.setLastUpdate(dto.dateTimePosted);
                painting.setAnswers(dto.answers);
                painting.setQuestions(dto.questions);

                paintingRepository.save(painting);

                if (dto.files != null) {
                    List<String> fileNames = new ArrayList<>();
                    Arrays.asList(dto.files).stream().forEach(file -> {
                        String message = "";
                        try {
                            //storageService.store(file);
                            //storageService.storePaintingFile(file, painting);
                            FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                            fileStoredInDataBaseInputDto.setFile(file);
                            fileStoredInDataBaseInputDto.setPainting(painting);
                            FileStoredInDataBase fileStoredInDataBase = storageService.storeAttachedFile(fileStoredInDataBaseInputDto);


                            fileNames.add(file.getOriginalFilename());
                            message = "Uploaded the files successfully: " + fileNames;
                        } catch (Exception e) {
                            message = "Fail to upload files!";
                        }
                    });

                    Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
                    List<FileStoredInDataBase> sortedFiles = storageService.getAllFilesByDescId();
                    //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedPaintings.toArray();//cast array of objects into array of paintings
                    for (int i = 0; i < fileNames.size(); i++) {
                        FileStoredInDataBase mostRecentFile = sortedFiles.get(i);
                        attachedFiles.add(mostRecentFile);
                    }
                    painting.setFiles(attachedFiles);

                }


                if (dto.audioFiles != null) {
                    List<String> fileNames = new ArrayList<>();
                    Arrays.asList(dto.audioFiles).stream().forEach(file -> {
                        String message = "";
                        try {
                            //storageService.store(file);
                            //storageService.storePaintingFile(file, painting);
                            MusicFileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new MusicFileStoredInDataBaseInputDto();
                            fileStoredInDataBaseInputDto.setFile(file);
                            fileStoredInDataBaseInputDto.setPainting(painting);
                            MusicFileStoredInDataBase fileStoredInDataBase = musicStorageService.storeAttachedFile(fileStoredInDataBaseInputDto);


                            fileNames.add(file.getOriginalFilename());
                            message = "Uploaded the files successfully: " + fileNames;
                        } catch (Exception e) {
                            message = "Fail to upload files!";
                        }
                    });

                    Set<MusicFileStoredInDataBase> attachedFiles = new HashSet<>();
                    List<MusicFileStoredInDataBase> sortedFiles = musicStorageService.getAllFilesByDescId();
                    //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedPaintings.toArray();//cast array of objects into array of answers
                    for (int i = 0; i < fileNames.size(); i++) {
                        MusicFileStoredInDataBase mostRecentFile = sortedFiles.get(i);
                        attachedFiles.add(mostRecentFile);
                    }
                    painting.setMusicFiles(attachedFiles);

                }

            } else {
                throw new RecordNotFoundException("User id does not exist");
            }
        } else {
            throw new RecordNotFoundException("Painting does not exist");
        }

    }

}





