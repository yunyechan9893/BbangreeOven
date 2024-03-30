package com.bbangle.bbangle.store.repository;

import com.bbangle.bbangle.board.dto.StoreAllBoardDto;
import com.bbangle.bbangle.store.dto.StoreDetailResponseDto;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;


public interface StoreQueryDSLRepository {

    StoreDetailResponseDto getStoreDetailResponseDtoWithLike(Long memberId, Long storeId);

    StoreDetailResponseDto getStoreDetailResponseDto(Long storeId);

    SliceImpl<StoreAllBoardDto> getAllBoardWithLike(Pageable pageable, Long memberId, Long storeId);

    SliceImpl getAllBoard(Pageable pageable, Long storeId);


    HashMap<Long, String> getAllStoreTitle();

}
