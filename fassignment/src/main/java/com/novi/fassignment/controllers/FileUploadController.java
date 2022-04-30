//modified from
//https://dev.to/nilmadhabmondal/let-s-develop-file-upload-service-from-scratch-using-java-and-spring-boot-3mf1


package com.novi.fassignment.controllers;

import com.novi.fassignment.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

/*    @CrossOrigin
    @PostMapping("/file-upload")
    public String uploadFile(@RequestBody MultipartFile file) {
        fileUploadService.uploadFile(file);
        return "redirect:/";
    }*/


    //upload a file
    //@CrossOrigin(origins = {"${settings.cors_origin}"})
    @CrossOrigin
    @PostMapping("api/user/file-upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        fileUploadService.uploadFile(file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        //return "redirect:/";
        return ResponseEntity.ok("redirect:/");
    }

/*    // get all the files
    @GetMapping("/")
    public ResponseEntity<List<FileInfo>> getListFiles() {

        // first get a stream of all file path present in root file directory
        Stream<Path> pathStream =  fileUploadService.loadAll();

        List<FileInfo> fileInfos = pathStream.map(path -> {
            // get file name
            String filename = path.getFileName().toString();

            // use function to get one file to build the URL
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "getFile", path.getFileName().toString()).build().toString();
            // make a fileinfo object  from filename and url
            return new FileInfo(filename, url);

        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    // get file by filename
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileUploadService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }*/



}
