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

    @CrossOrigin
    @PostMapping("api/user/file-upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        fileUploadService.uploadFile(file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        //return "redirect:/";
        return ResponseEntity.ok("redirect:/");
    }

}
