package com.masahiro.nakamoto.controller.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.masahiro.nakamoto.service.HolidayService;

@Controller
public class AdminController {

	@Autowired
	HolidayService holidayService;

	@GetMapping("/admin")
	public String getAdmin(Model model) {
		//休み希望を提出した人数を取得
		List<Integer> submittedNumber = holidayService.findSubmittedNumber();
		model.addAttribute("submittedNumber", submittedNumber);
		model.addAttribute("contents", "admin/admin :: admin");
		return "main/adminLayout";
	}

}
