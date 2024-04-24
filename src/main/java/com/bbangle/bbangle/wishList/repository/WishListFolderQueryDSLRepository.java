package com.bbangle.bbangle.wishList.repository;

import com.bbangle.bbangle.wishList.dto.FolderResponseDto;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.wishList.domain.WishlistFolder;

import java.util.List;

public interface WishListFolderQueryDSLRepository {

    List<FolderResponseDto> findMemberFolderList(Member member);
    List<WishlistFolder> findByMemberId(Long memberId);

}

