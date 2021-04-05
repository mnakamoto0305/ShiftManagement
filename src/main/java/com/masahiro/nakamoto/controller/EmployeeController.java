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
import com.masahiro.nakamoto.domain.Employee;
import com.masahiro.nakamoto.service.EmployeeService;

@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
    PasswordEncoder passwordEncoder;

	@Autowired
	HttpSession session;

	@GetMapping("/search/employee")
	public String getAllEmployee(Model model) {
		List<Employee> employeeList = employeeService.findAll();
		System.out.println(employeeList);
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("contents", "employee/find :: findEmployee");
		return "main/adminLayout";
	}

	/**
	 * 社員登録フォーム
	 *
	 * @param model
	 * @param employee
	 * @return
	 */
	@GetMapping("/create/employee")
	public String getCreateEmployee(Model model, @ModelAttribute Employee employee) {
		model.addAttribute("contents", "employee/createForm :: createForm");
		return "main/adminLayout";
	}

	/**
	 * 社員登録
	 *
	 * @param model
	 * @param employee
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/create/employee")
	public String postCreateEmployee(Model model, @ModelAttribute @Validated(GroupOrder.class) Employee employee, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			// パスワードをハッシュ化
			String password = employee.getPassword();
			password = passwordEncoder.encode(password);
			employee.setPassword(password);
			employeeService.createEmployee(employee);
		}
		model.addAttribute("contents", "employee/createForm :: createForm");
		return "main/adminLayout";
	}

	/**
	 * 社員の詳細情報を表示
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/detail/{id}")
	public String getDetailEmployee(Model model, @PathVariable String id) {
		Employee employee = employeeService.findEmployee(id);
		model.addAttribute("employee", employee);
		model.addAttribute("contents", "employee/detail :: detail");
		return "main/adminLayout";
	}

	/**
	 * 社員情報の更新画面を表示
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/update/{id}")
	public String getUpdateEmployee(Model model, @PathVariable String id) {
		Employee employee = employeeService.findEmployee(id);
		session.setAttribute("previousId", employee.getId());
		model.addAttribute("employee", employee);
		model.addAttribute("contents", "employee/update :: update");
		return "main/adminLayout";
	}

	/**
	 * 社員情報を更新
	 *
	 * @param model
	 * @param id
	 * @param employee
	 * @return
	 */
	@PostMapping("/update/{id}")
	public String postUpdateEmployee(Model model, @PathVariable String id, @ModelAttribute Employee employee) {
		employee.setPreviousId((String) session.getAttribute("previousId"));
		employeeService.updateEmployee(employee);
		session.removeAttribute("previousId");
		return "redirect:/search/employee";
	}

	@GetMapping("/delete/{id}")
	public String getDeleteConfirm(Model model, @PathVariable String id) {
		Employee employee = employeeService.findEmployee(id);
		model.addAttribute("employee", employee);
		model.addAttribute("contents", "employee/delete :: delete");
		return "main/adminLayout";
	}

	@PostMapping("/delete/{id}")
	public String postDeleteConfirm(Model model, @PathVariable String id) {
		employeeService.deleteEmployee(id);
		return "redirect:/search/employee";
	}

}
