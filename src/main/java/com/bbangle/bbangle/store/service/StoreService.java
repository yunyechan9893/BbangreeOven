package com.bbangle.bbangle.store.service;

import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;

import com.bbangle.bbangle.page.StoreDetailCustomPage;
import com.bbangle.bbangle.store.dto.PopularBoardResponse;
import com.bbangle.bbangle.store.dto.StoreBoardsResponse;

import com.bbangle.bbangle.page.StoreCustomPage;
import com.bbangle.bbangle.store.dto.StoreResponse;
import com.bbangle.bbangle.store.dto.StoreResponseDto;
import com.bbangle.bbangle.store.repository.StoreRepository;
import com.bbangle.bbangle.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public StoreResponse getStoreResponse(Long memberId, Long storeId){
        return storeRepository.getStoreResponse(memberId, storeId);
    }

    public List<PopularBoardResponse> getPopularBoardResponse(Long memberId, Long storeId) {
        return storeRepository.getPopularBoardResponses(memberId, storeId);
    }

    public StoreDetailCustomPage<List<StoreBoardsResponse>> getStoreAllBoard(Long memberId, Long storeId,
        Long boardIdAsCursorId) {
        return storeRepository.getStoreBoardList(memberId, storeId, boardIdAsCursorId);
    }

    public StoreCustomPage<List<StoreResponseDto>> getList(Long cursorId) {
        if (!SecurityUtils.isLogin()) {
            return storeRepository.findNextCursorPageWithoutLogin(cursorId);
        }

        Long memberId = SecurityUtils.getMemberId();
        Member member = memberRepository.findById(memberId)
            .orElseThrow(BbangleException::new);
        return storeRepository.findNextCursorPageWithLogin(cursorId, member);
    }

}
