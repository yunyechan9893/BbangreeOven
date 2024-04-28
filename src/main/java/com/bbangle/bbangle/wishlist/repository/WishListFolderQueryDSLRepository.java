package com.bbangle.bbangle.wishlist.repository;

import com.bbangle.bbangle.wishlist.dto.FolderResponseDto;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.wishlist.domain.WishlistFolder;

import java.util.List;

public interface WishListFolderQueryDSLRepository {

    List<FolderResponseDto> findMemberFolderList(Member member);
    List<WishlistFolder> findByMemberId(Long memberId);

}

