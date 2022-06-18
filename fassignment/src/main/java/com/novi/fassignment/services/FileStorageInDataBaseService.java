package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.models.FileStoredInDataBase;
import org.apache.maven.model.Resource;
import org.springframework.web.multipart.MultipartFile;
//import com.novi.fassignment.models.Answer;
//import com.novi.fassignment.models.Painting;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface FileStorageInDataBaseService {

    Boolean checkIfAudio(MultipartFile multipartFile);
    String cleanFileName(String fileName);
    Resource changeName(MultipartFile multipartFile, String newFileName);
    FileStoredInDataBase storeAttachedFile(FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto);
    FileStoredInDataBase getFile(Long id);
    Stream<FileStoredInDataBase> getAllFilesAsStream();
    List<FileStoredInDataBase> getAllFilesAsList();
    public List<FileStoredInDataBase> getAllFilesByDescId();
    public List<FileStoredInDataBase> findFileStoredInDataBaseByQuestionId(Long questionId);
    public void deleteFileStoredInDataBaseById(Long FileId);
    public void deleteAllFileStoredInDataBase();

}