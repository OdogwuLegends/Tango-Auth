package com.tango.auth_service.service.interfaces;


import com.tango.auth_service.service.implementation.FileImplementation;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileImplementation.FileResponse upload(MultipartFile file);
}
