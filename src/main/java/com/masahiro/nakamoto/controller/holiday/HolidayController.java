package com.masahiro.nakamoto.controller.holiday;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.holiday.Attendance;
import com.masahiro.nakamoto.domain.holiday.RegisterHolidayForm;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.domain.shift.ShiftResult;
import com.masahiro.nakamoto.mybatis.HolidayMapper;
import com.masahiro.nakamoto.service.AreaService;
import com.masahiro.nakamoto.service.DriverService;
import com.masahiro.nakamoto.service.HolidayService;
import com.masahiro.nakamoto.service.PositionService;

/**
 * 休み希望の登録や更新に関するコントローラー
 */
@Controller
public class HolidayController {

	@Autowired
	HolidayService holidayService;

	@Autowired
	PositionService positionService;

	@Autowired
	HolidayMapper holidayMapper;

	@Autowired
	DriverService driverService;

	@Autowired
	AreaService areaService;

	@Autowired
	ShiftForm shiftForm;

	@Autowired
	HttpSession session;

	/**
	 * 休み希望の登録画面
	 */
	@GetMapping("/user/attendances")
	public String getAttendances(Model model, @ModelAttribute RegisterHolidayForm registerHolidayForm) {
		//現在の日時を取得
		LocalDate date = LocalDate.now();
		//今月の20日の日付を取得
		LocalDate twenty = date.withDayOfMonth(20);

		//日付が毎月20日以降であればホーム画面へリダイレクト
		if (date.isAfter(twenty)) {
			return "redirect:/";
		} else {
			return "attendances/attendances";
		}
	}

	/**
	 * 取得した日付を休み希望日として登録
	 */
	@PostMapping("/user/register/attendances")
	public String postAttendances(Model model, @ModelAttribute @Validated RegisterHolidayForm registerHolidayForm, BindingResult bindingResult, Principal principal) {
		//社員IDの取得
		Authentication auth = (Authentication) principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		String id = user.getUsername();
		shiftForm.setId(id);

		//休み希望日が既に登録されていないか確認
		ShiftResult shiftResult = holidayService.findHoliday(shiftForm);
		List<Attendance> attendanceList = shiftResult.getAttendanceList();

		//休み希望日が既に登録されている場合はエラーメッセージを表示
		if (attendanceList.size() == 0) {
			if (!bindingResult.hasErrors()) {
				//その月の勤怠をすべてtrueで登録する
				holidayService.registerAttendances(new Attendance(), user.getUsername());
				//休み希望日の勤怠をfalseに更新する
				holidayService.registerHoliday(registerHolidayForm, new Attendance(), user.getUsername());
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
	 * 登録した休み希望日を表示する画面
	 */
	@GetMapping("/user/confirm/holiday")
	public String getConfirmHoliday(Model model, @ModelAttribute ShiftForm shiftForm, Principal principal) {
		//社員IDのセット
		Authentication auth = (Authentication) principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		shiftForm.setId(user.getUsername());

		//登録してある休み希望日を取得
		ShiftResult shiftResult = holidayService.findHoliday(shiftForm);
		List<Attendance> attendanceList = shiftResult.getAttendanceList();
		model.addAttribute("shiftResult", shiftResult);

		//来月の日付を取得
		LocalDate nextMonth = LocalDate.now().plusMonths(1);
		int year = nextMonth.getYear();
		int month = nextMonth.getMonthValue();
		model.addAttribute("year", year);
		model.addAttribute("month", month);

		//休み希望日がまだ登録されていない場合はエラーメッセージを表示
		if (attendanceList.size() == 0) {
			model.addAttribute("message", "来月の休み希望日はまだ登録されていません。");
			model.addAttribute("contents", "attendances/confirmHolidayError :: confirm");
			return "main/homeLayout";
		} else {
			model.addAttribute("contents", "attendances/confirmHoliday :: confirm");
			return "main/homeLayout";
		}

	}

	/**
	 * 休み希望日の修正画面
	 */
	@GetMapping("/user/fix/holiday")
	public String getUpdateHoliday(Model model, @ModelAttribute RegisterHolidayForm registerHolidayForm) {
		//今日の日付を取得
		LocalDate date = LocalDate.now();
		//今月の20日の日付を取得
		LocalDate twenty = date.withDayOfMonth(20);

		//日付が毎月20日以降であればホーム画面へリダイレクト
		if (date.isAfter(twenty)) {
			return "redirect:/";
		} else {
			return "attendances/updateHoliday";
		}
	}

	/**
	 * 休み希望日を修正
	 */
	@PostMapping("/user/fix/holiday")
	public String postUpdateHoliday(@ModelAttribute @Validated RegisterHolidayForm registerHolidayForm, BindingResult bindingResult, Principal principal) {
		if (!bindingResult.hasErrors()) {
			//社員IDの取得
			Authentication auth = (Authentication) principal;
			UserDetails user = (UserDetails) auth.getPrincipal();
			//すべての勤怠をTrueに更新する
			holidayService.makeTrue(new Attendance(), user.getUsername());
			//休み希望日の勤怠をfalseに更新する
			holidayService.registerHoliday(registerHolidayForm, new Attendance(), user.getUsername());
			return "attendances/attendances";
		} else {
			return "attendances/attendances";
		}
	}

	/**
	 * 各拠点のドライバーの休み希望提出状況を表示
	 */
	@GetMapping("/admin/submitted/detail/{areaId}")
	public String getIsSubmitted(Model model, @PathVariable int areaId) {
		//拠点名を取得
		String areaName = areaService.findAreaName(areaId).getName();
		model.addAttribute("areaName", areaName);
		model.addAttribute("areaId", areaId);

		//コース数を取得
		int totalCourses = areaService.findTotalCourses(areaId);
		model.addAttribute("totalCourses", totalCourses);

		//休み希望の提出有無を取得
		List<Integer> submittedList = holidayService.isSubmitted(areaId);
		model.addAttribute("submittedList", submittedList);

		//ドライバー情報を取得
		List<Driver> driverList = driverService.findAreaDriver(areaId);
		model.addAttribute("driverList", driverList);

		model.addAttribute("contents", "attendances/isSubmitted :: isSubmitted");
		return "main/adminLayout";
	}

	/**
	 * 管理者が休み希望を代理登録するカレンダーを表示
	 */
	@GetMapping("/admin/register/holiday/{areaId}/{courseId}")
	public String getProxyRegister(@PathVariable int areaId, @PathVariable int courseId, @ModelAttribute RegisterHolidayForm registerHolidayForm) {
		//拠点・コースIDをセッションに登録
		session.setAttribute("areaId", areaId);
		session.setAttribute("courseId", courseId);

		return "attendances/proxyRegister";
	}

	/**
	 * 管理者が休み希望を代理登録
	 */
	@PostMapping("/admin/register/proxy_holiday")
	public String postProxyRegister(@ModelAttribute RegisterHolidayForm registerHolidayForm) {
		//拠点・コースIDを取得
		int areaId = (int) session.getAttribute("areaId");
		int courseId = (int) session.getAttribute("courseId");

		//休み希望日を代理登録
		holidayService.proxyRegister(areaId, courseId, registerHolidayForm);

		//セッションから拠点・コースID
		session.removeAttribute("areaId");
		session.removeAttribute("courseId");

		return "redirect:/admin";
	}

}
