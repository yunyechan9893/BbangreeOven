package com.bbangle.bbangle.service.impl;

import java.util.List;
import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.exception.MemberNotFoundException;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.model.WishlistFolder;
import com.bbangle.bbangle.model.WishlistProduct;
import com.bbangle.bbangle.repository.BoardRepository;
import com.bbangle.bbangle.repository.MemberRepository;
import com.bbangle.bbangle.repository.WishListFolderRepository;
import com.bbangle.bbangle.repository.WishListProductRepository;
import com.bbangle.bbangle.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final WishListFolderRepository folderRepository;
    private final WishListProductRepository wishListProductRepository;

    @Override
    @Transactional(readOnly = true)
    public Slice<BoardResponseDto> getBoardList(String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                                Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                                String category, Integer minPrice, Integer maxPrice,
                                                Pageable pageable) {
        return boardRepository.getBoardResponseDto(
            sort,
            glutenFreeTag,
            highProteinTag,
            sugarFreeTag,
            veganTag,
            ketogenicTag,
            category,
            minPrice,
            maxPrice,
            pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoardDetailResponse(Long boardId) {
        return boardRepository.getBoardDetailResponseDto(boardId);
    }

    public Slice<BoardResponseDto> getPostInFolder(Long memberId, String sort, Long folderId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        WishlistFolder folder = folderRepository.findByMemberAndId(member, folderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 폴더입니다."));
        if(folder.isDeleted()){
            throw new IllegalArgumentException("존재하지 않는 폴더입니다.");
        }

        return boardRepository.getAllByFolder(sort, pageable, folderId, folder);
    }

}
