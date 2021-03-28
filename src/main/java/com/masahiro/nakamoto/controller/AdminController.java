package com.masahiro.nakamoto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	@GetMapping("/admin")
	public String getAdmin(Model model) {
		model.addAttribute("contents", "admin/admin :: admin");
		return "main/adminLayout";
	}

}
