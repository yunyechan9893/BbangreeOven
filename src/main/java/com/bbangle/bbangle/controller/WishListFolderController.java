package com.bbangle.bbangle.controller;

import java.util.List;
import com.bbangle.bbangle.dto.FolderRequestDto;
import com.bbangle.bbangle.dto.FolderResponseDto;
import com.bbangle.bbangle.dto.FolderUpdateDto;
import com.bbangle.bbangle.service.WishListFolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishLists")
public class WishListFolderController {

    private final WishListFolderService folderService;

    @PostMapping
    public ResponseEntity<Void> make(@RequestBody @Valid FolderRequestDto requestDto,
                                  Authentication authentication){
        Long memberId = 1L;
        folderService.create(memberId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<FolderResponseDto>> getList(Authentication authentication){
        Long memberId = 1L;
        return ResponseEntity.ok(folderService.getList(memberId));
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<FolderResponseDto> update(@PathVariable Long folderId,
                                                    @Valid FolderUpdateDto updateDto,
                                                    Authentication authentication){
        Long memberId = 1L;
        folderService.update(memberId, folderId, updateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> delete(@PathVariable Long folderId,
                                     Authentication authentication){
        Long memberId = 1L;
        folderService.delete(folderId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
