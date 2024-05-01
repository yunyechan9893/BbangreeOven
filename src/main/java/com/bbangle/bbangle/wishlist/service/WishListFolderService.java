package com.bbangle.bbangle.wishlist.service;

import static com.bbangle.bbangle.exception.BbangleErrorCode.NOTFOUND_MEMBER;

import com.bbangle.bbangle.exception.BbangleErrorCode;
import com.bbangle.bbangle.exception.BbangleException;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.wishlist.domain.WishlistFolder;
import com.bbangle.bbangle.wishlist.dto.FolderRequestDto;
import com.bbangle.bbangle.wishlist.dto.FolderResponseDto;
import com.bbangle.bbangle.wishlist.dto.FolderUpdateDto;
import com.bbangle.bbangle.wishlist.repository.WishListFolderRepository;
import com.bbangle.bbangle.wishlist.repository.impl.WishListFolderRepositoryImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListFolderService {

    private final MemberRepository memberRepository;
    private final WishListFolderRepository wishListFolderRepository;
    private final WishListFolderRepositoryImpl wishListFolderRepositoryImpl;

    @Transactional
    public void create(Long memberId, FolderRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BbangleException(NOTFOUND_MEMBER));
        validateMakingFolder(requestDto, member);

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

    private void validateMakingFolder(FolderRequestDto requestDto, Member member) {
        int folderCount = wishListFolderRepository.getFolderCount(member);
        if(wishListFolderRepository.existsByFolderNameAndMember(requestDto.title(), member)){
            throw new BbangleException(BbangleErrorCode.FOLDER_NAME_ALREADY_EXIST);
        }
        if (folderCount >= 10) {
            throw new BbangleException(BbangleErrorCode.OVER_MAX_FOLDER);
        }
    }
}
