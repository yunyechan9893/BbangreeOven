package com.bbangle.bbangle;

import static java.util.Collections.emptyMap;

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
import java.util.Map.Entry;
import java.util.stream.Collectors;
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

    @BeforeEach
    void before() {
        // 이거 없으면 데이터가 꼬여서 테스트 너무 힘들어서 넣었습니다 살려주세요
        storeRepository.deleteAllInBatch();
        rankingRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
    }

    protected FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();

    /**
     * NOTE: param 에 변경하고자 하는 필드명 : 값 형식으로 주입하면 변경되어 insert 됨
     */
    protected Store fixtureStore(Map<String, Object> params) {
        ArbitraryBuilder<Store> builder = fixtureMonkey.giveMeBuilder(Store.class);
        setBuilderParams(params, builder);

        return storeRepository.save(builder.sample());
    }

    protected Product fixtureProduct(Map<String, Object> params) {
        ArbitraryBuilder<Product> builder = fixtureMonkey.giveMeBuilder(Product.class);
        setBuilderParams(params, builder);

        if (!params.containsKey("board")) {
            builder = builder.set("board", null); // board 가 있으면 에러나서 추가
        }

        Product sample = builder.sample();
        return productRepository.save(sample);
    }

    protected Ranking fixtureRanking(Map<String, Object> params) {
        ArbitraryBuilder<Ranking> builder = fixtureMonkey.giveMeBuilder(Ranking.class);
        setBuilderParams(params, builder);

        if (!params.containsKey("board")) {
            Board board = fixtureBoard(emptyMap());
            builder = builder.set("board", board);
        }

        return rankingRepository.save(builder.sample());
    }

    protected Board fixtureBoard(Map<String, Object> params) {
        ArbitraryBuilder<Board> builder = fixtureMonkey.giveMeBuilder(Board.class);
        setBuilderParams(params, builder);

        if (!params.containsKey("store")) {
            Store store = fixtureStore(emptyMap());
            builder = builder.set("store", store);
        }

        List<Product> products;
        if (!params.containsKey("productList")) {
            products = Collections.singletonList(fixtureProduct(emptyMap()));
            builder = builder.set("productList", products);
        } else {
            products = (List<Product>) params.get("productList");
        }

        Board board = boardRepository.save(builder.sample());

        // product 에 다시 board 를 세팅해줘야 조인이 됨
        productRepository.saveAll(
            products.stream().peek(it -> it.setBoard(board)).collect(Collectors.toList())
        );

        return board;
    }

    private void setBuilderParams(Map<String, Object> params, ArbitraryBuilder builder) {
        for (Entry<String, Object> entry : params.entrySet()) {
            builder = builder.set(entry.getKey(), entry.getValue());
        }
    }
}