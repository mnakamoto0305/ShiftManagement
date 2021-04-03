package com.masahiro.nakamoto.controller.shift;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String postShiftResultAdmin(Model model, @ModelAttribute ShiftForm shiftForm) {
		//コース情報をセット
		course = shiftService.findCourseInfo(shiftForm);
		int totalCourses = course.getTotalCourses();
		//勤怠情報をセット
		MultiAttendances courseAttendances = new MultiAttendances(shiftService.findCourseAttendances(shiftForm, course));
		List<List<Integer>> substituteList = substituteService.findSubstituteShift(shiftForm, course);
		//拠点名をセット
		area = areaService.findAreaName(shiftForm.getArea());
		//年月をセット
		String year = shiftForm.getYear();
		String month = shiftForm.getMonth();
		//各ドライバーの出勤数をセット
		List<Integer> totalAttendance = shiftService.findTotalAttendance(shiftForm);
		//ドライバーの名前をセット
		List<Driver> driverName = shiftService.findDriverName(shiftForm);
		//表示するシフトの月と今日の月をセット
		int shiftMonth = shiftForm.getDate().getMonthValue();
		model.addAttribute("shiftMonth", shiftMonth);
		int thisMonth = LocalDate.now().getMonthValue();
		model.addAttribute("thisMonth", thisMonth);

		model.addAttribute("course", course);
		model.addAttribute("courseAttendances", courseAttendances);
		model.addAttribute("area", area);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("totalAttendance", totalAttendance);
		model.addAttribute("totalCourses", totalCourses);
		model.addAttribute("driverName", driverName);
		model.addAttribute("substituteList", substituteList);
		model.addAttribute("contents", "shift/result :: result");
		return "main/adminLayout";
	}

	@PostMapping("/shift_result")
	public String postShiftResult(Model model, @ModelAttribute ShiftForm shiftForm, Principal principal) {
		//社員IDをセット
		Authentication auth = (Authentication)principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		shiftForm.setId(user.getUsername());
		ShiftResult shiftResult = shiftService.findShift(shiftForm);
		//年月をセット
		String year = shiftForm.getYear();
		String month = shiftForm.getMonth();
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("shiftResult", shiftResult);
		model.addAttribute("contents", "shift/result :: resultForDriver");
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
		//コース情報をセット
		course = shiftService.findCourseInfo(shiftForm);
		int totalCourses = course.getTotalCourses();
		//勤怠情報をセット
		multiAttendances.setMultiAttendances(shiftService.makeMultiAttendances(shiftForm));
		//拠点名をセット
		area = areaService.findAreaName(shiftForm.getArea());
		//表示するシフトの年月をセット
		YearMonth date = YearMonth.now().plusMonths(1);
		//各ドライバーの出勤数をセット
		List<Integer> totalAttendance = shiftService.findTotal(shiftForm);
		//担当ドライバーの名前をセット
		List<Driver> driverName = shiftService.findDriverName(shiftForm);
		model.addAttribute("course", course);
		model.addAttribute("area", area);
		model.addAttribute("date", date);
		model.addAttribute("totalAttendance", totalAttendance);
		model.addAttribute("totalCourses", totalCourses);
		model.addAttribute("driverName", driverName);
		model.addAttribute("contents", "shift/makeMultiShift :: shift_make");
		return "main/adminLayout";
	}

	/**
	 * 今月のシフトを修正
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
		//勤怠情報をセット
		multiAttendances.setMultiAttendances(shiftService.findMultiAttendances(shiftForm));
		//拠点名をセット
		area = areaService.findAreaName(shiftForm.getArea());
		//表示するシフトの年月をセット
		YearMonth date = YearMonth.now();
		//各ドライバーの出勤数をセット
		List<Integer> totalAttendance = shiftService.findTotalAttendance(shiftForm);
		//担当ドライバーの名前をセット
		List<Driver> driverName = shiftService.findDriverName(shiftForm);
		model.addAttribute("course", course);
		model.addAttribute("area", area);
		model.addAttribute("date", date);
		model.addAttribute("totalAttendance", totalAttendance);
		model.addAttribute("totalCourses", totalCourses);
		model.addAttribute("driverName", driverName);
		model.addAttribute("contents", "shift/makeMultiShift :: shift_make");
		return "main/adminLayout";
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

		model.addAttribute("contents", "admin/admin :: admin");
		return "main/adminLayout";
	}

}
