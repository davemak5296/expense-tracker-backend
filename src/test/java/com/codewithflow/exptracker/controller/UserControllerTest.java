package com.codewithflow.exptracker.controller;

import com.codewithflow.exptracker.AbstractTestContainer;
import com.codewithflow.exptracker.dto.UserReqDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class UserControllerTest extends AbstractTestContainer {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<UserReqDTO> jsonUserDTO;

    @Test
    public void canRetrieveByIdWhenExists() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                get("/user/1")
                        .accept("application/json"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString()).isEqualTo(
                jsonUserDTO.write(new UserReqDTO("zzz@gmail.com", "test1234", "test1111", "zzz")).getJson()
        );
    }
}
