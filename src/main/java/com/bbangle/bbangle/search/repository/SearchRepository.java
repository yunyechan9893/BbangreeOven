package com.bbangle.bbangle.search.repository;

import com.bbangle.bbangle.search.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchQueryDSLRepository {

}
