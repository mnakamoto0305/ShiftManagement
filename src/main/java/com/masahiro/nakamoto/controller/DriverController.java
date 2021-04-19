package com.masahiro.nakamoto.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.masahiro.nakamoto.Valid.group.GroupOrder;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.form.DriverForm;
import com.masahiro.nakamoto.domain.form.PassChangeConfirmForm;
import com.masahiro.nakamoto.service.AccountingService;
import com.masahiro.nakamoto.service.DriverService;

/**
 * ドライバー情報に関するコントローラー
 */
@Controller
public class DriverController {

	@Autowired
	DriverService driverService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	HttpSession session;

	@Autowired
	AccountingService accountingService;

	/**
	 * ドライバーの検索フォーム表示
	 */
	@GetMapping("/admin/search/driver")
	public String getDriverForm(Model model, @ModelAttribute DriverForm driverForm) {
		model.addAttribute("contents", "driver/form :: form");
		return "main/adminLayout";
	}

	/**
	 * フォームからの検索結果を表示
	 */
	@PostMapping("/admin/search/driver/result")
	public String postSearchResult(Model model, @ModelAttribute DriverForm driverForm) {

		//検索フォームが空欄の場合は全件検索
		if (driverForm.getSearchWord() == null) {
			List<Driver> driverList = driverService.findAll();
			model.addAttribute("driverList", driverList);
		} else {
			List<Driver> driverList = driverService.findFromForm(driverForm);
			model.addAttribute("driverList", driverList);
		}

		model.addAttribute("contents", "driver/result :: result");
		return "main/adminLayout";
	}

	/**
	 * 拠点検索の結果を表示
	 */
	@PostMapping("/admin/search/driver/result/area")
	public String postSearchArea(Model model, @ModelAttribute DriverForm driverForm) {
		//ドライバー情報を検索
		List<Driver> driverList = driverService.findAreaDriver(driverForm.getAreaId());
		model.addAttribute("driverList", driverList);

		model.addAttribute("contents", "driver/result :: result");
		return "main/adminLayout";
	}

	/**
	 * ドライバー登録フォームの表示
	 */
	@GetMapping("/admin/create/driver")
	public String getCreateDriver(Model model, @ModelAttribute Driver driver) {
		model.addAttribute("contents", "driver/create :: createForm");
		return "main/adminLayout";
	}

	/**
	 * ドライバー情報の登録
	 */
	@PostMapping("/admin/create/driver")
	public String postCreateDriver(Model model, @ModelAttribute @Validated(GroupOrder.class) Driver driver, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			//コース番号に不備があった場合はエラーメッセージを表示
			try {
				driverService.isCorrectCourse(driver);
			} catch (Exception e) {
				model.addAttribute("illegalCourse", "この拠点には指定したコース番号が存在しません。");
				model.addAttribute("contents", "driver/create :: createForm");
				return "main/adminLayout";
			}

			//既にドライバーが登録されている場合はエラーメッセージを表示
			try {
				driverService.isRegistered(driver);
			} catch (Exception e) {
				model.addAttribute("registered", "指定したコースは既に他のドライバーが登録されています。");
				model.addAttribute("contents", "driver/create :: createForm");
				return "main/adminLayout";
			}

			// パスワードをハッシュ化
			String password = driver.getPassword();
			password = passwordEncoder.encode(password);
			driver.setPassword(password);

			//ドライバー情報を登録
			driverService.createDriver(driver);

			return "redirect:/create/driver";
		} else {
			model.addAttribute("contents", "driver/create :: createForm");
			return "main/adminLayout";
		}
	}

	/**
	 * ドライバーの詳細情報を表示
	 */
	@GetMapping("/admin/detail/driver/{id}")
	public String getDetailDriver(Model model, @PathVariable String id) {
		//ドライバー情報を取得
		Driver driver = driverService.findDriverInfo(id);
		model.addAttribute("driver", driver);

		//単価と経費をカンマ区切りに変換
		String dailyWages = accountingService.commaOf1000(driver.getDailyWages());
		model.addAttribute("dailyWages", dailyWages);
		String monthlyExpenses = accountingService.commaOf1000(driver.getMonthlyExpenses());
		model.addAttribute("monthlyExpenses", monthlyExpenses);

		model.addAttribute("contents", "driver/detail :: detail");
		return "main/adminLayout";
	}

	/**
	 * ドライバ情報の更新画面を表示
	 */
	@GetMapping("/admin/update/driver/{id}")
	public String getUpdateDriver(Model model, @PathVariable String id) {
		//現在のドライバー情報を取得
		Driver driver = driverService.findDriverInfo(id);
		model.addAttribute("driver", driver);

		//仮のパスワードを設定
		driver.setPassword("password");
		driver.setPasswordConfirm("password");

		//セッションに現在のIDを登録
		session.setAttribute("previousId", driver.getId());

		model.addAttribute("contents", "driver/update :: update");
		return "main/adminLayout";
	}

	/**
	 * ドライバー情報を更新
	 */
	@PostMapping("/admin/update/driver/{id}")
	public String postUpdateDriver(Model model, @PathVariable String id, @ModelAttribute @Validated(GroupOrder.class) Driver driver, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			//セッションから変更前のIDを取得して削除
			driver.setPreviousId((String) session.getAttribute("previousId"));
			session.removeAttribute("previousId");

			//ドライバー情報を更新
			driverService.updateDriver(driver);

			return "redirect:/search/driver";
		} else {
			model.addAttribute("contents", "driver/update :: update");
			return "main/adminLayout";
		}
	}

	/**
	 * 削除前の確認画面
	 */
	@GetMapping("/admin/delete/driver/{id}")
	public String getDeleteConfirm(Model model, @PathVariable String id) {
		//ドライバー情報を取得
		Driver driver = driverService.findDriverInfo(id);
		model.addAttribute("driver", driver);

		model.addAttribute("contents", "driver/delete :: delete");
		return "main/adminLayout";
	}

	/**
	 * ドライバー情報を削除
	 */
	@PostMapping("/admin/delete/driver/{id}")
	public String postDeleteDriver(Model model, @PathVariable String id) {
		driverService.deleteDriver(id);
		return "redirect:/search/driver";
	}

	/**
	 * パスワードの初期化フォーム
	 */
	@GetMapping("/admin/initialize/password/{id}")
	public String getInitializePassword(Model model, @PathVariable String id, @ModelAttribute PassChangeConfirmForm passChangeConfirmForm) {
		model.addAttribute("id", id);
		model.addAttribute("contents", "driver/password :: initialize");
		return "main/adminLayout";
	}

	/**
	 * パスワードの初期化を実行
	 */
	@PostMapping("/admin/initialize/password/{id}")
	public String postInitializePassword(Model model, @PathVariable String id, @ModelAttribute @Validated(GroupOrder.class) PassChangeConfirmForm passChangeConfirmForm, BindingResult bindingResult,
			@ModelAttribute Driver driver) {
		if (!bindingResult.hasErrors()) {
			//IDをセット
			passChangeConfirmForm.setId(id);

			// パスワードをハッシュ化
			String password = passChangeConfirmForm.getPassword();
			password = passwordEncoder.encode(password);
			passChangeConfirmForm.setPassword(password);

			//パスワードを初期化
			driverService.changePassword(passChangeConfirmForm);
			return "redirect:/";
		} else {
			model.addAttribute("contents", "driver/password :: initialize");
			return "/main/homeLayout";
		}
	}

}
