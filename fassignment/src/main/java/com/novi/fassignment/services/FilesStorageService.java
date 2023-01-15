//modified from https://github.com/bezkoder/spring-boot-upload-multipart-files.git

package com.novi.fassignment.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;


public interface FilesStorageService {
    void init();
    void save(MultipartFile file);
    void saveAs(MultipartFile file, String newName);
    //This is an alternative to save (see implementation)
    String store(MultipartFile file);
    Resource load(String filename);
    void deleteAll();
    Stream<Path> loadAll();
}
