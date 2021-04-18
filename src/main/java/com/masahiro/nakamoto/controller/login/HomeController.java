package com.masahiro.nakamoto.controller.login;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.form.InfomationForm;
import com.masahiro.nakamoto.domain.form.PassChangeConfirmForm;
import com.masahiro.nakamoto.domain.form.PassChangeForm;
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
	PasswordEncoder passwordEncoder;

	@Autowired
	ShiftForm shiftForm;

	@Autowired
	Course course;

	@Autowired
	Driver driver;

	@Autowired
	HttpSession session;

	/**
	 * ログイン後のホーム画面を表示
	 *
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping ("/")
	public String getHome(Model model, Principal principal) {
		//社員IDの取得
		Authentication auth = (Authentication)principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		String id = user.getUsername();
		//役職を取得
		int position = positionService.findPosition(id);

		if (position == 1) {
			return "forward:/admin";
		} else {
			//今日の日付を取得
			String today = dateService.getToday();
			model.addAttribute("today", today);
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

	/**
	 * パスワードの変更画面を表示
	 *
	 * @param model
	 * @param passChangeForm
	 * @return
	 */
	@GetMapping("/user/change/password")
	public String getChangePassword(Model model, @ModelAttribute PassChangeForm passChangeForm) {
		model.addAttribute("contents", "home/password :: confirm");
		return "/main/homeLayout";
	}

	/**
	 * 新しいパスワードの入力画面を表示
	 *
	 * @param model
	 * @param passChangeForm
	 * @param bindingResult
	 * @param passChangeConfirmForm
	 * @param principal
	 * @return
	 */
	@PostMapping("/user/confirm/password")
	public String postComfirmPassword(Model model, @ModelAttribute @Validated(GroupOrder.class) PassChangeForm passChangeForm , BindingResult bindingResult, @ModelAttribute PassChangeConfirmForm passChangeConfirmForm, Principal principal) {
		if (!bindingResult.hasErrors()) {
			//社員IDの取得
			Authentication auth = (Authentication)principal;
			UserDetails user = (UserDetails) auth.getPrincipal();
			String id = user.getUsername();
			if (driverService.isCorrectPassword(passChangeForm.getPassword(), id)) {
				model.addAttribute("contents", "home/password :: change");
				return "/main/homeLayout";
			} else {
				model.addAttribute("contents", "home/password :: confirm");
				return "/main/homeLayout";
			}
		} else {
			model.addAttribute("contents", "home/password :: confirm");
			return "/main/homeLayout";
		}
	}

	/**
	 * パスワードの入力処理
	 *
	 * @param model
	 * @param principal
	 * @param passChangeConfirmForm
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/user/change/password")
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
			return "/main/homeLayout";
		}
	}

	/**
	 * 登録情報の更新画面を表示
	 *
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping("/user/update/information")
	public String getUpdateInfomation(Model model, Principal principal) {
		//社員IDの取得
		Authentication auth = (Authentication)principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		String id = user.getUsername();
		//社員情報の取得
		InfomationForm infomationForm = driverService.getInfomation(id);
		session.setAttribute("previousId", id);
		model.addAttribute("infomationForm", infomationForm);
		model.addAttribute("contents", "home/infomation :: infomation");
		return "/main/homeLayout";
	}

	/**
	 * 登録情報を更新
	 *
	 * @param model
	 * @param principal
	 * @param infomationForm
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/user/update/infomation")
	public String postUpdateInfomation(Model model, Principal principal, @ModelAttribute @Validated(GroupOrder.class) InfomationForm infomationForm, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			infomationForm.setPreviousId((String) session.getAttribute("previousId"));
			driverService.updateInfomation(infomationForm);
			session.removeAttribute("previousId");
			//return "redirect:/logout";
			return "/home/logout";
		} else {
			model.addAttribute("contents", "home/infomation :: infomation");
			return "/main/homeLayout";
		}
	}

}
