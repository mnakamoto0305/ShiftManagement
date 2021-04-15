package com.masahiro.nakamoto.mybatis;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.form.DriverForm;
import com.masahiro.nakamoto.domain.form.InfomationForm;

@RunWith(SpringRunner.class)
@MybatisTest
@TestPropertySource(locations = "classpath:/resources/test.properties")
public class DriverMapperTest {

	@Autowired
	private DriverMapper sut;

	@Autowired
	private UserMapper userMapper;


	@Test
	public void findAllでドライバー情報を全件取得してリストで受け取る() throws Exception {
		List<Driver> actual = sut.findAll();

		assertThat(actual.size(), is(3));
	}

	@Test
	public void findDriverInfoで指定したドライバーの情報を1件取得できる() throws Exception {
		Driver actual = sut.findDriverInfo("koto@sample.com");

		assertThat(actual.getId(), is("koto@sample.com"));
		assertThat(actual.getLastName() , is("佐藤"));
		assertThat(actual.getFirstName() , is("太郎"));
		assertThat(actual.getPositionId() , is(2));
		assertThat(actual.getSex() , is("1"));
		assertThat(actual.getDateOfBirth() , is("1990-01-01"));
		assertThat(actual.getPostalCode() , is("1000004"));
		assertThat(actual.getAddress() , is("東京都千代田区大手町1-1-1"));
		assertThat(actual.getPhoneNumber() , is("08012345678"));
		assertThat(actual.getJoinDate() , is("2020-01-01"));
		assertThat(actual.getAreaId() , is(1));
		assertThat(actual.getCourseId() , is(1));
		assertThat(actual.getDailyWages() , is(20000));
		assertThat(actual.getMonthlyExpenses() , is(30000));
		assertThat(actual.getPassword() , is("$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG"));
		assertThat(actual.getRole() , is("USER"));
		assertThat(actual.getPassUpdateDate() , is("2021-04-12 12:29:41"));
	}

	@Test
	public void findAreaDriverで指定した拠点IDのドライバー情報をリストで受け取る() throws Exception {
		List<Driver> actual = sut.findAreaDriver(1);

		assertThat(actual.size(), is(2));
	}

	@Test
	public void findCourseDriverで指定した拠点とコースIDのドライバー情報を受け取る() throws Exception {
		Driver actual = sut.findCourseDriver(1, 1);

		assertThat(actual.getId(), is("koto@sample.com"));
		assertThat(actual.getLastName() , is("佐藤"));
		assertThat(actual.getFirstName() , is("太郎"));
		assertThat(actual.getPositionId() , is(2));
		assertThat(actual.getSex() , is("1"));
		assertThat(actual.getDateOfBirth() , is("1990-01-01"));
		assertThat(actual.getPostalCode() , is("1000004"));
		assertThat(actual.getAddress() , is("東京都千代田区大手町1-1-1"));
		assertThat(actual.getPhoneNumber() , is("08012345678"));
		assertThat(actual.getJoinDate() , is("2020-01-01"));
		assertThat(actual.getAreaId() , is(1));
		assertThat(actual.getCourseId() , is(1));
		assertThat(actual.getDailyWages() , is(20000));
		assertThat(actual.getMonthlyExpenses() , is(30000));
		assertThat(actual.getPassword() , is("$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG"));
		assertThat(actual.getRole() , is("USER"));
		assertThat(actual.getPassUpdateDate() , is("2021-04-12 12:29:41"));
	}

	@Test
	public void findCourseDriverでドライバーが登録されていないコースIDを指定するとNullを返す() throws Exception {
		Driver actual = sut.findCourseDriver(1, 4);
		assertThat(actual, is(nullValue()));
	}

	@Test
	public void findFromFormで名字を入力するとドライバー情報を取得できる() throws Exception {
		//SetUp
		DriverForm driverForm = new DriverForm();
		driverForm.setSearchWord("佐藤");

		//Excercise
		List<Driver> actual = sut.findFromForm(driverForm);

		//Verify
		assertThat(actual.size(), is(2));
	}

	@Test
	public void findFromFormでメールアドレスを指定するとドライバー情報を取得できる() throws Exception {
		//SetUp
		DriverForm driverForm = new DriverForm();
		driverForm.setSearchWord("koto@sample");

		//Excercise
		List<Driver> actual = sut.findFromForm(driverForm);

		//Verify
		assertThat(actual.get(0).getId(), is("koto@sample.com"));
		assertThat(actual.get(0).getLastName() , is("佐藤"));
		assertThat(actual.get(0).getFirstName() , is("太郎"));
		assertThat(actual.get(0).getPositionId() , is(2));
		assertThat(actual.get(0).getSex() , is("1"));
		assertThat(actual.get(0).getDateOfBirth() , is("1990-01-01"));
		assertThat(actual.get(0).getPostalCode() , is("1000004"));
		assertThat(actual.get(0).getAddress() , is("東京都千代田区大手町1-1-1"));
		assertThat(actual.get(0).getPhoneNumber() , is("08012345678"));
		assertThat(actual.get(0).getJoinDate() , is("2020-01-01"));
		assertThat(actual.get(0).getAreaId() , is(1));
		assertThat(actual.get(0).getCourseId() , is(1));
		assertThat(actual.get(0).getDailyWages() , is(20000));
		assertThat(actual.get(0).getMonthlyExpenses() , is(30000));
		assertThat(actual.get(0).getPassword() , is("$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG"));
		assertThat(actual.get(0).getRole() , is("USER"));
		assertThat(actual.get(0).getPassUpdateDate() , is("2021-04-12 12:29:41"));
	}

	@Test
	public void createDriverでドライバー情報を1件登録できる() throws Exception {
		//SetUp
		Driver driver = new Driver("edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG", "2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);

		//Excercize
		sut.createDriver(driver);
		userMapper.createDriver(driver);
		List<Driver> actual = sut.findAll();

		//Verify
		assertThat(actual.size(), is(4));
	}

	@Test
	public void updateDriverでドライバー情報を1件更新できる() throws Exception {
		//SetUp
		Driver driver = new Driver("koto@sample.com", "加藤", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG", "2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		driver.setPreviousId("koto@sample.com");

		//Excercize
		sut.updateDriver(driver);
		Driver actual = sut.findDriverInfo("koto@sample.com");

		//verify
		assertThat(actual.getLastName(), is("加藤"));
		assertThat(actual.getSex(), is("1"));
	}

	@Test
	public void deleteDriverでドライバー情報を1件削除できる() throws Exception {
		sut.deleteDriver("koto@sample.com");
		List<Driver> actual = sut.findAll();
		assertThat(actual.size(), is(2));
	}

	@Test
	public void updateInfomationでドライバー情報を更新できる() throws Exception {
		// SetUp
		InfomationForm form = new InfomationForm("koto@sample.com", "08012341234", "1000005", "東京都千代田区丸の内1-1-1");
		form.setPreviousId("koto@sample.com");

		// Exercise
		sut.updateInfomation(form);
		Driver actual = sut.findDriverInfo("koto@sample.com");

		// Verify
		assertThat(actual.getId(), is("koto@sample.com"));
		assertThat(actual.getPhoneNumber(), is("08012341234"));
		assertThat(actual.getPostalCode(), is("1000005"));
		assertThat(actual.getAddress(), is("東京都千代田区丸の内1-1-1"));
	}

	@Test
	public void getInfomationFormで更新前のデータを取得できる() throws Exception {
		InfomationForm actual = sut.getInfomationForm("koto@sample.com");

		assertThat(actual.getId(), is("koto@sample.com"));
		assertThat(actual.getPhoneNumber(), is("08012345678"));
		assertThat(actual.getPostalCode(), is("1000004"));
		assertThat(actual.getAddress(), is("東京都千代田区大手町1-1-1"));
	}

}
