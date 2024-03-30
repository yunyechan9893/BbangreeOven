package com.bbangle.bbangle.controller;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LandingPageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }



    @Test
    @DisplayName("이메일 값이 비어있는지 확인한다")
    public void notEmptyEmail() throws Exception{
        mockMvc.perform(post("/api/v1/landingpage")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"''\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 값이 형식에 맞지 않은지 확인한다")
    public void checkEmailForm() throws Exception{
        mockMvc.perform(post("/api/v1/landingpage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"abcabc@abc\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일이 형식에 맞는지 확인한다")
    public void checkRightEmailForm() throws Exception{
        mockMvc.perform(post("/api/v1/landingpage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"sikdong@naver.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }


}
