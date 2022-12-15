//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git

package com.novi.fassignment.controllers;

import com.novi.fassignment.controllers.dto.MusicFileStoredInDataBaseDto;
import com.novi.fassignment.exceptions.FileStorageException;
import com.novi.fassignment.utils.Checks;
import com.novi.fassignment.messages.ResponseFile;
import com.novi.fassignment.messages.ResponseMessage;
import com.novi.fassignment.models.MusicFileStoredInDataBase;
import com.novi.fassignment.services.MusicFileStorageInDataBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class MusicFileStorageInDataBaseController {

    @Autowired
    private MusicFileStorageInDataBaseServiceImpl storageService;

//    @Autowired
     private Checks checks;


    @PostMapping("attached-files/upload-to-database/audio")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {

        // the 3 liners below were added on 25-9-22 but have not been controlled yet
        Boolean isAudio=checks.checkIfAudio(file);
        if (isAudio!=true)
        {throw new FileStorageException("File does not appear to be a valid audio format");}

        String message = "";
        try {
            storageService.store(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("attached-files/get-all-from-database-as-stream/audio")

    public ResponseEntity<List<ResponseFile>> getListFilesAsStream() {
        List<ResponseFile> files = storageService.getAllFilesAsStream().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/audio-files-database/")
                    .buildAndExpand(dbFile.getFileId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }


    @GetMapping("attached-files/get-all-files-from-database/audio")
    public ResponseEntity<List<MusicFileStoredInDataBaseDto>> getResponseFilesAsList() {
        var dtos = new ArrayList<MusicFileStoredInDataBaseDto>();
        var filesStoredInDataBase = storageService.getAllFilesAsList();

        for (MusicFileStoredInDataBase fileStoredInDataBase : filesStoredInDataBase) {
            dtos.add(MusicFileStoredInDataBaseDto.fromFileStoredInDataBase(fileStoredInDataBase));
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("attached-files/get-file-from-database/audio/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable("id") Long id) {
        MusicFileStoredInDataBase fileDB = storageService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileDB.getName())
                .body(fileDB.getData());
    }


    @DeleteMapping("attached-files/delete-file-from-database/audio/{id}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable("id") long id) {
        try {
            storageService.deleteFileStoredInDataBaseById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("attached-files/delete-all-files-from-database/audio")
    public ResponseEntity<HttpStatus> deleteAllFiles() {
        try {
            storageService. deleteAllFileStoredInDataBase();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}