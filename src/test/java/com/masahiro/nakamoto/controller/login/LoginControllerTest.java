package com.masahiro.nakamoto.controller.login;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.masahiro.nakamoto.CsvDataSetLoader;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
      DependencyInjectionTestExecutionListener.class,
      TransactionalTestExecutionListener.class,
      DbUnitTestExecutionListener.class,
    })
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginControllerTest {

	@Autowired
	private MockMvc sut;

	@Test
	void ログイン画面が正常に表示される事() throws Exception {
		sut.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("メールアドレス")))
			.andExpect(content().string(containsString("パスワード")));
	}

	@Test
	@DatabaseSetup("classpath:/testData/")
	public void テストユーザーでログインできる() throws Exception {
		this.sut.perform(formLogin("/login")
                .user("koto@sample.com")
                .password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/top_user/init"));
	}

}
