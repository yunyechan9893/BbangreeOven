package com.bbangle.bbangle.store.service;

import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;

import com.bbangle.bbangle.board.dto.StoreAllBoardDto;

import com.bbangle.bbangle.page.StoreCustomPage;
import com.bbangle.bbangle.store.dto.StoreDetailResponseDto;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import com.bbangle.bbangle.store.repository.StoreRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreDetailResponseDto getStoreDetailResponse(Long memberId, Long storeId) {
        return memberId > 1L ?
            storeRepository.getStoreDetailResponseDtoWithLike(memberId, storeId) :
            storeRepository.getStoreDetailResponseDto(storeId);

    }

    public SliceImpl<StoreAllBoardDto> getAllBoard(int page, Long memberId, Long storeId) {
        int PAGE_SIZE = 10;

        return memberId > 1L ?
            storeRepository.getAllBoardWithLike(PageRequest.of(page, PAGE_SIZE), memberId, storeId) :
                storeRepository.getAllBoard(PageRequest.of(page, PAGE_SIZE), storeId);
    }

    public StoreCustomPage<List<StoreResponseDto>> getList(Long cursorId, Long memberId) {
        return storeRepository.getStoreList(cursorId, memberId);
    }

}
