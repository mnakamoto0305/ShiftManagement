package com.masahiro.nakamoto.controller.shift;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.masahiro.nakamoto.domain.Area;
import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.attendance.Attendance;
import com.masahiro.nakamoto.domain.attendance.MultiAttendances;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.domain.shift.ShiftResult;
import com.masahiro.nakamoto.service.AreaService;
import com.masahiro.nakamoto.service.CourseService;
import com.masahiro.nakamoto.service.DateService;
import com.masahiro.nakamoto.service.HomeService;
import com.masahiro.nakamoto.service.PositionService;
import com.masahiro.nakamoto.service.ShiftService;
import com.masahiro.nakamoto.service.SubstituteService;

@Controller
public class ShiftController {

	@Autowired
	ShiftService shiftService;

	@Autowired
	AreaService areaService;

	@Autowired
	SubstituteService substituteService;

	@Autowired
	DateService dateService;

	@Autowired
	CourseService courseService;

	@Autowired
	HomeService homeService;

	@Autowired
	PositionService positionService;

	@Autowired
	Attendance attendance;

	@Autowired
	Course course;

	@Autowired
	Area area;

	@GetMapping("/shift_search")
	public String getShiftForm(Model model, @ModelAttribute ShiftForm shiftForm) {
		model.addAttribute("contents", "shift/shiftSearchIndex :: shift_search");
		return "main/homeLayout";
	}

	/**
	 * シフトを検索するための条件を指定するフォームを表示(管理画面用)
	 *
	 * @param model
	 * @param shiftForm
	 * @return
	 */
	@GetMapping("/admin/shift_search")
	public String getShiftSearch(Model model, @ModelAttribute ShiftForm shiftForm) {
		model.addAttribute("contents", "shift/shiftSearchIndex :: shift_index");
		return "main/adminLayout";
	}


	/**
	 * 作成するシフトを検索するフォームを表示
	 *
	 * @param model
	 * @param shiftForm
	 * @return
	 */
	@GetMapping("/admin/shift_make")
	public String getShiftMake(Model model, @ModelAttribute ShiftForm shiftForm) {
		model.addAttribute("contents", "shift/shiftMakeIndex :: shift_index");
		return "main/adminLayout";
	}


	/**
	 * 確認するシフトを表示(管理者画面用)
	 *
	 * @param model
	 * @param multiAttendances
	 * @param shiftForm
	 * @return
	 */
	@PostMapping("/admin/shift_result")
	public String postShiftResultAdmin(Model model, @ModelAttribute ShiftForm shiftForm, BindingResult bindingResult) {
		try {
			//コース情報をセット
			course = shiftService.findCourseInfo(shiftForm);
			int totalCourses = course.getTotalCourses();
			model.addAttribute("course", course);
			model.addAttribute("totalCourses", totalCourses);
			//勤怠情報をセット
			MultiAttendances courseAttendances = new MultiAttendances(shiftService.findCourseAttendances(shiftForm, course));
			List<List<Integer>> substituteList = substituteService.findSubstituteShift(shiftForm, course);
			model.addAttribute("courseAttendances", courseAttendances);
			model.addAttribute("substituteList", substituteList);
			//拠点名をセット
			area = areaService.findAreaName(shiftForm.getArea());
			model.addAttribute("area", area);
			//年月をセット
			String year = shiftForm.getYear();
			String month = shiftForm.getMonth();
			model.addAttribute("year", year);
			model.addAttribute("month", month);
			//各ドライバーの出勤数をセット
			List<Integer> totalAttendance = shiftService.findTotalAttendance(shiftForm);
			model.addAttribute("totalAttendance", totalAttendance);
			//ドライバーの名前をセット
			List<Driver> driverName = shiftService.findDriverName(shiftForm);
			model.addAttribute("driverName", driverName);
			//表示するシフトの月と今日の月をセット
			int shiftMonth = shiftForm.getDate().getMonthValue();
			model.addAttribute("shiftMonth", shiftMonth);
			int thisMonth = LocalDate.now().getMonthValue();
			model.addAttribute("thisMonth", thisMonth);
		} catch (IndexOutOfBoundsException e) {
			model.addAttribute("message1", "指定した条件に合致するシフトは見つかりませんでした。");
			model.addAttribute("message2", "来月のシフトが表示されない場合は、作成したシフトに不備がある可能性があります。");
			model.addAttribute("contents", "shift/shiftSearchIndex :: shift_index");
			return "main/adminLayout";
		}

		model.addAttribute("contents", "shift/result :: result");
		return "main/adminLayout";
	}

	/**
	 * ログインしているドライバーのシフトを表示
	 *
	 * @param model
	 * @param shiftForm
	 * @param principal
	 * @return
	 */
	@PostMapping("/shift_result")
	public String postShiftResult(Model model, @ModelAttribute ShiftForm shiftForm, Principal principal) {
		//社員IDをセット
		Authentication auth = (Authentication)principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		String id = user.getUsername();
		shiftForm.setId(id);
		//役職を取得
		int position = positionService.findPosition(id);
		//年月をセット
		String year = shiftForm.getYear();
		model.addAttribute("year", year);
		String month = shiftForm.getMonth();
		model.addAttribute("month", month);

		if (position == 1) {
			model.addAttribute("message", "確認できるシフトはありません。");
			model.addAttribute("contents", "shift/result :: resultForNormalDriver");
		} else {
			//エリア情報を取得
			int areaId = areaService.findAreaId(id);
			shiftForm.setArea(areaId);
			//コース情報を取得
			course = shiftService.findCourseInfo(shiftForm);
			int courseId = courseService.findCourseId(id);
			int totalCourses = course.getTotalCourses();
			//勤怠情報をセット
			ShiftResult shiftResult = shiftService.findShift(shiftForm);
			model.addAttribute("shiftResult", shiftResult);

			//代走ドライバーの場合は走るコース番号をセット
			if (courseId > totalCourses) {
				List<Map<Integer, Integer>> listMap = homeService.findSubstituteShiftMonth(shiftForm, course);
				List<Integer> list = homeService.findCourse(listMap, courseId);
				model.addAttribute("list", list);
				model.addAttribute("contents", "shift/result :: resultForSubstituteDriver");
			} else {
				model.addAttribute("contents", "shift/result :: resultForNormalDriver");
			}
		}

		return "main/homeLayout";
	}

	/**
	 * 作成するシフトを表示
	 *
	 * @param model
	 * @param multiAttendances
	 * @param shiftForm
	 * @return
	 */
	@PostMapping("/admin/shift_make")
	public String postShiftMake(Model model, @ModelAttribute MultiAttendances multiAttendances, @ModelAttribute ShiftForm shiftForm) {
		try {
			//コース情報をセット
			course = shiftService.findCourseInfo(shiftForm);
			int totalCourses = course.getTotalCourses();
			model.addAttribute("course", course);
			model.addAttribute("totalCourses", totalCourses);
			//勤怠情報をセット
			multiAttendances.setMultiAttendances(shiftService.makeMultiAttendances(shiftForm));
			//拠点名をセット
			area = areaService.findAreaName(shiftForm.getArea());
			model.addAttribute("area", area);
			//表示するシフトの年月をセット
			YearMonth date = YearMonth.now().plusMonths(1);
			model.addAttribute("date", date);
			//各ドライバーの出勤数をセット
			List<Integer> totalAttendance = shiftService.findTotal(shiftForm);
			model.addAttribute("totalAttendance", totalAttendance);
			//その月の日数をセット
			int monthNum = dateService.getNextMonthNum();
			model.addAttribute("monthNum", monthNum);
			//担当ドライバーの名前をセット
			List<Driver> driverName = shiftService.findDriverName(shiftForm);
			model.addAttribute("driverName", driverName);
			model.addAttribute("contents", "shift/makeMultiShift :: shift_make");
			return "main/adminLayout";
		} catch (Exception e) {
			model.addAttribute("message", "指定した拠点はまだ休み希望が出揃っていません。");
			model.addAttribute("contents", "shift/shiftMakeIndex :: shift_index");
			return "main/adminLayout";
		}

	}

	/**
	 * 修正する今月のシフトを表示
	 *
	 * @param model
	 * @param multiAttendances
	 * @param shiftForm
	 * @return
	 */
	@PostMapping("/admin/shift_modify")
	public String postShiftModify(Model model, @ModelAttribute MultiAttendances multiAttendances, @ModelAttribute ShiftForm shiftForm) {
		//コース情報をセット
		course = shiftService.findCourseInfo(shiftForm);
		int totalCourses = course.getTotalCourses();
		model.addAttribute("course", course);
		model.addAttribute("totalCourses", totalCourses);
		//勤怠情報をセット
		multiAttendances.setMultiAttendances(shiftService.findMultiAttendances(shiftForm));
		//拠点名をセット
		area = areaService.findAreaName(shiftForm.getArea());
		model.addAttribute("area", area);
		//表示するシフトの年月をセット
		YearMonth date = YearMonth.now();
		model.addAttribute("date", date);
		//その月の日数をセット
		int monthNum = dateService.getThisMonthNum();
		model.addAttribute("monthNum", monthNum);
		//各ドライバーの出勤数をセット
		List<Integer> totalAttendance = shiftService.findTotalAttendance(shiftForm);
		model.addAttribute("totalAttendance", totalAttendance);
		//担当ドライバーの名前をセット
		List<Driver> driverName = shiftService.findDriverName(shiftForm);
		model.addAttribute("driverName", driverName);

		model.addAttribute("contents", "shift/makeMultiShift :: shift_modify");
		return "main/adminLayout";
	}


	/**
	 * 今月のシフトを修正
	 *
	 * @param model
	 * @param multiAttendances
	 * @return
	 */
	@PostMapping("/admin/shift/modify")
	public String postModify(Model model, @ModelAttribute MultiAttendances multiAttendances) {
		List<ShiftResult> shiftResult = multiAttendances.getMultiAttendances();
		shiftService.updateAttendances(shiftResult);
		//確認ボタンを押すと拠点検索フォームにリダイレクト
		return "redirect:/admin/shift_search";
	}

	/**
	 * 作成したシフトを登録する
	 *
	 * @param model
	 * @param multiAttendances
	 * @return
	 */
	@PostMapping("/admin/shift_update")
	public String postShiftUpdate(Model model, @ModelAttribute MultiAttendances multiAttendances) {
		List<ShiftResult> shiftResult = multiAttendances.getMultiAttendances();
		shiftService.updateAttendances(shiftResult);
		//確認ボタンを押すと拠点検索フォームにリダイレクト
		return "redirect:/admin/shift_make";
	}

}
