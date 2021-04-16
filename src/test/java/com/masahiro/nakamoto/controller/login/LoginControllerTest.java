package com.masahiro.nakamoto.controller.login;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:/test.properties")
@AutoConfigureMockMvc
class LoginControllerTest {

	@Autowired
	private MockMvc sut;

	@Autowired
	private WebApplicationContext context;

	@Autowired
    private FilterChainProxy springSecurityFilterChain;

	@InjectMocks
	private LoginController loginController;

	@Before
	void setUp() {
		sut = MockMvcBuilders
			.webAppContextSetup(context)
			.apply(springSecurity(springSecurityFilterChain))
			.build();
	}

	@Test
	void ログイン画面が正常に表示される事() throws Exception {
		sut.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("メールアドレス")))
			.andExpect(content().string(containsString("パスワード")));
	}

	@Test
	@WithMockUser(username = "koto@sample.com", password = "password")
    void DB上に存在する利用者ユーザでログインできる() throws Exception {
        sut.perform(get("/"))
            .andExpect(status().isOk());
    }

	@Test
	@WithMockUser(username = "koto@sample.com", password = "password", roles = "USER")
    void 一般ユーザーは管理者画面にアクセスできない() throws Exception {
        sut.perform(get("/admin"))
            .andExpect(status().is4xxClientError());
    }

	@Test
	@WithMockUser(username = "koto@sample.com", password = "password", roles = "ADMIN")
    void 管理者は管理者画面にアクセスできない() throws Exception {
        sut.perform(get("/admin"))
            .andExpect(status().isOk());
    }

}
