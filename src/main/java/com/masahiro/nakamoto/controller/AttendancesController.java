package com.masahiro.nakamoto.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.masahiro.nakamoto.domain.Attendance;
import com.masahiro.nakamoto.domain.RegisterHolidayForm;
import com.masahiro.nakamoto.domain.SiteUser;
import com.masahiro.nakamoto.mybatis.AttendancesMapper;
import com.masahiro.nakamoto.service.AttendancesService;

@Controller
public class AttendancesController {

	@Autowired
	AttendancesService attendanceService;

	@Autowired
	AttendancesMapper attendancesMapper;

	@GetMapping("/attendances")
	public String getAttendances(@ModelAttribute RegisterHolidayForm registerHolidayForm) {
		return "attendances/attendances";
	}

	@PostMapping("/register/attendances")
	public String postAttendances(@ModelAttribute @Validated RegisterHolidayForm registerHolidayForm, BindingResult bindingResult, Principal principal) {
		if(!bindingResult.hasErrors()) {
			//社員IDの取得
			Authentication auth = (Authentication)principal;
			SiteUser user = (SiteUser)auth.getPrincipal();
			//その月の勤怠をすべてtrueで登録する
			attendanceService.registerAttendances(new Attendance(), user.getId());
			//休み希望日の勤怠をfalseに更新する
			attendanceService.registerHoliday(registerHolidayForm, new Attendance(), user.getId());
			return "attendances/attendances";
		} else {
			return "attendances/attendances";
		}
	}
}
