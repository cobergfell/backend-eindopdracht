//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git

package com.novi.fassignment.services;


import com.novi.fassignment.controllers.NoviMethod1FileUploadController;
import com.novi.fassignment.controllers.dto.MusicFileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadRequestDto;
import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadResponseDto;
import com.novi.fassignment.exceptions.FileStorageException;
import com.novi.fassignment.models.MusicFileStoredInDataBase;
import com.novi.fassignment.models.MusicFileStoredInDataBase;
import com.novi.fassignment.repositories.MusicFileStorageInDataBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class MusicFileStorageInDataBaseServiceImpl {

    @Autowired
    private MusicFileStorageInDataBaseRepository fileStorageInDataBaseRepository;

    @Autowired
    //private FilesStorageServiceImpl fileStorageOnDiscService;//we use it to temporary copy files om disc to change files names
    FilesStorageService fileStorageOnDiscService;//we use it to temporary copy files om disc to change files names

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    NoviMethod1FileUploadService noviFileUploadService;

    @Autowired
    BezkoderMethod1FileUploadService bezkoderFileUploadService;




    public Resource changeName(MultipartFile multipartFile, String newFileName){
        fileStorageOnDiscService.saveAs(multipartFile, newFileName);
        Resource resource=fileStorageOnDiscService.load(newFileName);
        return resource;
    };



    public MusicFileStoredInDataBase store(MultipartFile file) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase(fileName, file.getContentType(), file.getBytes());
        MusicFileStoredInDataBase fileStoredInDataBase = new MusicFileStoredInDataBase();
        fileStoredInDataBase.setName(fileName);
        fileStoredInDataBase.setDescription(fileName);
        fileStoredInDataBase.setType(file.getContentType());
        fileStoredInDataBase.setData(file.getBytes());
        fileStoredInDataBase.setQuestion(null);
        fileStoredInDataBase.setAnswer(null);
        fileStoredInDataBase.setPainting(null);
        fileStoredInDataBase.setBytesInDatabaseUrl(null);
        //added 13-4-22: store multipartfile also as complete blob on disc
        //fileStoredInDataBase.setFileOnDiskUrl(fileUploadService.uploadFileAndReturnStorageLocationAbsolutePath(file));

        try {

            //added 16-4-22: store multipartfile also as complete blob on disc following method Novi
            NoviMethod1FileUploadRequestDto noviMethod1FileUploadRequestDto= new NoviMethod1FileUploadRequestDto();
            noviMethod1FileUploadRequestDto.setTitle(fileName);
            noviMethod1FileUploadRequestDto.setDescription(fileName);
            noviMethod1FileUploadRequestDto.setFile(file);

            //NoviMethod1FileUploadResponseDto noviMethod1FileUploadResponseDto=noviFileUploadService.getFileById(fileOnDiskId);
            Long fileOnDiskId=noviFileUploadService.uploadFile(noviMethod1FileUploadRequestDto);
            //Long fileOnDiskId=Long.valueOf(999);
            fileStoredInDataBase.setFileOnDiskId(fileOnDiskId);
            String fileOnDiskUrl="http://localhost:8080/api/user/download-file-from-disk/"+Long. toString(fileOnDiskId);
            //fileStoredInDataBase.setFile_on_disk_url(fileOnDiskUrl);
            fileStoredInDataBase.setFileOnDiskUrl(fileOnDiskUrl);

            return fileStorageInDataBaseRepository.save(fileStoredInDataBase);
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        }

    }

    public MusicFileStoredInDataBase storeAttachedFile(MusicFileStoredInDataBaseInputDto fileStoredInDataBaseInputDto) throws IOException {
        MultipartFile multipartFile = fileStoredInDataBaseInputDto.getFile();
        MusicFileStoredInDataBase fileStoredInDataBase=store(multipartFile);
        fileStoredInDataBase.setQuestion(fileStoredInDataBaseInputDto.getQuestion());
        fileStoredInDataBase.setAnswer(fileStoredInDataBaseInputDto.getAnswer());
        fileStoredInDataBase.setPainting(fileStoredInDataBaseInputDto.getPainting());

        return fileStorageInDataBaseRepository.save(fileStoredInDataBase);
    }


    public MusicFileStoredInDataBase getFile(Long id) {
        return fileStorageInDataBaseRepository.findById(id).get();
    }
    public Stream<MusicFileStoredInDataBase> getAllFilesAsStream() {return fileStorageInDataBaseRepository.findAll().stream();}
    public List<MusicFileStoredInDataBase> getAllFilesAsList() { return fileStorageInDataBaseRepository.findAll(); }
    public List<MusicFileStoredInDataBase> getAllFilesByDescId() { return fileStorageInDataBaseRepository.findAll(Sort.by("fileId").descending()); }
    public List<MusicFileStoredInDataBase> findFileStoredInDataBaseByQuestionId(Long questionId) { return fileStorageInDataBaseRepository.findByQuestionId(questionId); }
    public void deleteFileStoredInDataBaseById(Long FileId) { fileStorageInDataBaseRepository.deleteById(FileId); }
    public void deleteAllFileStoredInDataBase() { fileStorageInDataBaseRepository.deleteAll(); }

/*    public FileStoredInDataBase storeQuestionFile(MultipartFile file, Question question) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase(fileName, file.getContentType(), file.getBytes());
        //fileStorageInDataBaseRepository.save(fileStoredInDataBase);
        //fileStoredInDataBase.setQuestion(question);
        List<FileStoredInDataBase> sortedFiles = getAllFilesByDescId();
        FileStoredInDataBase mostRecentFile = sortedFiles.get(0);
        mostRecentFile.setQuestion(question);
        return fileStorageInDataBaseRepository.save(fileStoredInDataBase);
    }*/


/*    public void storeQuestionFile(MultipartFile file, Question question) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase(fileName, file.getContentType(), file.getBytes());
        fileStorageInDataBaseRepository.save(fileStoredInDataBase);
        //fileStoredInDataBase.setQuestion(question);
        List<FileStoredInDataBase> sortedFiles = getAllFilesByDescId();
        FileStoredInDataBase mostRecentFile = sortedFiles.get(0);
        mostRecentFile.setQuestion(question);
        //return fileStorageInDataBaseRepository.save(fileStoredInDataBase);
    }*/

/*    public FileStoredInDataBase storeAnswerFile(MultipartFile file, Answer answer) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase(fileName, file.getContentType(), file.getBytes());
        //fileStorageInDataBaseRepository.save(fileStoredInDataBase);
        //fileStoredInDataBase.setAnswer(answer);
        List<FileStoredInDataBase> sortedFiles = getAllFilesByDescId();
        FileStoredInDataBase mostRecentFile = sortedFiles.get(0);
        mostRecentFile.setAnswer(answer);

        return fileStorageInDataBaseRepository.save(fileStoredInDataBase);
    }*/


/*    public void storeAnswerFile(MultipartFile file, Answer answer) throws IOException {
        String message = "";
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase(fileName, file.getContentType(), file.getBytes());
            fileStorageInDataBaseRepository.save(file);
            List<FileStoredInDataBase> sortedFiles = getAllFilesByDescId();
            FileStoredInDataBase mostRecentFile = sortedFiles.get(0);
            mostRecentFile.setAnswer(answer);

            message = "Uploaded the files successfully: ";
        } catch (Exception e) {
            message = "Fail to upload files!";
        }

    }*/



}