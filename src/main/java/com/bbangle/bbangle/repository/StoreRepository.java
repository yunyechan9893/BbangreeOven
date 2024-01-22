package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreQueryDSLRepository {
    Slice<Store> findSliceBy(Pageable pageable);
}
