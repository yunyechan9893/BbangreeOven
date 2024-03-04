package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.FolderResponseDto;
import com.bbangle.bbangle.member.domain.Member;
import java.util.List;

public interface WishListFolderQueryDSLRepository {

    List<FolderResponseDto> findMemberFolderList(Member member);

}

