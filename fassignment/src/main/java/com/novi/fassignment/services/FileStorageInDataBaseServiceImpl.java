//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git
package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadRequestDto;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.FileStorageInDataBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileStorageInDataBaseServiceImpl {

    @Autowired
    FileStorageInDataBaseRepository fileStorageInDataBaseRepository;

    @Autowired
    NoviMethod1FileUploadService noviFileUploadService;

    //the method below is finally not used but kept as reference
    public Boolean checkIfAudio(MultipartFile multipartFile) {
        String contentTypeOfGivenFile = multipartFile.getContentType();
        Boolean isAudio=false;
        // If you want a fixed size list.
        List<String> list1 = Arrays.asList("audio/basic", "audio/midi", "audio/mpeg", "audio/x-aiff", "audio/x-mpegurl", "audio/x-pn-realaudio", "audio/x-wav");
        // If you want the list to be mutable
        List<String> list2 = new ArrayList<>(list1);
        for (String contentType : list2) {

            if (contentType.equals(contentTypeOfGivenFile)) {
                isAudio = true;
            }
        }

        //dubbel check with extension
        String extensionOfGivenFile = multipartFile.getOriginalFilename().split("\\.")[1];
        List<String> list3 = Arrays.asList("mp3", "mp4");
        for (String extension : list3) {

            if (extension.equals(extensionOfGivenFile)) {
                isAudio = true;
            }
        }
        return isAudio;
    }

    //the method below is finally not used but kept as reference
    public String cleanFileName(String fileName) {
        String newFileName = "";
        List<Character> fileNameAsCharactersList = new ArrayList<>();

        for (char ch: fileName.toCharArray()) {
            fileNameAsCharactersList.add(ch);
        }
        //System.out.println(fileNameAsCharactersList);
        List<Character> forbiddenCharacters = Arrays.asList(' ',',');

        for (int i = 0; i < fileNameAsCharactersList.size(); i++) {
            Character char1 = fileNameAsCharactersList.get(i);
            for (int j = 0; j < forbiddenCharacters.size(); j++) {
                Character char2 = forbiddenCharacters.get(j);
                if (char1==char2){char1='_';}
            }
            newFileName=newFileName+char1;
        }

        return newFileName;

    }


    public Resource  changeName(MultipartFile multipartFile,String newFileName){
        noviFileUploadService.saveAs(multipartFile, newFileName);
        Resource resource=noviFileUploadService.load(newFileName);
        return resource;
    };


    public FileStoredInDataBase store(MultipartFile file) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase(fileName, file.getContentType(), file.getBytes());
        FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase();
        fileStoredInDataBase.setName(fileName);
        fileStoredInDataBase.setType(file.getContentType());
        fileStoredInDataBase.setData(file.getBytes());
        fileStoredInDataBase.setQuestion(null);
        fileStoredInDataBase.setAnswer(null);
        fileStoredInDataBase.setPainting(null);
        //fileStoredInDataBase.setFileId(Long.valueOf(999));//test
        fileStoredInDataBase.setBytesInDatabaseUrl(null);
        //added 13-4-22: store multipartfile also as complete blob on disc
        //fileStoredInDataBase.setFileOnDiskUrl(fileUploadService.uploadFileAndReturnStorageLocationAbsolutePath(file));

        //added 16-4-22: store multipartfile also as complete blob on disc following method Novi
        NoviMethod1FileUploadRequestDto noviMethod1FileUploadRequestDto= new NoviMethod1FileUploadRequestDto();
        noviMethod1FileUploadRequestDto.setTitle(fileName);
        noviMethod1FileUploadRequestDto.setDescription(fileName);
        noviMethod1FileUploadRequestDto.setFile(file);

        Long fileOnDiskId=noviFileUploadService.uploadFile(noviMethod1FileUploadRequestDto);
        //NoviMethod1FileUploadResponseDto noviMethod1FileUploadResponseDto=noviFileUploadService.getFileById(fileOnDiskId);
        fileStoredInDataBase.setFileOnDiskId(fileOnDiskId);
        String fileOnDiskUrl="http://localhost:8080/filesOnDisk/"+Long. toString(fileOnDiskId);
        fileStoredInDataBase.setFileOnDiskUrl(fileOnDiskUrl);

        return fileStorageInDataBaseRepository.save(fileStoredInDataBase);

    }



    public FileStoredInDataBase storeAttachedFile(FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto) throws IOException {
        MultipartFile multipartFile = fileStoredInDataBaseInputDto.getFile();
        FileStoredInDataBase fileStoredInDataBase=store(multipartFile);
        fileStoredInDataBase.setQuestion(fileStoredInDataBaseInputDto.getQuestion());
        fileStoredInDataBase.setAnswer(fileStoredInDataBaseInputDto.getAnswer());
        fileStoredInDataBase.setPainting(fileStoredInDataBaseInputDto.getPainting());

        return fileStorageInDataBaseRepository.save(fileStoredInDataBase);

    }
    public FileStoredInDataBase getFile(Long id) {
        return fileStorageInDataBaseRepository.findById(id).get();
    }

    public Stream<FileStoredInDataBase> getAllFilesAsStream() {return fileStorageInDataBaseRepository.findAll().stream();}
    public List<FileStoredInDataBase> getAllFilesAsList() { return fileStorageInDataBaseRepository.findAll(); }
    public List<FileStoredInDataBase> getAllFilesByDescId() { return fileStorageInDataBaseRepository.findAll(Sort.by("fileId").descending()); }
    public List<FileStoredInDataBase> findFileStoredInDataBaseByQuestionId(Long questionId) { return fileStorageInDataBaseRepository.findByQuestionId(questionId); }
    public void deleteFileStoredInDataBaseById(Long FileId) { fileStorageInDataBaseRepository.deleteById(FileId); }
    public void deleteAllFileStoredInDataBase() { fileStorageInDataBaseRepository.deleteAll(); }


}