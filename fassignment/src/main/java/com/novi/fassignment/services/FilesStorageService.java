//modified from https://github.com/bezkoder/spring-boot-upload-multipart-files.git

package com.novi.fassignment.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    public void init();

    public void save(MultipartFile file);

    public void saveAs(MultipartFile file, String newName);

    //This is an alternative to save (see implementation)
    public String store(MultipartFile file);

    public Resource load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();
}
