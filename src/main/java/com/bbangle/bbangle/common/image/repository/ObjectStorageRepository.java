package com.bbangle.bbangle.common.image.repository;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageRepository {

    void downloadFile();

    void selectFile();

    void deleteFile();

    void deleteFolder();

    Boolean createFile(String bucketName, String objectName, MultipartFile file);

    Boolean createFolder(String bucketName, String folderName);

}
