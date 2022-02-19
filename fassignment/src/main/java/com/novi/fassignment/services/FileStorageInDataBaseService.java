//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git

package com.novi.fassignment.services;



import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.MusicPiece;
import com.novi.fassignment.repositories.FileStorageInDataBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileStorageInDataBaseService {

    @Autowired
    private FileStorageInDataBaseRepository fileStorageInDataBaseRepository;

    @Autowired
    private FilesStorageServiceImpl fileStorageOnDiscService;//we use it to temporary copy files om disc to change files names


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
        fileStorageOnDiscService.saveAs(multipartFile, newFileName);
        Resource resource=fileStorageOnDiscService.load(newFileName);
        return resource;
    };



/*    public FileStoredInDataBase store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase(fileName, file.getContentType(), file.getBytes());

        return fileStorageInDataBaseRepository.save(fileStoredInDataBase);
    }*/

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
        fileStoredInDataBase.setMusicPiece(null);


        return fileStorageInDataBaseRepository.save(fileStoredInDataBase);
    }



    public FileStoredInDataBase storeAttachedFile(FileStoredInDataBaseInputDto fileStoredInDataBaseInputDto) throws IOException {
        MultipartFile multipartFile = fileStoredInDataBaseInputDto.getFile();
        FileStoredInDataBase fileStoredInDataBase=store(multipartFile);
        fileStoredInDataBase.setQuestion(fileStoredInDataBaseInputDto.getQuestion());
        fileStoredInDataBase.setAnswer(fileStoredInDataBaseInputDto.getAnswer());
        fileStoredInDataBase.setPainting(fileStoredInDataBaseInputDto.getPainting());
        fileStoredInDataBase.setMusicPiece(fileStoredInDataBaseInputDto.getMusicPiece());


        /*
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Boolean isAudio=checkIfAudio(multipartFile);
        String newFileName=cleanFileName(fileName);
        Resource resource=changeName(multipartFile,newFileName);
        URL url=resource.getURL();
        String newFilePath=url.getFile();
        File file = new File(newFilePath);
*/

        /*        if (isAudio==true){
            //String fileNameWithoutExtension=file.getOriginalFilename().split("\\.")[1];
            String newFileName=cleanFileName(fileName);
            MultipartFile fileWithNewName=changeName(multipartFile,newFileName);
        }*/

        return fileStoredInDataBase;
    }

/*    public FileStoredInDataBase storeMusicPieceFile(MultipartFile file, MusicPiece musicPiece) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase(fileName, file.getContentType(), file.getBytes());
        fileStoredInDataBase.setMusicPiece(musicPiece);

        return fileStorageInDataBaseRepository.save(fileStoredInDataBase);
    }*/


    public FileStoredInDataBase getFile(Long id) {
        return fileStorageInDataBaseRepository.findById(id).get();
    }

/*    //these two lines below where an experiment to override storageService.store(file) but it does not work
    public FileStoredInDataBase store(MultipartFile file,Long questionID) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileStoredInDataBase fileStoredInDataBase = new FileStoredInDataBase(fileName, file.getContentType(), file.getBytes());

        return fileStorageInDataBaseRepository.save(fileStoredInDataBase,questionID);
    }*/

    public Stream<FileStoredInDataBase> getAllFilesAsStream() {return fileStorageInDataBaseRepository.findAll().stream();}
    public List<FileStoredInDataBase> getAllFilesAsList() { return fileStorageInDataBaseRepository.findAll(); }
    public List<FileStoredInDataBase> getAllFilesByDescId() { return fileStorageInDataBaseRepository.findAll(Sort.by("fileId").descending()); }
    public List<FileStoredInDataBase> findFileStoredInDataBaseByQuestionId(Long questionId) { return fileStorageInDataBaseRepository.findByQuestionId(questionId); }
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