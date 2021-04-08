package com.masahiro.nakamoto.controller.login;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.domain.shift.Today;
import com.masahiro.nakamoto.service.AreaService;
import com.masahiro.nakamoto.service.CourseService;
import com.masahiro.nakamoto.service.DateService;
import com.masahiro.nakamoto.service.DriverService;
import com.masahiro.nakamoto.service.HolidayService;
import com.masahiro.nakamoto.service.HomeService;
import com.masahiro.nakamoto.service.PositionService;
import com.masahiro.nakamoto.service.ShiftService;

@Controller
public class HomeController {

	@Autowired
	HomeService homeService;

	@Autowired
	ShiftService shiftService;

	@Autowired
	AreaService areaService;

	@Autowired
	CourseService courseService;

	@Autowired
	DateService dateService;

	@Autowired
	PositionService positionService;

	@Autowired
	DriverService driverService;

	@Autowired
	HolidayService holidayService;

	@Autowired
	ShiftForm shiftForm;

	@Autowired
	Course course;

	@Autowired
	Driver driver;

	/**
	 * ログイン後のホーム画面を表示
	 *
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping ("/")
	public String getHome(Model model, Principal principal) {
		//今日の日付を取得
		String today = dateService.getToday();
		model.addAttribute("today", today);
		//社員IDの取得
		Authentication auth = (Authentication)principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		String id = user.getUsername();
		//役職を取得
		int position = positionService.findPosition(id);

		if (position == 1) {
			model.addAttribute("contents", "home/admin_index :: index");
			return "main/homeLayout";
		} else {
			//日付と拠点をセット
			shiftForm.setDate(LocalDate.now());
			int areaId = areaService.findAreaId(id);
			shiftForm.setArea(areaId);
			//コース情報を取得
			course = shiftService.findCourseInfo(shiftForm);
			int courseId = courseService.findCourseId(id);
			int totalCourses = course.getTotalCourses();
			//今日の勤怠情報を取得
			Today todayShift = shiftService.findTodayShift(id);
			model.addAttribute("todayShift", todayShift);
			//名前を取得
			driver = driverService.findDriverInfo(id);
			model.addAttribute("driver", driver);
			//休み希望日の登録状況を確認
			int isSubmitHoliday = holidayService.isSubmitted(areaId, courseId);
			model.addAttribute("isSubmitHoliday", isSubmitHoliday);

			//代走ドライバーのコース設定
			if (courseId > totalCourses) {
				Map<Integer, Integer> substituteMap = homeService.findSubstituteShift(shiftForm, course);
				int todayCourse = substituteMap.get(todayShift.getCourseId());
				todayShift.setCourseId(todayCourse);
			}

			model.addAttribute("contents", "home/user_index :: index");
			return "main/homeLayout";
		}
	}

}
