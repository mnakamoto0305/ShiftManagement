package com.masahiro.nakamoto.controller;

import java.awt.Choice;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.masahiro.nakamoto.domain.Attendance;
import com.masahiro.nakamoto.domain.ShiftForm;
import com.masahiro.nakamoto.domain.ShiftResult;
import com.masahiro.nakamoto.service.ShiftService;

@Controller
public class ShiftController {

	@Autowired
	ShiftService shiftService;

	@Autowired
	Attendance attendance;


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
	 * シフトの検索結果を表示
	 *
	 * @param model
	 * @param shiftResult
	 * @param shiftForm
	 * @return
	 */
	@PostMapping("/admin/shift_result")
	public String postShiftResult(Model model, @ModelAttribute ShiftResult shiftResult, @ModelAttribute ShiftForm shiftForm) {

		shiftResult.setAttendanceList(shiftService.findAttendances(shiftForm));
		model.addAttribute("contents", "shift/shiftResult :: shift_result");
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
	 * シフト作成画面を表示
	 *
	 * @param model
	 * @param shiftResult
	 * @param shiftForm
	 * @param choice
	 * @return
	 */
	@PostMapping("/admin/shift_make")
	public String postShiftMake(Model model, @ModelAttribute ShiftResult shiftResult, @ModelAttribute ShiftForm shiftForm, @ModelAttribute Choice choice) {
		shiftResult.setAttendanceList(shiftService.findAttendances(shiftForm));
//
//		model.addAttribute("select_attendance", SELECT_ATTENDANCE);
		model.addAttribute("contents", "shift/makeShift :: shift_make");
		return "main/adminLayout";
	}


	/**
	 * 入力情報をもとにシフトを更新
	 *
	 * @param model
	 * @param shiftResult
	 * @return
	 */
	@PostMapping("/admin/shift_update")
	public String postShiftUpdate(Model model, ShiftResult shiftResult) {
		List<Attendance> attendanceList = shiftResult.getAttendanceList();
		shiftService.updateAttendances(attendanceList);

		model.addAttribute("contents", "admin/admin :: admin");
		return "main/adminLayout";
	}

}
