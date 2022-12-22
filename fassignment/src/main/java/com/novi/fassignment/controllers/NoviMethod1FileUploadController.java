package com.novi.fassignment.controllers;


import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadRequestDto;
import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadResponseDto;
import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.models.FileInfo;
import com.novi.fassignment.models.NoviMethod1FileStoredOnDisk;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.services.BezkoderMethod1FileUploadService;
import com.novi.fassignment.services.NoviMethod1FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController

@CrossOrigin
public class NoviMethod1FileUploadController {

    @Autowired
    NoviMethod1FileUploadService methode1Service;

    @Autowired
    BezkoderMethod1FileUploadService bezkoderMethodService;


    @GetMapping("filesOnDisk")
    public ResponseEntity<Object> getFiles() {
        Iterable<NoviMethod1FileStoredOnDisk> files = methode1Service.getFiles();
        return ResponseEntity.ok().body(files);
    }
    @GetMapping("filesOnDiskBezkoderMethod")
    public ResponseEntity<List<FileInfo>>  getFilesVersionBezkoder() {
        List<FileInfo> fileInfos = new ArrayList<FileInfo>();
                Iterable<NoviMethod1FileStoredOnDisk> files = methode1Service.getFiles();
        for (NoviMethod1FileStoredOnDisk file : files) {
            String filename = file.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(NoviMethod1FileUploadController.class, "getFile", file.getFileName().toString()).build().toString();
            fileInfos.add(new FileInfo(filename, url));

        }
        return ResponseEntity.ok().body(fileInfos);
    }


    @GetMapping("filesOnDiskBezkoderMethod/{id}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable long id) {
        Resource file = bezkoderMethodService.load(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @GetMapping("filesOnDisk/{id}")
    public ResponseEntity downloadFile(@PathVariable long id) {
        Resource resource = methode1Service.downloadFile(id);
        String mediaType = "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping(value = "filesOnDisk",
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Object> uploadFile(NoviMethod1FileUploadRequestDto method1Dto) {
        long newId = methode1Service.uploadFile(method1Dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newId).toUri();

        return ResponseEntity.created(location).body(location);
    }



    @DeleteMapping("filesOnDisk/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable long id) {
        methode1Service.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

}

