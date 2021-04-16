package com.masahiro.nakamoto.mybatis;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.masahiro.nakamoto.domain.SiteUser;

@RunWith(SpringRunner.class)
@MybatisTest
@TestPropertySource(locations = "classpath:/test.properties")
class UserMapperTest {

	@Autowired
	UserMapper sut;

	@Test
	public void identifyUserで指定したIDのユーザー情報を取得できる() throws Exception {
		SiteUser user = sut.identifyUser("koto@sample.com");

		assertThat(user.getId(), is("koto@sample.com"));
		assertThat(user.getPassword(), is("$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG"));
		assertThat(user.getPassUpdateDate(), is("2021-04-12 12:29:41"));
		assertThat(user.getRole(), is("USER"));
	}

}
