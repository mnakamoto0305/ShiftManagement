package com.masahiro.nakamoto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.domain.Attendance;
import com.masahiro.nakamoto.domain.RegisterHolidayForm;
import com.masahiro.nakamoto.mybatis.AttendancesMapper;


/**
 * 休み希望日を登録するサービス
 */
@Service
public class AttendancesService {

	@Autowired
	AttendancesMapper attendancesMapper;

	/**
	 * 休み希望登録前にその月をすべて出勤扱いで登録
	 *
	 * @param attendance
	 * @return
	 */
	@Transactional
	public void registerAttendances(Attendance attendance, String id) {
		//今日の日付取得
		LocalDate today = LocalDate.now();
		//月初の日付取得
		LocalDate firstDay = today.plusMonths(1).withDayOfMonth(1);
		//月末の日付取得
		LocalDate lastDay = today.withDayOfMonth(1).plusMonths(2).minusDays(1);

		//月初から月末までの勤怠をtrueで登録するwhile文
		while(!firstDay.equals(lastDay.plusDays(1))) {
			//attendanceインスタンスのフィールドに値をセット
			attendance.setId(id);
			attendance.setIsAttendance(true);
			attendance.setDate(firstDay);
			//勤怠を登録
			attendancesMapper.registerAttendances(attendance);
			//firstDayを1日進める
			firstDay = firstDay.plusDays(1);
		}
	}

	@Transactional
	public void registerHoliday(RegisterHolidayForm registerHolidayForm, Attendance attendance, String id) {
		//社員IDをセット
		attendance.setId(id);
		//勤怠を休みにセット
		attendance.setIsAttendance(false);
		List<String> stringDate = Arrays.asList(registerHolidayForm.getHoliday().split(","));

		//取得した日付のフォーマットに合わせる
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		//LocalDate型のリストを作成して、Stringから変換した日付を入れる
		List<LocalDate> holidayList = new ArrayList<>();
		for(String sd : stringDate) {
			LocalDate date = LocalDate.parse(sd, formatter);
			holidayList.add(date);
		}

		//holidayListから日付を取り出してattendanceにセット→休み希望日を登録
		for (LocalDate holiday : holidayList) {
			attendance.setDate(holiday);
			attendancesMapper.registerHoliday(attendance);
		}
	}

}
