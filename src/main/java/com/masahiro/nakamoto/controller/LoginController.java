package com.masahiro.nakamoto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {


	/**
	 * ログイン画面
	 */
	@GetMapping("/login")
	public String getLogin() {
		return "login/login";
	}


	/**
	 * ログイン後の画面遷移
	 */
	@PostMapping("/login")
	public String postLogin() {
		return "redirect:/";
	}

}
