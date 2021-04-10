package com.masahiro.nakamoto.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.Invoice;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.domain.shift.ShiftResult;
import com.masahiro.nakamoto.service.AccountingService;
import com.masahiro.nakamoto.service.AreaService;
import com.masahiro.nakamoto.service.DateService;
import com.masahiro.nakamoto.service.DriverService;
import com.masahiro.nakamoto.service.InvoiceService;
import com.masahiro.nakamoto.service.ShiftService;

@Controller
public class InvoiceController {

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	DateService dateService;

	@Autowired
	DriverService driverService;

	@Autowired
	AreaService areaService;

	@Autowired
	ShiftService shiftService;

	@Autowired
	AccountingService accountingService;

	@Autowired
	ShiftForm shiftForm;


	@GetMapping("/invoice")
	public String getInvoice(HttpServletResponse response, Principal principal) {

		//ドライバー情報の取得
		Authentication auth = (Authentication)principal;
		UserDetails user = (UserDetails) auth.getPrincipal();
		String id = user.getUsername();
		Driver driver = driverService.findDriverInfo(id);
		String name = driver.getLastName() + " " + driver.getFirstName();
		String phoneNumber = invoiceService.makePhoneNumber(driver.getPhoneNumber());
		String email = id;
		String area = areaService.findAreaName(driver.getAreaId()).getName();
		int dailyWages = driver.getDailyWages();
		int monthlyExpenses = driver.getMonthlyExpenses();
		shiftForm.setId(id);
		shiftForm.setYear(Integer.toString(LocalDate.now().getYear()));
		shiftForm.setMonth(dateService.convertDate());
		ShiftResult shiftResult = shiftService.findShift(shiftForm);
		int workingDays = shiftResult.getWorkingDays();
		int amount = accountingService.getProfit(dailyWages, monthlyExpenses, workingDays);
		int earnings = accountingService.getEarnings(dailyWages, workingDays);

		//パラメーターの設定
		Map<String, Object> params = new HashedMap<>();
		String today = dateService.getToday();
		params.put("date", today);
		params.put("bankName", "◯◯銀行");
		params.put("branchName", "✕✕支店");
		params.put("branchCode", "123");
		params.put("accountType", "普通");
		params.put("accountNumber", "1234567");

		//フィールドの設定
		List<Invoice> fields = new ArrayList<>();
		fields.add(new Invoice(name, phoneNumber, email, area, workingDays, dailyWages, monthlyExpenses, amount, earnings));

		byte[] output  = invoiceService.OrderReporting2(params, fields);

		response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + "sample.pdf");
        response.setContentLength(output.length);

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            os.write(output);
            os.flush();

            os.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

		return null;
	}

}
