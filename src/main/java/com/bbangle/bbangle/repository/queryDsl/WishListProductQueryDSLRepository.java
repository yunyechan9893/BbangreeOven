package com.bbangle.bbangle.repository.queryDsl;

import java.util.List;
import java.util.Optional;
import com.bbangle.bbangle.model.WishlistFolder;
import com.bbangle.bbangle.model.WishlistProduct;
import org.springframework.data.repository.query.Param;

public interface WishListProductQueryDSLRepository {

    List<WishlistProduct> findAllByWishlistFolder(WishlistFolder wishlistFolder, String sort);

}
