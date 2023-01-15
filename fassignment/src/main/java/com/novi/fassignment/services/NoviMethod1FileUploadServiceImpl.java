package com.novi.fassignment.services;



import com.novi.fassignment.controllers.dto.NoviMethod1FileUploadRequestDto;
import com.novi.fassignment.exceptions.FileStorageException;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.exceptions.StorageException;
import com.novi.fassignment.models.NoviMethod1FileStoredOnDisk;
import com.novi.fassignment.repositories.NoviMethod1FileUploadRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class NoviMethod1FileUploadServiceImpl implements NoviMethod1FileUploadService {


    @Value("${app.upload.dir}")
    private String uploadDir1;

    private String uploadDir=System.getProperty("user.home");
    Path root = Paths.get(uploadDir,"uploads");

    @Autowired
    private NoviMethod1FileUploadRepository repository;

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }


    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void saveAs(MultipartFile file, String newName) {
        //Path saveTo = Paths.get(root + "\\"+newName);
        try {
            Files.copy(file.getInputStream(), this.root.resolve(newName));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1)
                    .filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }


    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Iterable<NoviMethod1FileStoredOnDisk> getFiles() {
        return repository.findAll();
    }

    public Long uploadFile(NoviMethod1FileUploadRequestDto method1Dto) {

        MultipartFile file = method1Dto.getFile();

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        Path copyLocation = this.root.resolve(file.getOriginalFilename());

        try {
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            NoviMethod1FileStoredOnDisk newFileToStore = new NoviMethod1FileStoredOnDisk();
            newFileToStore.setFileName(originalFilename);
            newFileToStore.setLocation(copyLocation.toString());
            newFileToStore.setTitle(method1Dto.getTitle());
            newFileToStore.setDescription(method1Dto.getDescription());
            NoviMethod1FileStoredOnDisk saved = repository.save(newFileToStore);
            return saved.getId();
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + originalFilename + ". Please try again!");
        }
    }

    //This are alternative method to store (to compare with above method) and to retrieve files
    // https://dev.to/nilmadhabmondal/let-s-develop-file-upload-service-from-scratch-using-java-and-spring-boot-3mf1

//    public String store(MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                throw new StorageException("Failed to store empty file.");
//            }
//
//            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//            String uploadedFileName = UUID.randomUUID().toString() + "." + extension;
//
//            Path destinationFile = root.resolve(
//                    Paths.get(uploadedFileName))
//                    .normalize().toAbsolutePath();
//
//            try (InputStream inputStream = file.getInputStream()) {
//                Files.copy(inputStream, destinationFile,
//                        StandardCopyOption.REPLACE_EXISTING);
//
//                final String baseUrl =
//                        ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//
//                return baseUrl+"/fileUpload/files/"+uploadedFileName;
//                //return "bla";
//            }
//        }
//        catch (IOException e) {
//            throw new StorageException("Failed to store file.", e);
//        }
//    }


    @Override
    public void deleteFile(Long id) {
        Optional<NoviMethod1FileStoredOnDisk> stored = repository.findById(id);

        if (stored.isPresent()) {
            String filename = stored.get().getFileName();
            Path location = this.root.resolve(filename);
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
            Path path = this.root.resolve(filename);

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