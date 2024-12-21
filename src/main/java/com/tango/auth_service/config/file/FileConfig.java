package com.tango.auth_service.config.file;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileConfig implements WebMvcConfigurer {
    private final Environment environment;

    public FileConfig(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        String os = System.getProperty("os.name").toLowerCase();
        String location = getUploadDirLocation(os);

        if (location == null) {
            throw new IllegalStateException("File upload directory is missing for OS: " + os);
        }

        File uploadDir = new File(location);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        String dirName = "uploads";
        String os = System.getProperty("os.name").toLowerCase();
        String location = getStorageMapping(os);

        if (location == null) {
            throw new IllegalStateException("File storage mapping is missing for OS: " + os);
        }

        Path uploadDir = Paths.get(location);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        if (dirName.startsWith("../")) {
            dirName = dirName.replace("../", "");
        }

        String resourceLocation = (os.contains("win"))
                ? "file:/" + uploadPath + "/"
                : "file://" + uploadPath + "/";

        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations(resourceLocation);
    }

    private String getUploadDirLocation(String os) {
        String location = null;
        if (os.contains("win")) {
            location = environment.getProperty("file.upload-dir-windows");
        } else {
            location = environment.getProperty("file.upload-dir-unix");
        }
        return location;
    }

    private String getStorageMapping(String os) {
        String location = null;
        if (os.contains("win")) {
            location = environment.getProperty("app.file.storage.mapping-windows");
        } else {
            location = environment.getProperty("app.file.storage.mapping-unix");
        }
        return location;
    }

//    final Environment environment;
//
//    public FileConfig(Environment environment) {
//        this.environment = environment;
//    }
//
//    @PostConstruct
//    public void init() {
//        String windowsLocation = environment.getProperty("file.upload-dir-windows");
//        String unixLocation = environment.getProperty("file.upload-dir-unix");
//
//        String os = System.getProperty("os.name").toLowerCase();
//        if (os.contains("win")) {
//            if (windowsLocation == null) {
//                throw new IllegalStateException("Property file.upload-dir-windows is missing in the configuration");
//            }
//            File uploadDir = new File(windowsLocation);
//            if (!uploadDir.exists()) {
//                uploadDir.mkdirs();
//            }
//        } else {
//            if (unixLocation == null) {
//                throw new IllegalStateException("Property file.upload-dir-windows is missing in the configuration");
//            }
//            File uploadDir = new File(unixLocation);
//            if (!uploadDir.exists()) {
//                uploadDir.mkdirs();
//            }
//        }
//    }
//
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        String dirName = "uploads";
////        String location = environment.getProperty("app.file.storage.mapping-windows");
//        String location;
//
//        String os = System.getProperty("os.name").toLowerCase();
//        if (os.contains("win")) {
//            location = environment.getProperty("app.file.storage.mapping-windows");
//        } else {
//            location = environment.getProperty("app.file.storage.mapping-unix");
//        }
//
//
//        assert location != null;
//        Path uploadDir = Paths.get(location);
//        String uploadPath = uploadDir.toFile().getAbsolutePath();
//
//        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
//
//        String resourceLocation;
//
//        if (System.getProperty("os.name").toLowerCase().contains("win")) {
//            resourceLocation = "file:/" + uploadPath + "/";
//        } else {
//            resourceLocation = "file://" + uploadPath + "/";
//        }
//
//        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations(resourceLocation);
//    }

}
