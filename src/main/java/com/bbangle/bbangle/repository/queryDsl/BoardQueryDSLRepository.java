package com.bbangle.bbangle.repository.queryDsl;

import com.bbangle.bbangle.dto.BoardDetailResponseDto;
import com.bbangle.bbangle.dto.BoardResponseDto;
import com.bbangle.bbangle.model.WishlistFolder;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardQueryDSLRepository {

    List<BoardResponseDto> getBoardResponseDto(
        String sort, Boolean glutenFreeTag, Boolean highProteinTag,
        Boolean sugarFreeTag, Boolean veganTag, Boolean ketogenicTag,
        String category, Integer minPrice, Integer maxPrice
    );

    Slice<BoardResponseDto> getAllByFolder(
        String sort,
        Pageable pageable,
        Long wishListFolderId,
        WishlistFolder wishlistFolder
    );

    BoardDetailResponseDto getBoardDetailResponseDto(Long boardId);

    HashMap<Long, String> getAllBoardTitle();

    List<BoardResponseDto> updateLikeStatus(List<Long> matchedIdx, List<BoardResponseDto> content);
    BoardDetailResponseDto getDetailLikeUpdate(BoardDetailResponseDto content);

}

