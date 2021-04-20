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
import com.masahiro.nakamoto.domain.Employee;
import com.masahiro.nakamoto.domain.form.EmployeeForm;
import com.masahiro.nakamoto.service.EmployeeService;

/**
 * 社員情報に関するコントローラー
 */
@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	HttpSession session;

	/**
	 * 社員検索画面
	 */
	@GetMapping("/admin/search/employee")
	public String getEmployeeForm(Model model, @ModelAttribute EmployeeForm employeeForm) {
		model.addAttribute("contents", "employee/form :: form");
		return "main/adminLayout";
	}

	/**
	 * 検索結果
	 */
	@PostMapping("/admin/search/employee/result")
	public String postSearchResult(Model model, @ModelAttribute EmployeeForm employeeForm) {
		//検索フォームが空欄の場合は全件検索
		if (employeeForm.getSearchWord() == null) {
			List<Employee> employeeList = employeeService.findAll();
			model.addAttribute("employeeList", employeeList);
		} else {
			List<Employee> employeeList = employeeService.findFromForm(employeeForm);
			model.addAttribute("employeeList", employeeList);
		}

		model.addAttribute("contents", "employee/result :: result");
		return "main/adminLayout";
	}

	/**
	 * 社員登録画面
	 */
	@GetMapping("/admin/create/employee")
	public String getCreateEmployee(Model model, @ModelAttribute Employee employee) {
		model.addAttribute("contents", "employee/create :: createForm");
		return "main/adminLayout";
	}

	/**
	 * 社員情報を登録
	 */
	@PostMapping("/admin/create/employee")
	public String postCreateEmployee(Model model, @ModelAttribute @Validated(GroupOrder.class) Employee employee, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			// パスワードをハッシュ化
			String password = employee.getPassword();
			password = passwordEncoder.encode(password);
			employee.setPassword(password);

			//社員情報を登録
			employeeService.createEmployee(employee);

			return "redirect:/admin/create/employee";
		} else {
			model.addAttribute("contents", "employee/create :: createForm");
			return "main/adminLayout";
		}
	}

	/**
	 * 社員の詳細情報を表示
	 */
	@GetMapping("/admin/detail/employee/{id}")
	public String getDetailEmployee(Model model, @PathVariable String id) {
		//社員情報を取得
		Employee employee = employeeService.findEmployee(id);
		model.addAttribute("employee", employee);

		model.addAttribute("contents", "employee/detail :: detail");
		return "main/adminLayout";
	}

	/**
	 * 社員情報の更新画面
	 */
	@GetMapping("/admin/update/employee/{id}")
	public String getUpdateEmployee(Model model, @PathVariable String id) {
		//社員情報を取得
		Employee employee = employeeService.findEmployee(id);
		model.addAttribute("employee", employee);

		//仮のパスワードをセット
		employee.setPassword("password");
		employee.setPasswordConfirm("password");

		//セッションに現在のIDをセット
		session.setAttribute("previousId", employee.getId());

		model.addAttribute("contents", "employee/update :: update");
		return "main/adminLayout";
	}

	/**
	 * 社員情報を更新
	 */
	@PostMapping("/admin/update/employee/{id}")
	public String postUpdateEmployee(Model model, @PathVariable String id, @ModelAttribute @Validated(GroupOrder.class) Employee employee, BindingResult bindiResult) {
		if (!bindiResult.hasErrors()) {
			//セッションから変更前のIDを取得して削除
			employee.setPreviousId((String) session.getAttribute("previousId"));
			session.removeAttribute("previousId");

			// パスワードをハッシュ化
			String password = employee.getPassword();
			password = passwordEncoder.encode(password);
			employee.setPassword(password);

			//社員情報を更新
			employeeService.updateEmployee(employee);

			return "redirect:/admin/search/employee";
		} else {
			model.addAttribute("contents", "employee/update :: update");
			return "main/adminLayout";
		}
	}

	/**
	 * 削除の確認画面
	 */
	@GetMapping("/admin/delete/employee/{id}")
	public String getDeleteConfirm(Model model, @PathVariable String id) {
		//社員情報を取得
		Employee employee = employeeService.findEmployee(id);
		model.addAttribute("employee", employee);

		model.addAttribute("contents", "employee/delete :: delete");
		return "main/adminLayout";
	}

	/**
	 * 社員情報を削除
	 */
	@PostMapping("/admin/delete/employee/{id}")
	public String postDeleteEmployee(Model model, @PathVariable String id) {
		employeeService.deleteEmployee(id);
		return "redirect:/admin/search/employee";
	}

}
