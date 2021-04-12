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

import com.masahiro.nakamoto.Valid.GroupOrder;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.DriverForm;
import com.masahiro.nakamoto.domain.PassChangeConfirmForm;
import com.masahiro.nakamoto.service.DriverService;

@Controller
public class DriverController {

	@Autowired
	DriverService driverService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	HttpSession session;

	/**
	 * ドライバー情報の全件取得
	 *
	 * @param model
	 * @return
	 */
//	@GetMapping("/search/driver")
//	public String getSearchDriver(Model model) {
//		List<Driver> driverList = driverService.findAll();
//		model.addAttribute("driverList", driverList);
//		model.addAttribute("contents", "driver/find :: findDriver");
//		return "main/adminLayout";
//	}

	/**
	 * ドライバーの検索フォーム表示
	 *
	 * @param model
	 * @param employeeForm
	 * @return
	 */
	@GetMapping("/search/driver")
	public String getDriverForm(Model model, @ModelAttribute DriverForm driverForm) {
		model.addAttribute("contents", "driver/form :: form");
		return "main/adminLayout";
	}

	/**
	 * フォームからの検索結果を表示
	 *
	 * @param model
	 * @param employeeForm
	 * @return
	 */
	@PostMapping("/search/driver/result")
	public String postSearchResult(Model model, @ModelAttribute DriverForm driverForm) {
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
	 *
	 * @param model
	 * @param driverForm
	 * @return
	 */
	@PostMapping("/search/driver/result/area")
	public String postSearchArea(Model model, @ModelAttribute DriverForm driverForm) {
		List<Driver> driverList = driverService.findAreaDriver(driverForm.getAreaId());
		model.addAttribute("driverList", driverList);
		model.addAttribute("contents", "driver/result :: result");
		return "main/adminLayout";
	}

	/**
	 * ドライバー登録フォームの表示
	 *
	 * @param model
	 * @param employee
	 * @return
	 */
	@GetMapping("/create/driver")
	public String getCreateDriver(Model model, @ModelAttribute Driver driver) {
		model.addAttribute("contents", "driver/create :: createForm");
		return "main/adminLayout";
	}

	/**
	 * ドライバー情報の登録
	 *
	 * @param model
	 * @param driver
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/create/driver")
	public String postCreateDriver(Model model, @ModelAttribute @Validated(GroupOrder.class) Driver driver, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			try {
				//指定したコース番号が不正なものでないかを確認
				driverService.isCorrectCourse(driver);
			} catch (Exception e) {
				model.addAttribute("illegalCourse", "この拠点には指定したコース番号が存在しません。");
				model.addAttribute("contents", "driver/create :: createForm");
				return "main/adminLayout";
			}
			try {
				//指定した拠点・コースにドライバーが登録されていないかを確認
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
			driverService.createDriver(driver);
			return "redirect:/create/driver";
		} else {
			model.addAttribute("contents", "driver/create :: createForm");
			return "main/adminLayout";
		}
	}

	/**
	 * ドライバーの詳細情報を表示
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/detail/driver/{id}")
	public String getDetailDriver(Model model, @PathVariable String id) {
		Driver driver = driverService.findDriverInfo(id);
		System.out.println(driver);
		model.addAttribute("driver", driver);
		model.addAttribute("contents", "driver/detail :: detail");
		return "main/adminLayout";
	}

	/**
	 * ドライバ情報の更新画面を表示
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/update/driver/{id}")
	public String getUpdateDriver(Model model, @PathVariable String id) {
		Driver driver = driverService.findDriverInfo(id);
		driver.setPassword("password");
		driver.setPasswordConfirm("password");
		session.setAttribute("previousId", driver.getId());
		model.addAttribute("driver", driver);
		model.addAttribute("contents", "driver/update :: update");
		return "main/adminLayout";
	}

	/**
	 * ドライバー情報を更新
	 *
	 * @param model
	 * @param id
	 * @param driver
	 * @return
	 */
	@PostMapping("/update/driver/{id}")
	public String postUpdateDriver(Model model, @PathVariable String id, @ModelAttribute @Validated(GroupOrder.class) Driver driver, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			driver.setPreviousId((String) session.getAttribute("previousId"));
			driverService.updateDriver(driver);
			session.removeAttribute("previousId");
			return "redirect:/search/driver";
		} else {
			model.addAttribute("contents", "driver/update :: update");
			return "main/adminLayout";
		}
	}

	/**
	 * 削除前の確認画面を表示
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/delete/driver/{id}")
	public String getDeleteConfirm(Model model, @PathVariable String id) {
		Driver driver = driverService.findDriverInfo(id);
		model.addAttribute("driver", driver);
		model.addAttribute("contents", "driver/delete :: delete");
		return "main/adminLayout";
	}

	/**
	 * ドライバー情報を削除
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@PostMapping("/delete/driver/{id}")
	public String postDeleteDriver(Model model, @PathVariable String id) {
		driverService.deleteDriver(id);
		return "redirect:/search/driver";
	}

	/**
	 * パスワードの初期化フォームを表示
	 *
	 * @param model
	 * @param id
	 * @param passChangeConfirmForm
	 * @return
	 */
	@GetMapping("/initialize/password/{id}")
	public String getInitializePassword(Model model, @PathVariable String id, @ModelAttribute PassChangeConfirmForm passChangeConfirmForm) {
		model.addAttribute("id", id);
		model.addAttribute("contents", "driver/password :: initialize");
		return "main/adminLayout";
	}

	/**
	 * パスワードの初期化を実行
	 *
	 * @param model
	 * @param id
	 * @param passChangeConfirmForm
	 * @param bindingResult
	 * @param driver
	 * @return
	 */
	@PostMapping("/initialize/password/{id}")
	public String postInitializePassword(Model model, @PathVariable String id, @ModelAttribute @Validated(GroupOrder.class) PassChangeConfirmForm passChangeConfirmForm, BindingResult bindingResult, @ModelAttribute Driver driver) {
		if (!bindingResult.hasErrors()) {
			passChangeConfirmForm.setId(id);
			// パスワードをハッシュ化
			String password = passChangeConfirmForm.getPassword();
			password = passwordEncoder.encode(password);
			passChangeConfirmForm.setPassword(password);
			driverService.changePassword(passChangeConfirmForm);
			return "redirect:/";
		} else {
			model.addAttribute("contents", "driver/password :: initialize");
			return "/main/homeLayout";
		}
	}

}
