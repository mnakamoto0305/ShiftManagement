package com.masahiro.nakamoto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.domain.Attendance;
import com.masahiro.nakamoto.domain.ShiftForm;
import com.masahiro.nakamoto.mybatis.ShiftMapper;

@Service
public class ShiftService {

	@Autowired
	ShiftMapper shiftMapper;

	/**
	 * 指定した年月のシフトを検索(1人分)
	 *
	 * @param shiftForm
	 * @return
	 */
	public List<Attendance> findAttendances(ShiftForm shiftForm) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate date = LocalDate.parse(shiftForm.getDesignatedDate() + "/01", formatter);


		//月初と月末の指定
		LocalDate first = date.withDayOfMonth(1);
		LocalDate last = date.withDayOfMonth(1).plusMonths(1).minusDays(1);
		shiftForm.setFirst(first);
		shiftForm.setLast(last);
		return shiftMapper.findAttendances(shiftForm);
	}

	public boolean updateAttendances(List<Attendance> attendanceList) {
		return shiftMapper.updateAttendances(attendanceList);
	}

}
