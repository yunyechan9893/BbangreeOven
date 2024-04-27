package com.bbangle.bbangle;

import com.bbangle.bbangle.board.domain.Board;
import com.bbangle.bbangle.board.domain.Product;
import com.bbangle.bbangle.board.repository.BoardRepository;
import com.bbangle.bbangle.board.repository.ProductRepository;
import com.bbangle.bbangle.ranking.domain.Ranking;
import com.bbangle.bbangle.ranking.repository.RankingRepository;
import com.bbangle.bbangle.store.domain.Store;
import com.bbangle.bbangle.store.repository.StoreRepository;
import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class AbstractIntegrationTest {

    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    protected StoreRepository storeRepository;
    @Autowired
    protected RankingRepository rankingRepository;
    @Autowired
    protected ProductRepository productRepository;

    protected FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();

    protected Store fixtureStore() {
        Store store = fixtureMonkey.giveMeOne(Store.class);
        return storeRepository.save(store);
    }

    protected Product fixtureProduct() {
        Product product = fixtureMonkey.giveMeOne(Product.class);
        return productRepository.save(product);
    }

    protected Board fixtureBoard() {
        Store store = fixtureStore();
        List<Product> products = Collections.singletonList(fixtureProduct());

        Board targetBoard = fixtureMonkey.giveMeBuilder(Board.class)
            .set("store", store)
            .set("productList", products)
            .sample();

        return boardRepository.save(targetBoard);
    }

}
