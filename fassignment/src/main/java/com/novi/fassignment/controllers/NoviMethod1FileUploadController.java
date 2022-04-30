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
//@RequestMapping("api/user")
@CrossOrigin
public class NoviMethod1FileUploadController {

    @Autowired
    NoviMethod1FileUploadService methode1Service;

    @Autowired
    BezkoderMethod1FileUploadService bezkoderMethodService;


    @GetMapping("api/user/get-files-data-from-disk")
    public ResponseEntity<Object> getFiles() {
        Iterable<NoviMethod1FileStoredOnDisk> files = methode1Service.getFiles();
        return ResponseEntity.ok().body(files);
    }

    //version https://www.bezkoder.com/spring-boot-file-upload/
    /*    @GetMapping("/files")
   public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();
            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }*/

    //version https://www.bezkoder.com/spring-boot-file-upload/
    @GetMapping("api/user/get-files-data-from-disk-bezkoder")
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


/*    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }*/


    @GetMapping("api/user/get-file-data-from-disk-bezkoder/{id}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable long id) {
        Resource file = bezkoderMethodService.load(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }



/*    @GetMapping("api/user/get-file-data-from-disk/{id}")
    public ResponseEntity<Object> getFileInfo(@PathVariable long id) {
        NoviMethod1FileUploadResponseDto response = methode1Service.getFileById(id);
        return ResponseEntity.ok().body(response);
    }*/

    @GetMapping("api/user/download-file-from-disk/{id}")
    public ResponseEntity downloadFile(@PathVariable long id) {
        Resource resource = methode1Service.downloadFile(id);
        String mediaType = "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping(value = "api/user/upload-file-to-disk",
//            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Object> uploadFile(NoviMethod1FileUploadRequestDto method1Dto) {
        long newId = methode1Service.uploadFile(method1Dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newId).toUri();

        return ResponseEntity.created(location).body(location);
    }

    @DeleteMapping("api/user/delete-file-from-disk/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable long id) {
        methode1Service.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

}

