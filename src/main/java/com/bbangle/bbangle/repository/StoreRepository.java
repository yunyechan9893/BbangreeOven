package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.dto.BoardDto;
import com.bbangle.bbangle.model.Store;
import com.bbangle.bbangle.repository.queryDsl.StoreQueryDSLRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreQueryDSLRepository {

    Slice<Store> findSliceBy(Pageable pageable);

    Page<Store> findByIdIn(
        @Param("ids")
        List<Long> ids, Pageable pageable
    );

}
