package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.MusicPieceInputDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.MusicPiece;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.MusicPieceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MusicPieceServiceImpl implements MusicPieceService {

    @Autowired
    private MusicPieceRepository musicPieceRepository;

    @Autowired
    private FileStorageInDataBaseService storageService;

    @Autowired
    private MusicPieceServiceImpl musicPieceService;

    @Autowired
    UserService userService;

    private FileStorageInDataBaseService fileStorageInDataBaseService;
    //private List<MusicPiece> musicPieces = new ArrayList<>();

/*    @Autowired
    public MusicPieceServiceImpl(MusicPieceRepository musicPieceRepository) {
        this.musicPieceRepository = musicPieceRepository;
    }*/

    @Override
    public MusicPiece getMusicPieceById(Long id) {
        var optionalMusicPiece = musicPieceRepository.findById(id);
        if (optionalMusicPiece.isPresent()) {
            return optionalMusicPiece.get();
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }


    public List<MusicPiece> getAllMusicPieces() {
        return musicPieceRepository.findAll();
    }

    public List<MusicPiece> getAllMusicPiecesByDescId() {
        return musicPieceRepository.findAll(Sort.by("musicPieceId").descending());
    }

/*    @Override
    public MusicPiece createMusicPieceWithoutAttachment(MusicPiece musicPiece) { return musicPieceRepository.save(musicPiece); }*/


    @Override
    public void deleteMusicPieceById(Long id) {
        var optionalMusicPiece = musicPieceRepository.findById(id);
        if (optionalMusicPiece.isPresent()) {
            musicPieceRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }

    @Override
    public void deleteAllMusicPieces() {
        musicPieceRepository.deleteAll();
    }


    @Override
    public void createMusicPiece(MusicPieceInputDto dto) {

        MusicPiece musicPiece = new MusicPiece();
        Optional<User> user = userService.getUser(dto.username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(dto.username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        musicPiece.setUser(userFromCustomUser);
        musicPiece.setTitle(dto.title);
        musicPiece.setArtist(dto.artist);
        // musicPiece.setDatePosted(datePosted.toString());
        musicPiece.setDescription(dto.description);
        musicPiece.setCreationYear(dto.creationYear);



        musicPieceRepository.save(musicPiece);

        List<MusicPiece> sortedMusicPieces = musicPieceService.getAllMusicPiecesByDescId();
        //MusicPiece[] sortedMusicPiecesArray= (MusicPiece[]) sortedMusicPieces.toArray();//cast array of objects into array of musicPieces
        MusicPiece mostRecentMusicPiece = sortedMusicPieces.get(0);
        //MusicPiece mostRecentMusicPiece=sortedMusicPiecesArray[0];
        Long mostRecentMusicPieceId = mostRecentMusicPiece.getMusicPieceId();

        //storageService.store(file);

        List<String> fileNames = new ArrayList<>();

        Arrays.asList(dto.files).stream().forEach(file -> {
            String message2 = "";
            try {
                //storageService.store(file);
                //storageService.storeQuestionFile(file, question);
                FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                fileStoredInDataBaseInputDto.setFile(file);
                fileStoredInDataBaseInputDto.setMusicPiece(musicPiece);
                FileStoredInDataBase fileStoredInDataBase=storageService.storeAttachedFile(fileStoredInDataBaseInputDto);

                fileNames.add(file.getOriginalFilename());
                message2 = "Uploaded the files successfully: " + fileNames;
            } catch (Exception e) {
                message2 = "Fail to upload files!";
            }
        });

        Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
        List<FileStoredInDataBase> sortedFiles = storageService.getAllFilesByDescId();
        //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedMusicPieces.toArray();//cast array of objects into array of musicPieces
        for (int i = 0; i < fileNames.size(); i++) {
            FileStoredInDataBase mostRecentFile = sortedFiles.get(i);
            attachedFiles.add(mostRecentFile);
        }

        mostRecentMusicPiece.setFiles(attachedFiles);
    }


    @Override
    public void updateMusicPiece(MusicPieceInputDto dto, MusicPiece musicPiece) {

        Optional<User> user = userService.getUser(dto.username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(dto.username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        musicPiece.setUser(userFromCustomUser);
        musicPiece.setTitle(dto.title);
        musicPiece.setArtist(dto.artist);
        // musicPiece.setDatePosted(datePosted.toString());
        musicPiece.setDescription(dto.description);
        musicPiece.setCreationYear(dto.creationYear);
        musicPieceRepository.save(musicPiece);


        List<String> fileNames = new ArrayList<>();

        Arrays.asList(dto.files).stream().forEach(file -> {
            String message = "";
            try {
                //storageService.store(file);
                //storageService.storeQuestionFile(file, question);
                FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto = new FileStoredInDataBaseInputDto();
                fileStoredInDataBaseInputDto.setFile(file);
                fileStoredInDataBaseInputDto.setMusicPiece(musicPiece);
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

        musicPiece.setFiles(attachedFiles);

    }



}


