//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git

package com.novi.fassignment.controllers;

import com.novi.fassignment.controllers.dto.FileStoredInDataBaseDto;
import com.novi.fassignment.controllers.dto.FileStoredInDataBaseDto;
import com.novi.fassignment.messages.ResponseFile;
import com.novi.fassignment.messages.ResponseMessage;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.services.FileStorageInDataBaseServiceImpl;
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
public class FileStorageInDataBaseController {

    @Autowired
    private FileStorageInDataBaseServiceImpl storageService;

    @PostMapping("filesInDatabase")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
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

    @GetMapping("filesInDatabase")
    public ResponseEntity<List<FileStoredInDataBaseDto>> getResponseFilesAsList() {
        var dtos = new ArrayList<FileStoredInDataBaseDto>();
        var filesStoredInDataBase = storageService.getAllFilesAsList();

        for (FileStoredInDataBase fileStoredInDataBase : filesStoredInDataBase) {
            dtos.add(FileStoredInDataBaseDto.fromFileStoredInDataBase(fileStoredInDataBase));
        }

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("filesInDatabase/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable("id") Long id) {
        FileStoredInDataBase fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileDB.getName())
                .body(fileDB.getData());
    }




    @DeleteMapping("filesInDatabase/{id}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable("id") long id) {
        try {
            storageService.deleteFileStoredInDataBaseById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("filesInDatabase")
    public ResponseEntity<HttpStatus> deleteAllFiles() {
        try {
            storageService. deleteAllFileStoredInDataBase();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    Code snippet below is not used but kept for reference

    @GetMapping("filesInDatabaseAsStream")
    public ResponseEntity<List<ResponseFile>> getListFilesAsStream() {
        List<ResponseFile> files = storageService.getAllFilesAsStream().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/filesInDatabase/")
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


}