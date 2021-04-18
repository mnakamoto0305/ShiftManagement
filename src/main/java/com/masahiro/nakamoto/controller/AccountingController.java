package com.masahiro.nakamoto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.masahiro.nakamoto.domain.Accounting;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.service.AccountingService;
import com.masahiro.nakamoto.service.AreaService;
import com.masahiro.nakamoto.service.DriverService;
import com.masahiro.nakamoto.service.ShiftService;

@Controller
public class AccountingController {

	@Autowired
	ShiftService shiftService;

	@Autowired
	AccountingService accountingService;

	@Autowired
	DriverService driverService;

	@Autowired
	AreaService areaService;

	@GetMapping("/admin/accounting")
	public String getAccounting(Model model, @ModelAttribute ShiftForm shiftForm) {
		model.addAttribute("contents", "accounting/form :: form");
		return "main/adminLayout";
	}

	@PostMapping("/admin/accounting")
	public String postAccounting(Model model, @ModelAttribute ShiftForm shiftForm) {
		//出勤日数を取得
		List<Integer> workingDaysList = shiftService.findTotalAttendance(shiftForm);
		model.addAttribute("workingDaysList", workingDaysList);
		//ドライバー情報を取得
		int areaId = shiftForm.getArea();
		List<Driver> driverList = driverService.findAreaDriver(areaId);
		model.addAttribute("driverList", driverList);
		//支払予定額を取得
		List<Integer> paymentList = accountingService.getPayment(driverList, workingDaysList);
		//数字をカンマ表記に変換
		List<Accounting> accountingList = accountingService.commaOf1000(driverList, paymentList);
		model.addAttribute("accountingList", accountingList);
		//年月をセット
		String year = shiftForm.getYear();
		String month = shiftForm.getMonth();
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		//拠点名を取得
		String areaName = areaService.findAreaName(areaId).getName();
		model.addAttribute("areaName", areaName);
		model.addAttribute("contents", "accounting/result :: result");
		return "main/adminLayout";
	}

}
