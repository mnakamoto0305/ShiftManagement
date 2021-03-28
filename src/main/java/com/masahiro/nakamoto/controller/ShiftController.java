package com.masahiro.nakamoto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.masahiro.nakamoto.domain.Attendance;
import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.MultiAttendances;
import com.masahiro.nakamoto.domain.ShiftForm;
import com.masahiro.nakamoto.domain.ShiftResult;
import com.masahiro.nakamoto.service.ShiftService;

@Controller
public class ShiftController {

	@Autowired
	ShiftService shiftService;

	@Autowired
	Attendance attendance;

	@Autowired
	Course course;


	/**
	 * シフトを検索するための条件を指定するフォーム
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
	 * 作成するシフトを表示
	 *
	 * @param model
	 * @param multiAttendances
	 * @param shiftForm
	 * @return
	 */
	@PostMapping("/admin/shift_make")
	public String postShiftMake(Model model, @ModelAttribute MultiAttendances multiAttendances, @ModelAttribute ShiftForm shiftForm) {
		course = shiftService.findCourseInfo(shiftForm);
		int totalCourses = course.getTotalCourses();
		multiAttendances.setMultiAttendances(shiftService.findMultiAttendances(shiftForm));
		model.addAttribute("course", course);
		model.addAttribute("totalCourses", totalCourses);
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
