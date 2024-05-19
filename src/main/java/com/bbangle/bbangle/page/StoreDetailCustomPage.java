package com.bbangle.bbangle.page;

import com.bbangle.bbangle.store.dto.StoreBoardsResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class StoreDetailCustomPage<T> extends CustomPage<T> {

    public StoreDetailCustomPage(T content, Long requestCursor, Boolean hasNext) {
        super(content, requestCursor, hasNext);
    }

    public static StoreDetailCustomPage<List<StoreBoardsResponse>> from(
        List<StoreBoardsResponse> storeBoardsResponse,
        Long requestCursor,
        Boolean hasNext
    ) {
        return new StoreDetailCustomPage<>(storeBoardsResponse, requestCursor, hasNext);
    }

}