package com.bbangle.bbangle.wishList.service;

import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_MEMBER;

import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.wishList.domain.WishlistFolder;
import com.bbangle.bbangle.wishList.dto.FolderRequestDto;
import com.bbangle.bbangle.wishList.dto.FolderResponseDto;
import com.bbangle.bbangle.wishList.dto.FolderUpdateDto;
import com.bbangle.bbangle.wishList.repository.WishListFolderRepository;
import com.bbangle.bbangle.wishList.repository.impl.WishListFolderRepositoryImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListFolderService {

    private static final String OVER_MAX_FOLDER = "10개를 초과한 폴더를 생성하실 수 없습니다.";
    private final MemberRepository memberRepository;
    private final WishListFolderRepository wishListFolderRepository;
    private final WishListFolderRepositoryImpl wishListFolderRepositoryImpl;

    @Transactional
    public void create(Long memberId, FolderRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));
        int folderCount = wishListFolderRepository.getFolderCount(member);
        if (folderCount >= 10) {
            throw new BbangleException(OVER_MAX_FOLDER);
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
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));

        return wishListFolderRepository.findMemberFolderList(member);
    }

    @Transactional
    public void delete(Long folderId, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));

        WishlistFolder wishlistFolder = wishListFolderRepository.findByMemberAndId(member, folderId)
            .orElseThrow(() -> new BbangleException("존재하지 않는 폴더입니다."));

        wishlistFolder.delete();
    }

    @Transactional
    public void update(Long memberId, Long folderId, FolderUpdateDto updateDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));

        WishlistFolder wishlistFolder = wishListFolderRepository.findByMemberAndId(member, folderId)
            .orElseThrow(() -> new BbangleException("존재하지 않는 폴더입니다."));

        wishlistFolder.updateTitle(updateDto.title());
    }

    @Transactional
    public void deletedByDeletedMember(Long memberId) {
        List<WishlistFolder> wishListFolders = wishListFolderRepositoryImpl.findByMemberId(memberId);
        for (WishlistFolder wishListFolder : wishListFolders) {
            wishListFolder.delete();
        }
    }
}
