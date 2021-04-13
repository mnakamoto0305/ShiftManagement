package com.masahiro.nakamoto.controller.login;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.masahiro.nakamoto.Valid.group.GroupOrder;
import com.masahiro.nakamoto.domain.form.PassChangeConfirmForm;
import com.masahiro.nakamoto.domain.form.PassChangeForm;
import com.masahiro.nakamoto.service.DriverService;
import com.masahiro.nakamoto.service.HolidayService;

@Controller
public class AdminController {

	@Autowired
	HolidayService holidayService;

	@Autowired
	DriverService driverService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/admin")
	public String getAdmin(Model model) {
		//休み希望を提出した人数を取得
		List<Integer> submittedNumber = holidayService.findSubmittedNumber();
		model.addAttribute("submittedNumber", submittedNumber);
		model.addAttribute("contents", "admin/admin :: admin");
		return "main/adminLayout";
	}

	@GetMapping("/admin/change/password")
	public String getChangePassword(Model model, @ModelAttribute PassChangeForm passChangeForm) {
		model.addAttribute("contents", "home/password :: confirm");
		return "/main/adminLayout";
	}

	@PostMapping("/admin/confirm/password")
	public String postComfirmPassword(Model model, @ModelAttribute @Validated(GroupOrder.class) PassChangeForm passChangeForm , BindingResult bindingResult, @ModelAttribute PassChangeConfirmForm passChangeConfirmForm, Principal principal) {
		if (!bindingResult.hasErrors()) {
			//社員IDの取得
			Authentication auth = (Authentication)principal;
			UserDetails user = (UserDetails) auth.getPrincipal();
			String id = user.getUsername();
			if (driverService.isCorrectPassword(passChangeForm.getPassword(), id)) {
				model.addAttribute("contents", "home/password :: change");
				return "/main/adminLayout";
			} else {
				model.addAttribute("contents", "home/password :: confirm");
				return "/main/adminLayout";
			}
		} else {
			model.addAttribute("contents", "home/password :: confirm");
			return "/main/adminLayout";
		}
	}

	@PostMapping("/admin/change/password")
	public String postChangePassword(Model model, Principal principal, @ModelAttribute @Validated(GroupOrder.class) PassChangeConfirmForm passChangeConfirmForm , BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			//社員IDの取得
			Authentication auth = (Authentication)principal;
			UserDetails user = (UserDetails) auth.getPrincipal();
			passChangeConfirmForm.setId(user.getUsername());
			// パスワードをハッシュ化
			String password = passChangeConfirmForm.getPassword();
			password = passwordEncoder.encode(password);
			passChangeConfirmForm.setPassword(password);
			driverService.changePassword(passChangeConfirmForm);
			return "redirect:/";
		} else {
			model.addAttribute("contents", "home/password :: change");
			return "/main/adminLayout";
		}

	}

}
