package com.bbangle.bbangle.service;

import java.util.List;
import com.bbangle.bbangle.dto.FolderRequestDto;
import com.bbangle.bbangle.dto.FolderResponseDto;
import com.bbangle.bbangle.dto.FolderUpdateDto;
import com.bbangle.bbangle.exception.MemberNotFoundException;
import com.bbangle.bbangle.model.Member;
import com.bbangle.bbangle.model.WishlistFolder;
import com.bbangle.bbangle.repository.MemberRepository;
import com.bbangle.bbangle.repository.WishListFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListFolderService {

    private static final String OVER_MAX_FOLDER = "10개를 초과한 폴더를 생성하실 수 없습니다.";
    private final MemberRepository memberRepository;
    private final WishListFolderRepository wishListFolderRepository;

    @Transactional
    public void create(Long memberId, FolderRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        int folderCount = wishListFolderRepository.getFolderCount(member);
        if (folderCount >= 10) {
            throw new IllegalArgumentException(OVER_MAX_FOLDER);
        }

        WishlistFolder folder = WishlistFolder.builder()
            .member(member)
            .folderName(requestDto.title())
            .isDeleted(false)
            .build();

        wishListFolderRepository.save(folder);
    }

    @Transactional(readOnly = true)
    public List<FolderResponseDto> getList(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        return wishListFolderRepository.findMemberFolderList(member);
    }

    @Transactional
    public void delete(Long folderId, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        WishlistFolder wishlistFolder = wishListFolderRepository.findByMemberAndId(member, folderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 폴더입니다."));

        wishlistFolder.delete();
    }

    @Transactional
    public void update(Long memberId, Long folderId, FolderUpdateDto updateDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        WishlistFolder wishlistFolder = wishListFolderRepository.findByMemberAndId(member, folderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 폴더입니다."));

        wishlistFolder.updateTitle(updateDto.title());
    }

}
