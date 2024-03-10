package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.FolderResponseDto;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.model.WishlistFolder;

import java.util.List;

public interface WishListFolderQueryDSLRepository {

    List<FolderResponseDto> findMemberFolderList(Member member);
    List<WishlistFolder> findByMemberId(Long memberId);

}

