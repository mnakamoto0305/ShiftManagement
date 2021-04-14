package com.masahiro.nakamoto.service;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.experimental.runners.Enclosed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.masahiro.nakamoto.AlreadyRegisteredException;
import com.masahiro.nakamoto.IllegalCourseException;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.mybatis.CourseMapper;
import com.masahiro.nakamoto.mybatis.DriverMapper;
import com.masahiro.nakamoto.mybatis.UserMapper;

@RunWith(Enclosed.class)
class DriverServiceTest {

	public static class 例外処理 {

		@Mock
		private DriverMapper driverMapper;

		@Mock
		private CourseMapper courseMapper;

		@InjectMocks
		private DriverService sut;

		@BeforeEach
	    public void initmocks() {
	        MockitoAnnotations.initMocks(this);
	    }

		@Before
		public void setUp() throws Exception {
			sut = new DriverService();
		}

		@Test
		public void isRegisteredで既にドライバーが登録されていた場合例外を送出する() throws Exception {
			// SetUp
			Driver driver = new Driver("koto@sample.com", "佐藤", "太郎", 2, "1", "1990-01-01", "08000000000", 1000004, "東京都千代田区大手町1-1-1", "2020-01-01", "$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG", "2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
			when(driverMapper.isRegistered(driver)).thenReturn(getDriver1());

			//Verify
			assertThatThrownBy(() -> {
				sut.isRegistered(driver);
			}).isInstanceOf(AlreadyRegisteredException.class);
		}

		@Test
		public void isCorrectCourseで不正なコース番号が指定された場合例外を送出する() throws Exception {
			// SetUp
			Driver driver = new Driver("koto@sample.com", "佐藤", "太郎", 2, "1", "1990-01-01", "08000000000", 1000004, "東京都千代田区大手町1-1-1", "2020-01-01", "$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG", "2021-04-12 12:29:41", "USER", 1, 8, 20000, 30000);
			when(courseMapper.findTotalCourses(driver.getAreaId())).thenReturn(7);
			// Verify
			assertThatThrownBy(() -> {
				sut.isCorrectCourse(driver);
			}).isInstanceOf(IllegalCourseException.class);
		}

		public Driver getDriver1() {
			Driver actual = new Driver("koto@sample.com", "佐藤", "太郎", 2, "1", "1990-01-01", "08000000000", 1000004, "東京都千代田区大手町1-1-1", "2020-01-01", "$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG", "2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
			return actual;
		}
	}

	public static class パスワードの確認 {

		@Mock
		UserMapper userMapper;

		@Mock
		PasswordEncoder passwordEncoder;

		@InjectMocks
		DriverService sut;

		@BeforeEach
	    public void initmocks() {
	        MockitoAnnotations.initMocks(this);
	    }

		@Before
		public void setUp() throws Exception {
			sut = new DriverService();
		}

		@Test
		public void dbに登録されているパスワードを入力したらTrueを返す() throws Exception {
			// SetUp
			when(userMapper.getPassword("koto@sample.com")).thenReturn("$2a$10$Gb3vobOVIpg8bzUgihtONO4kUoeGmYSu7pZE/SNVhItmJNnkhTf4a");
			when(passwordEncoder.matches("password", userMapper.getPassword("koto@sample.com"))).thenReturn(true);

			// Verify
			assertThat(sut.isCorrectPassword("password", "koto@sample.com"), is(true));
		}

		@Test
		public void dbに登録されていないパスワードを入力したらFalseを返す() throws Exception {
			// SetUp
			when(userMapper.getPassword("koto@sample.com")).thenReturn("$2a$10$Gb3vobOVIpg8bzUgihtONO4kUoeGmYSu7pZE/SNVhItmJNnkhTf4a");
			when(passwordEncoder.matches("password", userMapper.getPassword("koto@sample.com"))).thenReturn(true);

			// Verify
			assertThat(sut.isCorrectPassword("p@ssword", "koto@sample.com"), is(false));
		}

	}

}
