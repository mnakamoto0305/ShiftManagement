package com.masahiro.nakamoto.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping ("/")
	public String getHome(Model model) {
		model.addAttribute("contents", "home/index :: index");
		return "main/homeLayout";
	}

}
