package com.masahiro.nakamoto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.domain.Attendance;
import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.ShiftForm;
import com.masahiro.nakamoto.domain.ShiftResult;
import com.masahiro.nakamoto.mybatis.ShiftMapper;

@Service
public class ShiftService {

	@Autowired
	ShiftMapper shiftMapper;

	@Autowired
	Course course;

	/**
	 * 指定したエリア・年月のシフトを検索する
	 *
	 * @param shiftForm
	 * @return
	 */
	public List<ShiftResult> findMultiAttendances(ShiftForm shiftForm) {
		//フォームから受け取った日付をLocalDateに変換
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate date = LocalDate.parse(shiftForm.getDesignatedDate() + "/01", formatter);
		//月初と月末の指定
		LocalDate first = date.withDayOfMonth(1);
		LocalDate last = date.withDayOfMonth(1).plusMonths(1).minusDays(1);

		List<ShiftResult> multiAttendances = new ArrayList<>();
		List<Attendance> attendancesList;

		//各コースの勤怠を月初から月末までのループで取得
		while (!first.equals(last.plusDays(1))) {
			shiftForm.setDate(first);
			attendancesList = shiftMapper.findAttendances(shiftForm);
			ShiftResult shiftResult = new ShiftResult();
			shiftResult.setAttendanceList(attendancesList);
			multiAttendances.add(shiftResult);
			first = first.plusDays(1);
		}

		return multiAttendances;
	}

	/**
	 * フォームから受け取ったシフトを登録する
	 *
	 * @param multiAttendances
	 */
	public void updateAttendances(List<ShiftResult> multiAttendances) {
		for (ShiftResult shiftResult : multiAttendances) {
			shiftMapper.updateAttendances(shiftResult.getAttendanceList());
		}
	}

	/**
	 * 指定したエリアのコース数とドライバー数を検索する
	 *
	 * @param shiftForm
	 * @return
	 */
	public Course findCourseInfo(ShiftForm shiftForm) {
		return shiftMapper.findCourseInfo(shiftForm);

	};

}
