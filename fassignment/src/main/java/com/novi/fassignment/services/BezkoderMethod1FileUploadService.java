package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadRequestDto;
import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadResponseDto;
import com.novi.fassignment.models.NoviMethod1FileStoredOnDisk;
import org.springframework.core.io.Resource;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface BezkoderMethod1FileUploadService {

    void init();
    public void save(MultipartFile file);
    public Resource load(long id);
    public void deleteAll();
    public Stream<Path> loadAll();

}