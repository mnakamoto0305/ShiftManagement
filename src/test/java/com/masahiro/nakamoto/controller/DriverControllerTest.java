package com.masahiro.nakamoto.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.masahiro.nakamoto.AlreadyRegisteredException;
import com.masahiro.nakamoto.IllegalCourseException;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.form.DriverForm;
import com.masahiro.nakamoto.domain.form.PassChangeConfirmForm;
import com.masahiro.nakamoto.service.AccountingService;
import com.masahiro.nakamoto.service.DriverService;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:/resources/test.properties")
@AutoConfigureMockMvc
class DriverControllerTest {

	@Autowired
	private MockMvc sut;

	@MockBean
	private DriverService driverService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private AccountingService accountingService;

	@Test
	@WithMockUser
	public void ドライバー検索ページのリクエスト結果が正常となりViewとしてadminLayoutが返る事() throws Exception {
		sut.perform(get("/search/driver"))
			.andExpect(status().isOk())
			.andExpect(view().name("main/adminLayout"))
		 	.andExpect(model().attribute("contents", "driver/form :: form"));
	}

	@Test
	@WithMockUser
	public void 検索フォームを空欄で送信するとfindAllメソッドが呼ばれて結果画面が返る事() throws Exception {
		// SetUp
		DriverForm driverForm = new DriverForm();
		driverForm.setSearchWord(null);
		// Verify
		sut.perform(post("/search/driver/result").flashAttr("driverForm", driverForm))
			.andExpect(status().isOk())
			.andExpect(view().name("main/adminLayout"))
			.andExpect(model().attribute("contents", "driver/result :: result"));

		verify(driverService, times(1)).findAll();
	}

	@Test
	@WithMockUser
	public void 検索フォームに入力して検索するとfindFromFormメソッドが呼ばれて結果画面が返る事() throws Exception {
		// SetUp
		DriverForm driverForm = new DriverForm();
		driverForm.setSearchWord("佐藤");
		// Verify
		sut.perform(post("/search/driver/result").flashAttr("driverForm", driverForm))
			.andExpect(status().isOk())
			.andExpect(view().name("main/adminLayout"))
			.andExpect(model().attribute("contents", "driver/result :: result"));

		verify(driverService, times(1)).findFromForm(driverForm);
	}

	@Test
	@WithMockUser
	public void 検索フォームで拠点を選択するとfindAreaDriverメソッドが呼ばれて結果画面が返る事() throws Exception {
		// SetUp
		DriverForm driverForm = new DriverForm();
		driverForm.setAreaId(1);
		// Verify
		sut.perform(post("/search/driver/result/area").flashAttr("driverForm", driverForm))
			.andExpect(status().isOk())
			.andExpect(view().name("main/adminLayout"))
			.andExpect(model().attribute("contents", "driver/result :: result"));

		verify(driverService, times(1)).findAreaDriver(driverForm.getAreaId());
	}

	@Test
	@WithMockUser
	public void ドライバー登録ページのリクエスト結果が正常となりViewとしてadminLayoutが返る事() throws Exception {
		sut.perform(get("/create/driver"))
			.andExpect(status().isOk())
			.andExpect(view().name("main/adminLayout"))
		 	.andExpect(model().attribute("contents", "driver/create :: createForm"));
	}

	@Test
	@WithMockUser
	public void 新規登録ページで登録処理を行うとサービスで処理されて登録ページにリダイレクトする事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		String password = driver.getPassword();
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/create/driver"));

		verify(passwordEncoder, times(1)).encode(password);
		verify(driverService, times(1)).createDriver(driver);
	}

	@Test
	@WithMockUser
	public void 指定したコース番号が存在しない場合に例外を検出して画面にエラーメッセージを表示させる事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 8, 20000, 30000);
		doThrow(new IllegalCourseException()).when(driverService).isCorrectCourse(driver);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(view().name("main/adminLayout"))
		.andExpect(model().attribute("illegalCourse", "この拠点には指定したコース番号が存在しません。"))
		.andExpect(model().attribute("contents", "driver/create :: createForm"));

		verify(driverService, times(1)).isCorrectCourse(driver);
	}

	@Test
	@WithMockUser
	public void 指定したコースにドライバーが登録されている場合に例外を検出して画面にエラーメッセージを表示させる事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		doThrow(new AlreadyRegisteredException()).when(driverService).isRegistered(driver);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(view().name("main/adminLayout"))
		.andExpect(model().attribute("registered", "指定したコースは既に他のドライバーが登録されています。"))
		.andExpect(model().attribute("contents", "driver/create :: createForm"));

		verify(driverService, times(1)).isRegistered(driver);
	}

	@Test
	@WithMockUser
	public void ドライバー登録ページでメールアドレスをblankにすると例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void パスワードとパスワード確認用が一致しないと例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "passwordpassword","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void メールアドレスとメールアドレス確認用が一致しないと例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void 生年月日をハイフンつなぎで入力しないと例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990/01/01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void 電話番号が10桁か11桁でない場合に例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "012345678", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void 郵便番号が7桁でない場合に例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "10000045", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}
	@Test
	@WithMockUser
	public void 入社日がハイフンつなぎで入力しないと例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020/01/01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void パスワードが8文字未満の場合に例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "passwor", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void パスワードに半角英数字以外を使うと例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password!","2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void 単価に負の値を入力すると例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, -20000, 30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void 経費に負の値を入力すると例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("edogawa3@sample.com", "edogawa3@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password","2021-04-12 12:29:41", "USER", 1, 1, 20000, -30000);
		// Verify
		sut.perform(post("/create/driver").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/create :: createForm"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void  ドライバー詳細ページのリクエスト結果が正常となり検索サービスの結果を返す事() throws Exception {
		//SetUp
		when(driverService.findDriverInfo("koto@sample.com")).thenReturn(new Driver("koto@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG", "2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000));
		Driver driver = driverService.findDriverInfo("koto@sample.com");
		when(accountingService.commaOf1000(driver.getDailyWages())).thenReturn("20,000");
		String dailyWages = accountingService.commaOf1000(driver.getDailyWages());
		when(accountingService.commaOf1000(driver.getMonthlyExpenses())).thenReturn("30,000");
		String monthlyExpenses = accountingService.commaOf1000(driver.getMonthlyExpenses());
		//Verify
		sut.perform(get("/detail/driver/{id}", "koto@sample.com"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("driver", driver))
			.andExpect(model().attribute("dailyWages", dailyWages))
			.andExpect(model().attribute("monthlyExpenses", monthlyExpenses))
			.andExpect(model().attribute("contents", "driver/detail :: detail"))
			.andExpect(view().name("main/adminLayout"));

		verify(driverService, times(2)).findDriverInfo("koto@sample.com");
		verify(accountingService, times(2)).commaOf1000(driver.getDailyWages());
		verify(accountingService, times(2)).commaOf1000(driver.getMonthlyExpenses());
	}

	@Test
	@WithMockUser
	public void ドライバー情報を取得して更新画面を返す事() throws Exception {
		//SetUp
		when(driverService.findDriverInfo("koto@sample.com")).thenReturn(new Driver("koto@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG", "2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000));
		Driver driver = driverService.findDriverInfo("koto@sample.com");
		//Verify
		sut.perform(get("/update/driver/{id}", "koto@sample.com"))
			.andExpect(status().isOk())
			.andExpect(request().sessionAttribute("previousId", is("koto@sample.com")))
			.andExpect(model().attribute("driver", driver))
			.andExpect(model().attribute("contents", "driver/update :: update"))
			.andExpect(view().name("main/adminLayout"));

		verify(driverService, times(2)).findDriverInfo("koto@sample.com");
		assertThat(driver.getPassword(), is("password"));
		assertThat(driver.getPasswordConfirm(), is("password"));
	}

	@Test
	@WithMockUser
	public void 更新情報を受け取ってドライバー情報を更新しドライバー検索画面へリダイレクトする事() throws Exception {
		//SetUp
		Driver driver = new Driver("koto2@sample.com", "koto2@sample.com", "佐藤", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password", "2021-04-12 12:29:41", "USER", 1, 2, 20000, 30000);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("previousId", "koto@sample.com");
		//Verify
		sut.perform(post("/update/driver/{id}", "koto2@sample.com").flashAttr("driver", driver))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/search/driver"));

		verify(driverService, times(1)).updateDriver(driver);
	}

	@Test
	@WithMockUser
	public void 更新情報にエラーがあると例外情報が入った状態で画面が返る事() throws Exception {
		// SetUp
		Driver driver = new Driver("koto2@sample.com", "koto@sample.com", "佐藤", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "password", "password", "2021-04-12 12:29:41", "USER", 1, 2, 20000, 30000);
		// Verify
		sut.perform(post("/update/driver/{id}", "koto2@sample.com").flashAttr("driver", driver))
		.andExpect(status().isOk())
		.andExpect(model().hasErrors())
		.andExpect(model().attribute("driver", driver))
		.andExpect(model().attribute("contents", "driver/update :: update"))
		.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void ドライバー情報の削除前に更新画面が返る事() throws Exception {
		//SetUp
		when(driverService.findDriverInfo("koto@sample.com")).thenReturn(new Driver("koto@sample.com", "山本", "太郎", 2, "1", "1990-01-01", "08000000000", "1000004", "東京都千代田区大手町1-1-1", "2020-01-01", "$2a$10$zjHq1xmqY3/cgKSpK7dywOSYFsthdmUrBhOZ8YjzTyrJxSqfEBJYG", "2021-04-12 12:29:41", "USER", 1, 1, 20000, 30000));
		Driver driver = driverService.findDriverInfo("koto@sample.com");
		//Verify
		sut.perform(get("/delete/driver/{id}", "koto@sample.com"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("driver", driver))
			.andExpect(model().attribute("contents", "driver/delete :: delete"))
			.andExpect(view().name("main/adminLayout"));

		verify(driverService, times(2)).findDriverInfo("koto@sample.com");
	}

	@Test
	@WithMockUser
	public void 指定したIDのドライバー情報が削除されてドライバー検索画面にリダイレクトする事() throws Exception {
		//Verify
		sut.perform(post("/delete/driver/{id}", "koto@sample.com"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/search/driver"));

		verify(driverService, times(1)).deleteDriver("koto@sample.com");
	}

	@Test
	@WithMockUser
	public void パスワード初期化フォームのリクエスト結果が正常となりフォームが表示される事() throws Exception {
		//SetUp
		PassChangeConfirmForm form = new PassChangeConfirmForm();
		form.setId("koto@sample.com");
		//Veryfy
		sut.perform(get("/initialize/password/{id}", "koto@sample.com"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("passChangeConfirmForm", form))
			.andExpect(model().attribute("id", "koto@sample.com"))
			.andExpect(model().attribute("contents", "driver/password :: initialize"))
			.andExpect(view().name("main/adminLayout"));
	}

	@Test
	@WithMockUser
	public void フォームの結果を受け取ってパスワードを初期化してホーム画面へリダイレクトする事() throws Exception {
		//SetUp
		PassChangeConfirmForm passChangeConfirmForm =  new PassChangeConfirmForm("password", "password");
		String password = passChangeConfirmForm.getPassword();
		//Verify
		sut.perform(post("/initialize/password/{id}", "koto@sample.com").flashAttr("passChangeConfirmForm", passChangeConfirmForm))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/"));

		verify(driverService, times(1)).changePassword(passChangeConfirmForm);
		verify(passwordEncoder, times(1)).encode(password);
	}

}
