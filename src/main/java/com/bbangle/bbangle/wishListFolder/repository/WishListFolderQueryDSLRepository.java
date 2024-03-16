package com.bbangle.bbangle.wishListFolder.repository;

import com.bbangle.bbangle.wishListFolder.dto.FolderResponseDto;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.wishListFolder.domain.WishlistFolder;

import java.util.List;

public interface WishListFolderQueryDSLRepository {

    List<FolderResponseDto> findMemberFolderList(Member member);
    List<WishlistFolder> findByMemberId(Long memberId);

}

