//modified from https://github.com/bezkoder/spring-boot-upload-multipart-files.git
//https://dev.to/nilmadhabmondal/let-s-develop-file-upload-service-from-scratch-using-java-and-spring-boot-3mf1

package com.novi.fassignment.services;

import com.novi.fassignment.exceptions.StorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

//    @Value("${app.upload.dir:${user.home}}")
//    public String uploadDir;
//    Path root = uploadDir;
//    Path path = dir.resolve("file");
//    Path root = Paths.get(uploadDir);

    //Path root = Paths.get(uploadDir + File.separator + "test_upload_services_version_bezkoder");
    Path root = Paths.get("C:\\Users", "Gebruiker\\Postman\\files");
    //Path root = Paths.get("D:\\Users\\Gebruiker");
    //private final Path root = Paths.get("test_upload_services_version_bezkoder");


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

    //This are alternative method to store (to compare with above method) and to retrieve files
    // https://dev.to/nilmadhabmondal/let-s-develop-file-upload-service-from-scratch-using-java-and-spring-boot-3mf1

    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String uploadedFileName = UUID.randomUUID().toString() + "." + extension;

            Path destinationFile = root.resolve(
                    Paths.get(uploadedFileName))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);

                final String baseUrl =
                        ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

                return baseUrl+"/fileUpload/files/"+uploadedFileName;
                //return "bla";
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
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

/*    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }*/
}
