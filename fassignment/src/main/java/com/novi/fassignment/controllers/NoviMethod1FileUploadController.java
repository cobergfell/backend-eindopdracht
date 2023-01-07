package com.novi.fassignment.controllers;


import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadRequestDto;
import com.novi.fassignment.models.NoviMethod1FileStoredOnDisk;
import com.novi.fassignment.services.NoviMethod1FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


@RestController

@CrossOrigin
public class NoviMethod1FileUploadController {

    @Autowired
    NoviMethod1FileUploadService methode1Service;


    @GetMapping("filesOnDisk")
    public ResponseEntity<Object> getFiles() {
        Iterable<NoviMethod1FileStoredOnDisk> files = methode1Service.getFiles();
        return ResponseEntity.ok().body(files);
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

