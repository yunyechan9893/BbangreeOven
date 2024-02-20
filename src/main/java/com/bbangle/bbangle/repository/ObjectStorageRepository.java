package com.bbangle.bbangle.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ObjectStorageRepository {
    void downloadFile();
    void selectFile();
    void deleteFile();
    void deleteFolder();
    Boolean createFile(String bucketName, String objectName, MultipartFile file);
    Boolean createFolder(String bucketName, String folderName);
}
