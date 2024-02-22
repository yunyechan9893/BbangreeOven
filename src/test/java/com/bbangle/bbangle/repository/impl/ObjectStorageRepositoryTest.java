package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.repository.ObjectStorageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ObjectStorageRepositoryTest {

    private String BUCKET_NAME = "bbangle-bucket";
    private String STORE_ID = "1";
    private String BOARD_ID = "2";
//    private String IMAGE = "img";

    private String FOLDER_PATH = String.format("%s/%s/", STORE_ID, BOARD_ID);
    private String HTML_FILE_NAME = "detail.html";
    private String OBJECT_NAME = FOLDER_PATH + HTML_FILE_NAME;
    private final ObjectStorageRepository objectStorageRepository;

    public ObjectStorageRepositoryTest(@Autowired ObjectStorageRepository objectStorageRepository) {
        this.objectStorageRepository = objectStorageRepository;
    }

    @Test
    @DisplayName("ObjectStorage에 정상적으로 폴더를 만들 수 있다")
    public void createStorageFolder(){
        objectStorageRepository.createFolder(BUCKET_NAME, FOLDER_PATH);
    }

    @Test
    @DisplayName("ObjectStorage에 정상적으로 파일을 저장할 수 있다")
    public void createStorageFile(){
        // https://green-bin.tistory.com/112 S3 Mock 테스트 미래에 참조
        byte[] content;
        try {
            content = Files.readAllBytes(Paths.get("src/test/resources/html/detail.html"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // MockMultipartFile 인스턴스 생성
        MockMultipartFile HTML_FILE = new MockMultipartFile(
                "htmlFile", // form의 input field 이름
                "detail.html", // 업로드될 파일명
                "text/html", // 파일 타입
                content // 파일 내용
        );

        var result = objectStorageRepository.createFile(BUCKET_NAME, OBJECT_NAME, HTML_FILE);
        assertThat(result).isEqualTo(true);
    }
}
