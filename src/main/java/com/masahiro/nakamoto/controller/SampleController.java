package com.masahiro.nakamoto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

	@GetMapping("/sample")
	public String getSample() {
		return "sample/sample";
	}
}
