package com.bbangle.bbangle.repository.queryDsl;

import java.util.List;
import com.bbangle.bbangle.dto.FolderResponseDto;
import com.bbangle.bbangle.member.domain.Member;

public interface WishListFolderQueryDSLRepository {
    List<FolderResponseDto> findMemberFolderList(Member member);
}

