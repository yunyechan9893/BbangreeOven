package com.bbangle.bbangle.repository;

import java.util.List;
import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.dto.FolderResponseDto;
import com.bbangle.bbangle.model.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListFolderQueryDSLRepository {
    List<FolderResponseDto> findMemberFolderList(Member member);
}

