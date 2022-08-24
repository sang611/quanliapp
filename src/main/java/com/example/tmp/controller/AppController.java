package com.example.tmp.controller;


import com.example.tmp.dto.ApplicationDTO;
import com.example.tmp.entity.Application;
import com.example.tmp.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/application")
public class AppController {
    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/findAllApplication")
    public ResponseEntity<List<Application>> getAllApplication() {
        List<Application> applications = applicationService.findAllApplication();
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @GetMapping("/findApplicationById/{id}")
    public Optional<Application> getApplication(@PathVariable("id") Integer id) {
        Optional<Application> application = applicationService.findApplicationById(id);
        return application;
    }

    @PostMapping("/add")
    public ResponseEntity<Application> addEmployee(@RequestBody Application application) {
        Application newApplication = applicationService.addApplication(application);
        return new ResponseEntity<>(newApplication, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Application> updateEmployee(@PathVariable("id") Integer id, @RequestBody Application application) {
        Optional<Application> applicationEdit = applicationService.findApplicationById(id);
        if (applicationEdit.isPresent()) {
            applicationEdit.get().setImage(application.getImage());
            applicationEdit.get().setName(application.getName());
            applicationEdit.get().setImage(application.getImage());
            applicationEdit.get().setOs_type(application.getOs_type());
            applicationEdit.get().setFile_path(application.getFile_path());
            applicationEdit.get().setVersion(application.getVersion());
            applicationEdit.get().setBuild_number(application.getBuild_number());
            applicationEdit.get().setShortdescripti(application.getShortdescripti());
            applicationEdit.get().setDescripti(application.getDescripti());
            Application updateApplication = applicationService.updateApplication(applicationEdit.get());
            return new ResponseEntity<>(updateApplication, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable("id") Integer id) {
        applicationService.deleteApplication(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //sangvn1 up file
    @Value("${app.file.upload.path}")
    String uploadPath;

    @PostMapping(value = "/add-with-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Application> addAppWithFile(@ModelAttribute ApplicationDTO application) {
        Application newApp = new Application();
        newApp.setName(application.getName());
        newApp.setImage(application.getImage());
        newApp.setOs_type(application.getOs_type());
        newApp.setVersion(application.getVersion());
        newApp.setBuild_number(application.getBuild_number());
        newApp.setShortdescripti(application.getDescripti());
        newApp.setDescripti(application.getDescripti());

        MultipartFile file = application.getDocument();


        try {
            //Lưu file vào đường dẫn uploadPath cấu hình trong file application.properties
            Files.copy(file.getInputStream(), Paths.get(uploadPath).resolve(Objects.requireNonNull(file.getOriginalFilename())));

            //Lưu file path vào db, path = đường dẫn cấu hình + tên file
            newApp.setFile_path(uploadPath + file.getOriginalFilename());
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

        Application newApplication = applicationService.addApplication(newApp);

        return new ResponseEntity<>(newApplication, HttpStatus.CREATED);

    }

    //sangvn1 down file
    @GetMapping("/files/download/{appId}")
    public ResponseEntity<FileSystemResource> download(@PathVariable Integer appId) {
        Optional<Application> application = applicationService.findApplicationById(appId);
        FileSystemResource resource = new FileSystemResource(application.get().getFile_path());

        MediaType mediaType = MediaTypeFactory
                .getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        ContentDisposition disposition = ContentDisposition
                .inline()
                .filename(Objects.requireNonNull(resource.getFilename()))
                .build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<FileSystemResource>(resource, headers, HttpStatus.OK);
    }


    /*@GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Integer id) {
        Optional<Application> application = applicationService.findApplicationById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + application.get().getFile_path() + "\"")
                .body(optionalFileDB.get().getData());
    }*/
}
