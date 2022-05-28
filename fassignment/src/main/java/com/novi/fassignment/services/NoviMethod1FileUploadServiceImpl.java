package com.novi.fassignment.services;



import com.novi.fassignment.controllers.FilesControllerNotUsedAnymoreInThisApp;
import com.novi.fassignment.controllers.NoviMethod1FileUploadController;
import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadRequestDto;
import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadResponseDto;
import com.novi.fassignment.exceptions.FileStorageException;
import com.novi.fassignment.exceptions.RecordNotFoundException;

import com.novi.fassignment.models.NoviMethod1FileStoredOnDisk;
import com.novi.fassignment.repositories.NoviMethod1FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class NoviMethod1FileUploadServiceImpl implements NoviMethod1FileUploadService {

    @Value("${app.upload.dir:${user.home}}")
    private String uploadDirectory;  // relative to root
    //private final Path uploads = Paths.get("uploads");
    //private final Path uploads = Paths.get("C:\\Users\\Gebruiker\\Test\\uploads");
    //private final Path uploads2 = Paths.get("C:\\Users\\Gebruiker\\Test\\uploads2");
    private final Path uploads = Paths.get("D:\\Users\\Gebruiker\\Test\\uploads");
    //private final Path uploads = Paths.get("D:\\Data\\NOVI\\eindopdracht\\fassignment-frontend\\src\\assets");

    @Autowired
    private NoviMethod1FileUploadRepository repository;

    @Override
    public void init() {
        try {
            Files.createDirectory(uploads);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Iterable<NoviMethod1FileStoredOnDisk> getFiles() {
        return repository.findAll();
    }

    public Long uploadFile(NoviMethod1FileUploadRequestDto method1Dto) {

        MultipartFile file = method1Dto.getFile();

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        Path copyLocation = this.uploads.resolve(file.getOriginalFilename());
/*        boolean exists = Files.exists(copyLocation);
        if (exists){
            try {
                String[] splitted = originalFilename.split("\\.");
                String extensionRemoved = splitted[0];
                String extension = splitted[1];
                String newName=extensionRemoved+"_bla";
                copyLocation = this.uploads.resolve(newName+"."+extension);
            }catch (Exception e) {
                throw new FileStorageException("File " + originalFilename + " already exists!");
            }
        } */

        try {
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            NoviMethod1FileStoredOnDisk newFileToStore = new NoviMethod1FileStoredOnDisk();
            newFileToStore.setFileName(originalFilename);
            newFileToStore.setLocation(copyLocation.toString());
            newFileToStore.setTitle(method1Dto.getTitle());
            newFileToStore.setDescription(method1Dto.getDescription());
            //NoviMethod1FileStoredOnDisk saved = repository.save(newFileToStore);
            NoviMethod1FileStoredOnDisk saved = repository.save(newFileToStore);
            return saved.getId();
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + originalFilename + ". Please try again!");
        }
    }


    @Override
    public void deleteFile(Long id) {
        Optional<NoviMethod1FileStoredOnDisk> stored = repository.findById(id);

        if (stored.isPresent()) {
            String filename = stored.get().getFileName();
            Path location = this.uploads.resolve(filename);
            try {
                Files.deleteIfExists(location);
            }
            catch (IOException ex) {
                throw new RuntimeException("File not found");
            }

            repository.deleteById(id);
        }
        else {
            throw new RecordNotFoundException();
        }
    }


    @Override
    public boolean fileExistsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Resource downloadFile(Long id) {
        Optional<NoviMethod1FileStoredOnDisk> stored = repository.findById(id);

        if (stored.isPresent()) {
            String filename = stored.get().getFileName();
            Path path = this.uploads.resolve(filename);

            Resource resource = null;
            try {
                resource = new UrlResource(path .toUri());
                return resource;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else {
            throw new RecordNotFoundException();
        }

        return null;
    }

}