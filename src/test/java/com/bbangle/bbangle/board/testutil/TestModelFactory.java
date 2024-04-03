package com.bbangle.bbangle.board.testutil;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.Category;
import com.bbangle.bbangle.board.domain.Product;
import com.bbangle.bbangle.board.domain.ProductImg;
import com.bbangle.bbangle.store.domain.Store;
import jakarta.persistence.EntityManager;

import java.util.Map;

public class TestModelFactory {

    private EntityManager entityManager;

    public void setEntitiyManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static Store createStore(
            String identifier, String name,
            String profile, String introduce){

        return Store.builder()
                        .identifier(identifier)
                        .name(name)
                        .profile(profile)
                        .introduce(introduce)
                        .build();
    };

    public static Board createBoard(
            Store store, String title, Integer price,
            Boolean status, String profile, String purchaseUrl,
            Integer view, Boolean sunday, Boolean monday,
            Boolean tuesday, Boolean wednesday, Boolean thursday,
            Boolean friday, Boolean saturday
    ){
        return Board.builder()
                        .store(store)
                        .title(title)
                        .price(price)
                        .status(status)
                        .profile(profile)
                        .purchaseUrl(purchaseUrl)
                        .view(view)
                        .sunday(sunday)
                        .monday(monday)
                        .tuesday(tuesday)
                        .wednesday(wednesday)
                        .thursday(thursday)
                        .friday(friday)
                        .saturday(saturday)
                        .build();
    };

    public static Product createProduct(
            Board board, String title, Integer price,
            Category category, Boolean glutenFreeTag,
            Boolean highProteinTag, Boolean sugarFreeTag,
            Boolean veganTag, Boolean ketogenicTag
    ){
        return Product.builder()
                .board(board)
                .title(title)
                .price(price)
                .category(category)
                .glutenFreeTag(glutenFreeTag)
                .highProteinTag(highProteinTag)
                .sugarFreeTag(sugarFreeTag)
                .veganTag(veganTag)
                .ketogenicTag(ketogenicTag)
                .build();
    }

    public static ProductImg createProductImage(
            Board board, String url
    ){
        return ProductImg.builder()
                        .board(board)
                        .url(url)
                        .build();
    }

    public void resetTableIds(Map<String, Integer> tableToStartingId) {
        tableToStartingId.forEach(this::resetId);
    }

    private void resetId(String tableName, Integer startingId) {
        entityManager
                .createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = " + startingId)
                .executeUpdate();
    }
}
