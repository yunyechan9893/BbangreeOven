package com.bbangle.bbangle.wishList.repository;

import com.bbangle.bbangle.wishList.domain.WishListFolder;
import com.bbangle.bbangle.wishList.dto.FolderResponseDto;
import com.bbangle.bbangle.member.domain.Member;

import java.util.List;

public interface WishListFolderQueryDSLRepository {

    List<FolderResponseDto> findMemberFolderList(Member member);
    List<WishListFolder> findByMemberId(Long memberId);

}

