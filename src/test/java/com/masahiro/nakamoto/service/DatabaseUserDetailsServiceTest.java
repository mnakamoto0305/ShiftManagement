package com.masahiro.nakamoto.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.domain.SiteUser;
import com.masahiro.nakamoto.mybatis.UserMapper;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/test.properties")
class DatabaseUserDetailsServiceTest {

	@Autowired
	UserMapper userMapper;

	@Autowired
	DatabaseUserDetailsService service;


	@Test
	void ユーザ名が存在する際にユーザ詳細を取得できる() {
		//SetUp
		SiteUser user = new SiteUser();
		user.setId("koto@sample.com");
		user.setPassword("$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG");
		user.setRole("USER");
		user.setPassUpdateDate("2021-04-12 12:29:41");
		//Excersize
		UserDetails actual = service.loadUserByUsername("koto@sample.com");
		//Verify
		assertEquals(user.getUsername(), actual.getUsername());
	}

	@Test
	public void ユーザ名が存在しない際に例外を送出する() throws Exception {
		assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("sumida3@samplel.com"));
	}

}
