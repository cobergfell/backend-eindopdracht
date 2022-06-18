package com.novi.fassignment.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.controllers.dto.FileStoredInDataBaseDto;
import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.PaintingRepository;
import com.novi.fassignment.services.FileStorageInDataBaseServiceImpl;
import com.novi.fassignment.services.PaintingServiceImpl;
import com.novi.fassignment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.novi.fassignment.utils.Parsers.myLocalDateTimeParserTypeYearMonthDayHourMinSec;

@RestController
//@CrossOrigin("http://localhost:8080")
@CrossOrigin("*")
public class PaintingController {

    @Autowired
    private FileStorageInDataBaseServiceImpl storageService;

    @Autowired
    private PaintingServiceImpl paintingService;

    @Autowired
    private PaintingRepository paintingRepository;

    @Autowired
    UserService userService;
/*
    @Autowired
    private PaintingGetDto paintingGetDto;//Dto are usually static methods but in this case it had to be instantiated to instantiate services
*/
public Long paintingId;//paintingId will only be specified when updating


    @PostMapping("api/user/paintings-upload")
    public ResponseEntity<Object> sendPainting(
            @RequestParam(value="username",required=false) String username,
            @RequestParam(value="title",required=false)  String title,
            @RequestParam(value="artist",required=false) String artist,
            @RequestParam(value="description",required=false)  String description,
            @RequestParam(value="image",required=false)  MultipartFile image,
            @RequestParam(value="files",required=false) MultipartFile[] files,
            @RequestParam(value="audioFiles",required=false) MultipartFile[] audioFiles) {


        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
//            ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:01"));
//            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
//            formatter.format(zonedDateTimePosted);


            PaintingInputDto inputDto= new PaintingInputDto();
            inputDto.username=username;
            inputDto.title=title;
            inputDto.artist=artist;
            inputDto.description=description;
            inputDto.dateTimePosted=dateTimePosted;
            inputDto.lastUpdate=dateTimePosted;
            inputDto.image=image.getBytes();
            inputDto.files=files;
            inputDto.audioFiles=audioFiles;

            paintingService.createPainting(inputDto);
            message = "Painting submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Painting could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }


    @GetMapping("api/user/paintings")
    public List<PaintingDto> getPaintings() {
        var dtos = new ArrayList<PaintingDto>();
        //var paintings = paintingService.getAllPaintings();
        var paintings = paintingService.getAllPaintingsByAscId();
        List<FileStoredInDataBase> filesStoredInDataBase = storageService.getAllFilesAsList();

        for (Painting painting : paintings) {
            //paintingService.getFiles(painting);
            dtos.add(PaintingDto.fromPaintingToDto(painting));
        }
        return dtos;
    }

    @GetMapping("api/user/paintings/{paintingId}")
    public PaintingDto getPainting(@PathVariable("paintingId") Long paintingId) {
        var dto = new PaintingDto();
        var painting = paintingService.getPaintingById(paintingId);
        dto=PaintingDto.fromPaintingToDto(painting);
        return dto;
    }


    @RequestMapping(value = "api/user/paintings/image/{paintingId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@PathVariable("paintingId") Long paintingId) {
        var painting = paintingService.getPaintingById(paintingId);
        var dto = new PaintingDto();
        dto=PaintingDto.fromPaintingToDto(painting);
        byte[] image = dto.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }


    @RequestMapping(value = "api/user/paintings/imageB64/{paintingId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageB64(@PathVariable("paintingId") Long paintingId) {
        var painting = paintingService.getPaintingById(paintingId);
        var dto = new PaintingDto();
        dto=PaintingDto.fromPaintingToDto(painting);
        byte[] image = dto.getImage();
        String s = new String(image);
        byte[] encoded = Base64.getEncoder().encode(s.getBytes());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(encoded);
    }


    @DeleteMapping("api/user/paintings/{paintingId}")
    public ResponseEntity<HttpStatus> deletePainting(@PathVariable("paintingId") long paintingId) {
        try {
            paintingService.deletePaintingById(paintingId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("api/user/paintings")
    public ResponseEntity<HttpStatus> deleteAllPaintings() {
        try {
            paintingService.deleteAllPaintings();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("api/user/paintings-update/{paintingId}")
    public ResponseEntity<Object> updatePaintingWithFiles(@PathVariable("paintingId") Long paintingId,
                                                          @RequestParam(value="username",required=false) String username,
                                                          @RequestParam(value="dateTimePosted",required=false) String dateTimePosted,
                                                          @RequestParam(value="title",required=false)  String title,
                                                          @RequestParam(value="artist",required=false) String artist,
                                                          @RequestParam(value="description",required=false)  String description,
                                                          @RequestParam(value="image",required=false)  MultipartFile image,
                                                          @RequestParam(value="files",required=false) MultipartFile[] multipartFiles,
                                                          @RequestParam(value="audioFiles",required=false) MultipartFile[] audioFiles) {
        String message_painting = "";
        Optional<Painting> currentPainting = paintingRepository.findById(paintingId);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss Z");
        //DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        //DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


//        OffsetDateTime date = OffsetDateTime.now();
//        System.out.println(date);
//
//        ZonedDateTime zdt = ZonedDateTime.from(date);
//        System.out.println(zdt);
//        System.out.println( formatter.format(zdt) );

        if (currentPainting.isPresent()) {
            //paintingService.deletePaintingById(id);
            Painting paintingToUpdate = currentPainting.get();
            ///paintingService.createPainting(updatedPainting);
            try {
                //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
                String message = "";
                try {
                    //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));

//                    var dto = new PaintingDto();
//                    var painting = paintingService.getPaintingById(paintingId);
//                    dto=PaintingDto.fromPaintingToDto(painting);

//                    ZonedDateTime dateTimePostedWithZoneOffset = ZonedDateTime
//                            .parse("2011-12-03T10:15:30+01:00", formatter);
                    //ZonedDateTime dateTimePostedWithZoneOffset = ZonedDateTime
                    //        .parse(dateTimePosted, formatter);

                    LocalDateTime localDateTimePosted =myLocalDateTimeParserTypeYearMonthDayHourMinSec(dateTimePosted);
                    LocalDateTime lastUpdate = LocalDateTime.now(ZoneId.of("GMT+00:01"));
                    ZonedDateTime zonedLastUpdate = lastUpdate.atZone(ZoneId.of("GMT+00:01"));
                    //formatter.format(zonedLastUpdate);


                    if (username != null){
                        Optional<User> user = userService.getUser(username);
                        String password = user.get().getPassword();
                        String email = user.get().getEmail();
                        User userFromCustomUser = new User();
                        userFromCustomUser.setUsername(username);
                        userFromCustomUser.setPassword(password);
                        userFromCustomUser.setEmail(email);
                        paintingToUpdate.setUser(userFromCustomUser);
                    }

                    PaintingInputDto inputDto= new PaintingInputDto();
                    inputDto.paintingId=paintingId;
                    inputDto.username=username;
                    inputDto.dateTimePosted=localDateTimePosted;
                    inputDto.lastUpdate=lastUpdate;
                    if (title != null){inputDto.title=title;}
                    else{inputDto.title=paintingToUpdate.getTitle();}
                    if (artist != null){inputDto.artist=artist;}
                    else{inputDto.artist=paintingToUpdate.getArtist();}
                    if (description != null){inputDto.description=description;}
                    else{inputDto.description=paintingToUpdate.getDescription();}
                    if (image != null){inputDto.image=image.getBytes();}
                    else{inputDto.image=paintingToUpdate.getImage();}
                    if (multipartFiles != null){inputDto.files=multipartFiles;}
                    else{inputDto.files=null;}
                    if (audioFiles != null){inputDto.audioFiles=audioFiles;}
                    else{inputDto.audioFiles=null;}


/*                    if (multipartFiles != null){
                        List<String> fileNames = new ArrayList<>();
                        //Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
                        List<MultipartFile> multipartFilesList = new ArrayList<MultipartFile>();
                        Arrays.asList(multipartFiles).stream().forEach(theFile -> multipartFilesList.add(theFile));
                    }
                    if (musicFiles != null){
                        List<String> fileNames = new ArrayList<>();
                        //Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
                        List<MultipartFile> multipartFilesList = new ArrayList<MultipartFile>();
                        Arrays.asList(musicFiles).stream().forEach(theFile -> multipartFilesList.add(theFile));
                    }*/
                    //paintingRepository.save(paintingToUpdate);

                    paintingService.updatePainting(inputDto, paintingToUpdate);

                    message = "Painting submitted!";
                    return ResponseEntity.noContent().build();

                } catch (Exception exception) {
                    message = "Painting could not be submitted/uploaded!";
                    return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
                }


            } catch (Exception exception) {
                message_painting = "Painting could not be submitted/uploaded!";
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
            }


        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @GetMapping("api/user/paintings/sortedpaintings")
    public ResponseEntity<List<Painting>> getAllTutorials(@RequestParam(defaultValue = "paintingId,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Painting> paintings = paintingRepository.findAll(Sort.by(orders));

            if (paintings.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(paintings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("api/user/paintingsPage")
    public ResponseEntity<Map<String, Object>> getAllPaintingsPage(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "paintingId,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Painting> paintings = new ArrayList<Painting>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Painting> pagePainting;
            if (title == null)
                pagePainting = paintingRepository.findAll(pagingSort);
            else
                pagePainting = paintingRepository.findByTitleContaining(title, pagingSort);

            paintings = pagePainting.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("paintings", paintings);
            response.put("currentPage", pagePainting.getNumber());
            response.put("totalItems", pagePainting.getTotalElements());
            response.put("totalPages", pagePainting.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}








/*    @PostMapping("/paintings-upload-with-files-in-database")
    public ResponseEntity<Object> sendPainting(
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("body") String body,
            @RequestParam("tags")  String tags,
            //@RequestParam("video") MultipartFile video,
            @RequestParam("files") MultipartFile[] files) {
        String message = "";
        try {
            //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
            Painting painting = new Painting();
            Optional<User> user=userService.getUser(username);
            String password = user.get().getPassword();
            String email = user.get().getEmail();
            User userFromCustomUser = new User();
            userFromCustomUser.setUsername(username);
            userFromCustomUser.setPassword(password);
            userFromCustomUser.setEmail(email);
            painting.setUser(userFromCustomUser);
            painting.setTitle(title);
            // painting.setDatePosted(datePosted.toString());
            painting.setBody(body);
            painting.setTags(tags);

            paintingService.createPainting(painting);

            List<Painting> sortedPaintings=paintingService.getAllPaintingsByDescId();
            //Painting[] sortedPaintingsArray= (Painting[]) sortedPaintings.toArray();//cast array of objects into array of paintings
            Painting mostRecentPainting= sortedPaintings.get(0);
            //Painting mostRecentPainting=sortedPaintingsArray[0];
            Long mostRecentPaintingId=mostRecentPainting.getPaintingId();

            //storageService.store(file);

            List<String> fileNames = new ArrayList<>();

            Arrays.asList(files).stream().forEach(file -> {String message2 = "";
                try {
                    //storageService.store(file);
                    storageService.storePaintingFile(file,painting);

                    fileNames.add(file.getOriginalFilename());
                    message2 = "Uploaded the files successfully: " + fileNames;
                } catch (Exception e) {
                    message2 = "Fail to upload files!";
                }
            });

            Set<FileStoredInDataBase> attachedFiles= new HashSet<>();
            List<FileStoredInDataBase> sortedFiles=storageService.getAllFilesByDescId();
            //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedPaintings.toArray();//cast array of objects into array of paintings
            for (int i=0; i<fileNames.size(); i++) {
                FileStoredInDataBase mostRecentFile=sortedFiles.get(i);
                attachedFiles.add(mostRecentFile);
            };

            mostRecentPainting.setFiles(attachedFiles);

            message = "Painting submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Painting could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

    }*/




