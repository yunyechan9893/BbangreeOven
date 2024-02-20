package com.bbangle.bbangle.repository.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bbangle.bbangle.repository.ObjectStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
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
    public void createFile(PutObjectRequest putObjectRequest) {
        try {
            if (s3 != null) {
                s3.putObject(putObjectRequest);
            }
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
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
            System.out.format("Folder %s has been created.\n", folderName);
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
