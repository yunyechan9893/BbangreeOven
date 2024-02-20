package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.repository.ObjectStorageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ObjectStorageRepositoryTest {

    private String BUCKET_NAME = "pzza-bucket";
    private String FOLDER_NAME = "test";
    private final ObjectStorageRepository objectStorageRepository;

    public ObjectStorageRepositoryTest(@Autowired ObjectStorageRepository objectStorageRepository) {
        this.objectStorageRepository = objectStorageRepository;
    }

    @Test
    public void createStorageFolder(){
        objectStorageRepository.createFolder(BUCKET_NAME, FOLDER_NAME);
    }

}
