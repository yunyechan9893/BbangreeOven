package com.bbangle.bbangle.board.repository.folder;

import static com.bbangle.bbangle.board.repository.BoardRepositoryImpl.BOARD_PAGE_SIZE;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.Product;
import com.bbangle.bbangle.board.domain.TagEnum;
import com.bbangle.bbangle.board.dto.BoardResponseDto;
import com.bbangle.bbangle.page.BoardCustomPage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardPageGenerator {

    private static final Long NO_NEXT_CURSOR = -1L;
    private static final Long HAS_NEXT_PAGE_SIZE = BOARD_PAGE_SIZE + 1L;

    public BoardCustomPage<List<BoardResponseDto>> getBoardPage(List<Board> allByFolder){
        if(allByFolder.isEmpty()){
            return BoardCustomPage.emptyPage();
        }

        List<BoardResponseDto> boardResponseDtos = convertToBoardResponse(allByFolder);
        Long nextCursor = NO_NEXT_CURSOR;
        boolean hasNext = false;
        if(allByFolder.size() == HAS_NEXT_PAGE_SIZE){
            hasNext = true;
            nextCursor = allByFolder.get(allByFolder.size()-1).getId();
        }
        return BoardCustomPage.from(boardResponseDtos,
            nextCursor, hasNext);
    }

    private List<BoardResponseDto> convertToBoardResponse(List<Board> boards) {
        Map<Long, List<String>> tagMapByBoardId = boards.stream()
            .collect(Collectors.toMap(
                Board::getId,
                board -> extractTags(board.getProductList())
            ));

        return boards.stream()
            .limit(BOARD_PAGE_SIZE)
            .map(board -> BoardResponseDto.from(board, tagMapByBoardId.get(board.getId())))
            .toList();
    }

    private List<String> extractTags(List<Product> products) {
        if (Objects.isNull(products) || products.isEmpty()) {
            return Collections.emptyList();
        }

        HashSet<String> tags = new HashSet<>();
        for (Product dto : products) {
            addTagIfTrue(tags, dto.isGlutenFreeTag(), TagEnum.GLUTEN_FREE.label());
            addTagIfTrue(tags, dto.isHighProteinTag(), TagEnum.HIGH_PROTEIN.label());
            addTagIfTrue(tags, dto.isSugarFreeTag(), TagEnum.SUGAR_FREE.label());
            addTagIfTrue(tags, dto.isVeganTag(), TagEnum.VEGAN.label());
            addTagIfTrue(tags, dto.isKetogenicTag(), TagEnum.KETOGENIC.label());
        }
        return new ArrayList<>(tags);
    }

    private void addTagIfTrue(Set<String> tags, boolean condition, String tag) {
        if (condition) {
            tags.add(tag);
        }
    }

}
