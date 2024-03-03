package com.bbangle.bbangle.repository.queryDsl;

import java.util.HashMap;
import java.util.List;
import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.model.WishlistFolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardQueryDSLRepository {

    List<BoardResponseDto> getBoardResponseDto(String sort, Boolean glutenFreeTag, Boolean highProteinTag,
                                               Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
                                               String category, Integer minPrice, Integer maxPrice);

    Slice<BoardResponseDto> getAllByFolder(String sort, Pageable pageable, Long wishListFolderId, WishlistFolder wishlistFolder);
    BoardDetailResponseDto getBoardDetailResponseDto(Long boardId);

    BoardDetailResponseDto getBoardDetailResponseDtoWithLike(Long memberId, Long boardId);

    HashMap<Long, String> getAllBoardTitle();
}

