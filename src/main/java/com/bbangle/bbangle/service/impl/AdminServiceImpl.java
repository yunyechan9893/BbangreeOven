package com.bbangle.bbangle.service.impl;

import com.bbangle.bbangle.dto.*;
import com.bbangle.bbangle.model.*;
import com.bbangle.bbangle.repository.*;
import com.bbangle.bbangle.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminStoreRepository adminStoreRepository;
    private final AdminBoardRepository adminBoardRepository;
    private final AdminBoardImgRepository adminBoardImgRepository;
    private final AdminProductRepository adminProductRepository;
    private final ObjectStorageRepository objectStorageRepository;


    String CDN_URL = "bbangree-oven.cdn.ntruss.com";
    String DEFAULT_PROFILE_FILE_NAME = "profile.jpg";
    String DEFAULT_SUBIMAGE_FOLDER_NAME = "subimage";
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    @Override
    @Transactional
    public Long uploadStore(AdminStoreRequestDto adminStoreRequestDto, MultipartFile profile) {

        Long storeId = adminStoreRepository.save(Store.builder()
                .identifier(adminStoreRequestDto.identifier())
                .name(adminStoreRequestDto.title())
                .introduce(adminStoreRequestDto.introduce())
                .build()).getId();

        String profileUrl = String.format("%s/%s", storeId, DEFAULT_PROFILE_FILE_NAME);
        objectStorageRepository.createFile(BUCKET_NAME, profileUrl, profile);
        String profileCdnUrl = String.format("%s/%s", CDN_URL, profileUrl);

        adminStoreRepository.save(Store.builder()
                .id(storeId)
                .identifier(adminStoreRequestDto.identifier())
                .name(adminStoreRequestDto.title())
                .introduce(adminStoreRequestDto.introduce())
                .profile(profileCdnUrl)
                .build());

        return storeId;
    }

    @Override
    public Long uploadBoard(MultipartFile profile, Long storeId, AdminBoardRequestDto adminBoardRequestDto) {

        Long boardId = adminBoardRepository.save(Board.builder()
                        .store(Store.builder().id(storeId).build())
                        .title(adminBoardRequestDto.title())
                        .price(adminBoardRequestDto.price())
                        .status(adminBoardRequestDto.status())
                        .purchaseUrl(adminBoardRequestDto.purchaseUrl())
                        .detail(adminBoardRequestDto.detailUrl())
                        .monday(adminBoardRequestDto.mon())
                        .tuesday(adminBoardRequestDto.tue())
                        .wednesday(adminBoardRequestDto.wed())
                        .thursday(adminBoardRequestDto.thr())
                        .friday(adminBoardRequestDto.fri())
                        .saturday(adminBoardRequestDto.sat())
                        .sunday(adminBoardRequestDto.sun())
                .build()).getId();

        String profileUrl = String.format("%s/%s/%s", storeId, boardId, DEFAULT_PROFILE_FILE_NAME);
        objectStorageRepository.createFile(BUCKET_NAME, profileUrl, profile);
        String profileCdnUrl = String.format("%s/%s", CDN_URL, profileUrl);

        adminBoardRepository.save(Board.builder()
                .id(boardId)
                .store(Store.builder().id(storeId).build())
                .title(adminBoardRequestDto.title())
                .price(adminBoardRequestDto.price())
                .profile(profileCdnUrl)
                .purchaseUrl(adminBoardRequestDto.purchaseUrl())
                .status(adminBoardRequestDto.status())
                .detail(adminBoardRequestDto.detailUrl())
                .monday(adminBoardRequestDto.mon())
                .tuesday(adminBoardRequestDto.tue())
                .wednesday(adminBoardRequestDto.wed())
                .thursday(adminBoardRequestDto.thr())
                .friday(adminBoardRequestDto.fri())
                .saturday(adminBoardRequestDto.sat())
                .sunday(adminBoardRequestDto.sun())
                .build());

        return boardId;
    }

    @Override
    public Boolean uploadBoardImage(Long storeId, Long boardId, MultipartFile profile) {
        int imgCount = adminBoardImgRepository.getBoardImageCount(boardId);
        String subimageName = String.format("%s.jpg",imgCount);
        String subimageUrl = String.format("%s/%s/%s/%s", storeId, boardId, DEFAULT_SUBIMAGE_FOLDER_NAME, subimageName);
        adminBoardImgRepository.save(
                ProductImg.builder()
                        .board(Board.builder().id(boardId).build())
                        .url(String.format("%s/%s/%s/%s/%s", CDN_URL, storeId, boardId, DEFAULT_SUBIMAGE_FOLDER_NAME, subimageName))
                        .build()
        );

        return objectStorageRepository.createFile(BUCKET_NAME, subimageUrl, profile);
    }

    @Override
    public void uploadProduct(Long storeId, Long boardId, AdminProductRequestDto adminProductRequestDto) {
        adminProductRepository.save(
                Product.builder()
                        .board(Board.builder().id(boardId).build())
                        .title(adminProductRequestDto.title())
                        .price(adminProductRequestDto.price())
                        .category(Category.valueOf(adminProductRequestDto.category()))
                        .glutenFreeTag(adminProductRequestDto.glutenFree())
                        .sugarFreeTag(adminProductRequestDto.sugarFree())
                        .highProteinTag(adminProductRequestDto.highProtein())
                        .veganTag(adminProductRequestDto.vegan())
                        .ketogenicTag(adminProductRequestDto.ketogenic())
                        .build()
        );
    }

}
