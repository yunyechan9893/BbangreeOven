package com.bbangle.bbangle.wishlist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.config.ranking.BoardWishListConfig;
import com.bbangle.bbangle.member.domain.Member;
import com.bbangle.bbangle.member.repository.MemberRepository;
import com.bbangle.bbangle.mock.WithCustomMockUser;
import com.bbangle.bbangle.store.domain.Store;
import com.bbangle.bbangle.store.repository.StoreRepository;
import com.bbangle.bbangle.token.oauth.domain.OauthServerType;
import com.bbangle.bbangle.wishlist.domain.WishListStore;
import com.bbangle.bbangle.wishlist.repository.WishListStoreRepository;
import com.bbangle.bbangle.wishlist.repository.impl.WishListStoreRepositoryImpl;
import com.bbangle.bbangle.wishlist.service.WishListStoreService;
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
    @Autowired
    MockMvc mockMvc;

    @Autowired
    WishListStoreService wishListStoreService;

    @Autowired
    WishListStoreRepository wishListStoreRepository;

    @Autowired
    WishListStoreRepositoryImpl wishListStoreRepositoryImpl;

    @MockBean
    BoardWishListConfig boardWishListConfig;

    @Autowired
    ResponseService responseService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StoreRepository storeRepository;

    @BeforeEach
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new WishListStoreController(wishListStoreService, responseService)).build();
    }

    @BeforeEach
    public void createData(){
        memberRepository.deleteAll();
        storeRepository.deleteAll();
        wishListStoreRepository.deleteAll();
        Member member = Member.builder()
            .email("test@email.com")
            .name("testUser")
            .provider(OauthServerType.KAKAO)
            .isDeleted(false)
            .build();
        memberRepository.save(member);
        createWishListStore(member);

    }

    private void createWishListStore(Member member) {
        for(int i = 1; i <= 25; i++){
            Store store= Store.builder()
                .name("test"+i)
                .introduce("introduce"+i)
                .isDeleted(false)
                .build();
            storeRepository.save(store);
            if(i != 25){
                WishListStore wishlistStore = WishListStore.builder()
                    .member(member)
                    .store(store)
                    .build();
                wishListStoreRepository.save(wishlistStore);
            }
        }
    }

    @DisplayName("위시리스트 스토어 전체 조회를 시행한다")
    @Test
    @WithCustomMockUser
    public void getWishListStores() throws Exception{
        mockMvc.perform(get("/api/v1/likes/stores"))
                .andExpect(jsonPath("$.result.hasNext").value(true))
                .andExpect(jsonPath("$.result.nextCursor").value(4))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("위시리스트 삭제를 시행한다")
    @Test
    @WithCustomMockUser
    public void deleteWishListStore() throws Exception{
        mockMvc.perform(patch("/api/v1/likes/store/75"))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @DisplayName("위시리스트 추가를 시행한다")
    @Test
    @WithCustomMockUser
    public void addWishListStore() throws Exception{
        mockMvc.perform(post("/api/v1/likes/store/51"))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
