package com.bbangle.bbangle.repository.impl;

import com.bbangle.bbangle.model.ProductImg;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImgRepository extends JpaRepository<ProductImg, Long> {

}
