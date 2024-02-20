package com.bbangle.bbangle.repository;

import com.amazonaws.services.s3.model.PutObjectRequest;

public interface ObjectStorageRepository {
    void downloadFile();
    void selectFile();
    void deleteFile();
    void deleteFolder();
    void createFile(PutObjectRequest putObjectRequest);
    Boolean createFolder(String bucketName, String folderName);
}
