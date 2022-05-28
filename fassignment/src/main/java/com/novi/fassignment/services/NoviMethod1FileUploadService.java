package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadRequestDto;
import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadResponseDto;
import com.novi.fassignment.models.NoviMethod1FileStoredOnDisk;
import org.springframework.core.io.Resource;

import java.util.Optional;

public interface NoviMethod1FileUploadService {

    void init();
    Iterable<NoviMethod1FileStoredOnDisk> getFiles();
    //NoviMethod1FileUploadResponseDto getFileById(long id);
    boolean fileExistsById(Long id);
    Long uploadFile(NoviMethod1FileUploadRequestDto method1Dto);
    void deleteFile(Long id);
    Resource downloadFile(Long id);

}