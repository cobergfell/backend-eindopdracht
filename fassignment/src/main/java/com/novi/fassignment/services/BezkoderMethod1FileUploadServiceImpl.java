package com.novi.fassignment.services;


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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;


import java.util.stream.Stream;
import org.springframework.util.FileSystemUtils;



@Service
public class BezkoderMethod1FileUploadServiceImpl implements BezkoderMethod1FileUploadService {


    @Value("${app.upload.dir:${user.home}}")
    private String uploadDirectory;  // relative to root
    //private final Path uploads = Paths.get("uploads");
    private final Path root = Paths.get("C:\\Users\\Gebruiker\\Test\\uploads");
    //private final Path uploads = Paths.get("D:\\Data\\NOVI\\eindopdracht\\fassignment-frontend\\src\\assets");

    @Autowired
    private NoviMethod1FileUploadRepository repository;


   //private final Path root = Paths.get("uploads");
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

/*    @Override
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
    }*/

    @Override
    public Resource load(long id) {
        Optional<NoviMethod1FileStoredOnDisk> stored = repository.findById(id);

        try {
            String filename=stored.get().getFileName();
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
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
/*    @Value("${app.upload.dir:${user.home}}")
    private String uploadDirectory;  // relative to root
    //private final Path uploads = Paths.get("uploads");
    private final Path uploads = Paths.get("C:\\Users\\Gebruiker\\Test\\uploads");
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

    public long uploadFile(NoviMethod1FileUploadRequestDto method1Dto) {

        MultipartFile file = method1Dto.getFile();

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        Path copyLocation = this.uploads.resolve(file.getOriginalFilename());

        try {
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + originalFilename + ". Please try again!");
        }

        NoviMethod1FileStoredOnDisk newFileToStore = new NoviMethod1FileStoredOnDisk();
        newFileToStore.setFileName(originalFilename);
        newFileToStore.setLocation(copyLocation.toString());
        newFileToStore.setTitle(method1Dto.getTitle());
        newFileToStore.setDescription(method1Dto.getDescription());

        NoviMethod1FileStoredOnDisk saved = repository.save(newFileToStore);

        return saved.getId();
    }

    @Override
    public void deleteFile(long id) {
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
    public NoviMethod1FileUploadResponseDto getFileById(long id) {
        Optional<NoviMethod1FileStoredOnDisk> stored = repository.findById(id);

        if (stored.isPresent()) {

            String location=stored.get().getLocation();
            //Path path = Paths.get(location);
            URI uri = ServletUriComponentsBuilder.fromPath(location).build().toUri();
            //URI uri = ServletUriComponentsBuilder.fromPath(location).build().encode().toUri();

            //URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            //        .buildAndExpand("download").toUri();




            NoviMethod1FileUploadResponseDto responseDto = new NoviMethod1FileUploadResponseDto();
            responseDto.setFileName(stored.get().getFileName());
            responseDto.setTitle(stored.get().getTitle());
            responseDto.setDescription(stored.get().getDescription());
            responseDto.setMediaType(stored.get().getMediaType());
            //responseDto.setDownloadUri(uri.toString());
            responseDto.setDownloadUri(location);
            return responseDto;
        }
        else {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public boolean fileExistsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public Resource downloadFile(long id) {
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
    }*/

}