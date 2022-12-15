package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadRequestDto;
import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadResponseDto;
import com.novi.fassignment.models.NoviMethod1FileStoredOnDisk;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public interface NoviMethod1FileUploadService {

    void init();
    void save(MultipartFile file);
    void saveAs(MultipartFile file, String newName);
    //This is an alternative to save (see implementation)
    String store(MultipartFile file);
    Resource load(String filename);
    Stream<Path> loadAll();
    Iterable<NoviMethod1FileStoredOnDisk> getFiles();
    void deleteAll();
    boolean fileExistsById(Long id);
    Long uploadFile(NoviMethod1FileUploadRequestDto method1Dto);
    void deleteFile(Long id);
    Resource downloadFile(Long id);

}