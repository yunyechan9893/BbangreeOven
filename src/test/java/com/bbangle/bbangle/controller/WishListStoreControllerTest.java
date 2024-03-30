package com.bbangle.bbangle.controller;

import com.bbangle.bbangle.member.controller.ProfileController;
import com.bbangle.bbangle.wishListStore.controller.WishListStoreController;
import com.bbangle.bbangle.wishListStore.repository.WishListStoreRepositoryImpl;
import com.bbangle.bbangle.wishListStore.repository.WishListStoreServiceImpl;
import com.bbangle.bbangle.wishListStore.service.WishListStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WishListStoreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WishListStoreServiceImpl wishListStoreService;

    @Autowired
    WishListStoreRepositoryImpl wishListStoreRepository;

    @BeforeEach
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new WishListStoreController(wishListStoreService)).build();
    }

    private final String BEARER = "Bearer";
    private final String AUTHORIZATION = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJiYmFuZ2xlYmJhbmdsZSIsImlhdCI6MTcxMDU5NDgzMSwiZXhwIjoxNzEwNjA1NjMxLCJzdWIiOiJkc3lvb24xOTk0QGdtYWlsLmNvbSIsImlkIjoxMn0.EdRBBy5Orzlv_oZm4hGRZQZZ79_H-1JJHjzXZxlM_YU";

    @DisplayName("위시리스트 스토어 전체 조회를 시행한다")
    @Test
    public void getWishListStores() throws Exception{

        mockMvc.perform(get("/api/v1/likes/stores")
                .header("Authorization", String.format("%s %s",BEARER, AUTHORIZATION))
                .param("page", "0")
                .param("size", "1")
                .param("sort", "createdAt,DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].introduce").value("건강을 먹다_로썸"))
                .andDo(print());


    }
}
