package com.bbangle.bbangle.wishList.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.config.ranking.BoardWishListConfig;
import com.bbangle.bbangle.wishList.repository.impl.WishListStoreRepositoryImpl;
import com.bbangle.bbangle.wishList.service.WishListStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class WishListStoreControllerTest {
    //BeforeEach로 데이터 넣기

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WishListStoreService wishListStoreService;

    @Autowired
    WishListStoreRepositoryImpl wishListStoreRepository;

    @MockBean
    BoardWishListConfig boardWishListConfig;

    @Autowired
    ResponseService responseService;

    @BeforeEach
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new WishListStoreController(wishListStoreService, responseService)).build();
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
                .andExpect(jsonPath("$.contents[0].wished").value(true))
                .andExpect(jsonPath("$.contents[0].profile").value("https://firebasestorage.googleapis.com/v0/b/test-1949b.appspot.com/o/stores%2Frawsome%2Fprofile.jpg?alt=media&token=26bd1435-2c28-4b85-a5aa-b325e9aac05e"))
                .andDo(print());

    }

    @DisplayName("위시리스트 삭제를 시행한다")
    @Test
    public void deleteWishListStore() throws Exception{
        mockMvc.perform(patch("/api/v1/likes/store/1")
            .header("Authorization", String.format("%s %s",BEARER, AUTHORIZATION)))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
