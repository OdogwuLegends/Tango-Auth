package com.tango.auth_service.service.implementation;

import com.tango.auth_service.entities.File;
import com.tango.auth_service.repositories.FileRepository;
import com.tango.auth_service.service.interfaces.FileService;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileImplementation implements FileService {
    private final FileRepository fileRepository;
    private final Path fileStorageLocation;
    private static final Logger logger = LoggerFactory.getLogger(FileImplementation.class);

    private final Environment environment;

//    @Value("${file.upload-dir-windows}")
//    private String fileUploadDirWindows;

//    @Value("${file.upload-dir-unix}")
//    private String fileUploadDirUnix;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Autowired
    public FileImplementation(FileRepository fileRepository, Environment environment) {
        this.fileRepository = fileRepository;
        this.environment = environment;

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String fileUploadDirWindows = "C:/var/www/html/java/tango/uploads";
            this.fileStorageLocation = Paths.get(fileUploadDirWindows);
        } else {
            String fileUploadDirUnix = "C:/var/www/html/java/tango/uploads";
            this.fileStorageLocation = Paths.get(fileUploadDirUnix);
        }

        try {
            Files.createDirectories(this.fileStorageLocation);
            logger.info("File storage directory created at: {}", this.fileStorageLocation);
        } catch (Exception ex) {
            logger.error("Error creating file storage directory", ex);
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public FileResponse upload(MultipartFile file){
        File fileData = null;
        try {
            fileData = File.builder()
                    .name(storeFile(file))
                    .originalName(file.getOriginalFilename())
                    .extension(file.getContentType())
                    .size(String.valueOf(file.getSize()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File savedFile = fileRepository.save(fileData);
        return fileResponse(savedFile);
    }


    private String storeFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + new Date().getTime() + "-file." + getFileExtension(file.getOriginalFilename());

        if (fileName.contains("..")) {
            throw new RuntimeException(
                    "Sorry! Filename contains invalid path sequence " + fileName);
        }

        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");

        return fileNameParts[fileNameParts.length - 1];
    }

    private String getUrl(){
        return appBaseUrl + "/api/file/";
    }


    private FileResponse fileResponse(File file) {
        return new FileResponse(file,getUrl());
    }



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FileResponse {
        private String id;
        private String name;
        private String extension;
        private String originalName;
        private String size;

        public FileResponse(File file, String url) {
            this.id = file.getId();
            this.name = url + file.getName();
            this.extension = file.getExtension();
            this.originalName = file.getOriginalName();
            this.size = file.getSize();
        }
    }
}
