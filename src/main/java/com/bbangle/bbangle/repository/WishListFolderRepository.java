package com.bbangle.bbangle.repository;

import java.util.Optional;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.model.WishlistFolder;
import com.bbangle.bbangle.repository.queryDsl.WishListFolderQueryDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListFolderRepository extends JpaRepository<WishlistFolder, Long>, WishListFolderQueryDSLRepository {

    @Query(value = "select count(folder) from WishlistFolder folder where folder.member = :member and folder.isDeleted = false ")
    int getFolderCount(@Param("member") Member member);

    Optional<WishlistFolder> findByMemberAndId(Member member, Long folderId);

}
