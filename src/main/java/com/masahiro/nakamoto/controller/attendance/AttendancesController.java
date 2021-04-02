package com.masahiro.nakamoto.controller.attendance;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.masahiro.nakamoto.domain.attendance.Attendance;
import com.masahiro.nakamoto.domain.attendance.RegisterHolidayForm;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.domain.shift.ShiftResult;
import com.masahiro.nakamoto.mybatis.AttendancesMapper;
import com.masahiro.nakamoto.service.AttendancesService;

@Controller
public class AttendancesController {

	@Autowired
	AttendancesService attendancesService;

	@Autowired
	AttendancesMapper attendancesMapper;

	@Autowired
	ShiftForm shiftForm;

	/**
	 * 休み希望の登録フォームを表示
	 *
	 * @param registerHolidayForm
	 * @return
	 */
	@GetMapping("/attendances")
	public String getAttendances(Model model, @ModelAttribute RegisterHolidayForm registerHolidayForm) {
		LocalDate date = LocalDate.now();
		LocalDate twenty = date.withDayOfMonth(20);
		if (date.isAfter(twenty)) {
			return "redirect:/";
		} else {
			return "attendances/attendances";
		}
	}

	/**
	 * 取得した日付を休み希望日として登録
	 *
	 * @param registerHolidayForm
	 * @param bindingResult
	 * @param principal
	 * @return
	 */
	@PostMapping("/register/attendances")
	public String postAttendances(Model model, @ModelAttribute @Validated RegisterHolidayForm registerHolidayForm, BindingResult bindingResult, Principal principal) {
		//社員IDの取得
		Authentication auth = (Authentication)principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		shiftForm.setId(user.getUsername());

		ShiftResult shiftResult = attendancesService.findHoliday(shiftForm);
		List<Attendance> attendanceList = shiftResult.getAttendanceList();

		if (attendanceList.size() == 0) {
			if(!bindingResult.hasErrors()) {
				//その月の勤怠をすべてtrueで登録する
				attendancesService.registerAttendances(new Attendance(), user.getUsername());
				//休み希望日の勤怠をfalseに更新する
				attendancesService.registerHoliday(registerHolidayForm, new Attendance(), user.getUsername());
				return "attendances/attendances";
			} else {
				return "attendances/attendances";
			}
		} else {
			model.addAttribute("message", "来月の休み希望日は既に登録されています。");
			return "attendances/attendances";
		}

	}

	/**
	 * 登録した休み希望日を表示
	 *
	 * @param model
	 * @param shiftForm
	 * @param principal
	 * @return
	 */
	@GetMapping("/confirm/holiday")
	public String getConfirmHoliday(Model model, @ModelAttribute ShiftForm shiftForm, Principal principal) {
		//社員IDのセット
		Authentication auth = (Authentication)principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		shiftForm.setId(user.getUsername());

		ShiftResult shiftResult = attendancesService.findHoliday(shiftForm);
		List<Attendance> attendanceList = shiftResult.getAttendanceList();

		LocalDate nextMonth = LocalDate.now().plusMonths(1);
		int year = nextMonth.getYear();
		int month = nextMonth.getMonthValue();
		model.addAttribute("shiftResult", shiftResult);
		model.addAttribute("year", year);
		model.addAttribute("month", month);

		if (attendanceList.size() == 0) {
			model.addAttribute("message", "来月の休み希望日はまだ登録されていません。");
			model.addAttribute("contents", "attendances/confirmHolidayError :: confirm");
			return "main/homeLayout";
		} else {
			model.addAttribute("contents", "attendances/confirmHoliday :: confirm");
			return "main/homeLayout";
		}

	}

	@GetMapping("/fix/holiday")
	public String getUpdateHoliday(Model model, @ModelAttribute RegisterHolidayForm registerHolidayForm) {
		LocalDate date = LocalDate.now();
		LocalDate twenty = date.withDayOfMonth(20);
		if (date.isAfter(twenty)) {
			return "redirect:/";
		} else {
			return "attendances/updateHoliday";
		}
	}

	@PostMapping("/fix/holiday")
	public String postUpdateHoliday(@ModelAttribute @Validated RegisterHolidayForm registerHolidayForm, BindingResult bindingResult, Principal principal) {
		if(!bindingResult.hasErrors()) {
			//社員IDの取得
			Authentication auth = (Authentication)principal;
			UserDetails user = (UserDetails) auth.getPrincipal();
			//すべての勤怠をTrueに更新する
			attendancesService.makeTrue(new Attendance(), user.getUsername());
			//休み希望日の勤怠をfalseに更新する
			attendancesService.registerHoliday(registerHolidayForm, new Attendance(), user.getUsername());
			return "attendances/attendances";
		} else {
			return "attendances/attendances";
		}
	}
}
