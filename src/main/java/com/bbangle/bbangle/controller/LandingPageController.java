package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.dto.RequestEmailDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class LandingPageController {

    @PostMapping("/api/v1/landingPage")
    public ResponseEntity<Map<String, String>> getUserEmail(@Valid @RequestBody RequestEmailDto requestEmailDto){
        String email = requestEmailDto.getEmail()+",";

//        for local
//        String directoryPath = "C:\\Users\\Dongseok\\Desktop";
//        for server
        String directoryPath = "/etc/bbangle";
        String fileName = "landingPageUserEmail.txt";

        // 파일 오브젝트 생성하기
        Path filePath = Paths.get(directoryPath, fileName);
        Map<String, String> messageMap = new HashMap<>();
        try {
            //만약 경로가 없으면 생성하기
            Files.createDirectories(filePath.getParent());

            // 파일이 있는지 확인하고 없으면 생성
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                log.debug("File created: " + filePath.toString());
            }

            // 파일에 이메일 추가하기
            Files.write(filePath, email.getBytes(), StandardOpenOption.APPEND);

            messageMap.put("message", "success");
            return ResponseEntity.ok(messageMap);
        } catch (IOException e) {
            log.error("getUserEmail() >>>> file io {}", e.getMessage());
            messageMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageMap);
        }
    }
}
