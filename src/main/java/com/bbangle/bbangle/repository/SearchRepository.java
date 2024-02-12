package com.bbangle.bbangle.repository;

import com.bbangle.bbangle.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchQueryDSLRepository {
}
