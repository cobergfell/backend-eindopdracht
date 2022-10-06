package com.novi.fassignment.utils;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Checks {


    public boolean checkIfAudio(MultipartFile multipartFile) {
        String contentTypeOfGivenFile = multipartFile.getContentType();
        boolean isAudio=false;
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



}
