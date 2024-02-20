package com.bbangle.bbangle.repository.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bbangle.bbangle.repository.ObjectStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class ObjectStorageRepositoryImpl implements ObjectStorageRepository {

    private final AmazonS3 s3;

    @Override
    public void downloadFile() {

    }

    @Override
    public void selectFile() {

    }

    @Override
    public void deleteFile() {

    }

    @Override
    public void deleteFolder() {

    }

    @Override
    public Boolean createFile(String bucketName, String objectName, MultipartFile file) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();

            // 콘텐츠 타입 설정
            objectMetadata.setContentType(file.getContentType());

            // 파일 크기 설정
            objectMetadata.setContentLength(file.getSize());

            s3.putObject(bucketName, objectName, file.getInputStream(), objectMetadata);
            return true;
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            return false;
        } catch(SdkClientException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean createFolder(String bucketName, String folderName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName,
                new ByteArrayInputStream(new byte[0]), objectMetadata);

        try {
            s3.putObject(putObjectRequest);
            return true;
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            return false;
        } catch (SdkClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}
