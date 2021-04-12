package com.masahiro.nakamoto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.domain.attendance.Attendance;
import com.masahiro.nakamoto.domain.attendance.RegisterHolidayForm;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.domain.shift.ShiftResult;
import com.masahiro.nakamoto.mybatis.HolidayMapper;
import com.masahiro.nakamoto.mybatis.UserMapper;


/**
 * 休み希望日を登録するサービス
 */
@Service
public class HolidayService {

	@Autowired
	HolidayMapper holidayMapper;

	@Autowired
	DateService dateService;

	@Autowired
	AreaService areaService;

	@Autowired
	DriverService driverService;

	@Autowired
	UserMapper userMapper;

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
			holidayMapper.registerAttendances(attendance);
			//firstDayを1日進める
			firstDay = firstDay.plusDays(1);
		}
	}

	/**
	 * 休み希望日を登録
	 *
	 * @param registerHolidayForm
	 * @param attendance
	 * @param id
	 */
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
			holidayMapper.registerHoliday(attendance);
		}
	}

	/**
	 * 登録した休み希望日を検索
	 *
	 * @param shiftForm
	 * @return
	 */
	public ShiftResult findHoliday(ShiftForm shiftForm) {
		//月初と月末の指定
		LocalDate first = LocalDate.now().plusMonths(1).withDayOfMonth(1);
		LocalDate last = first.plusMonths(1).minusDays(1);
		shiftForm.setFirst(first);
		shiftForm.setLast(last);
		//登録した休み希望日の検索
		ShiftResult shiftResult = new ShiftResult();
		shiftResult.setAttendanceList(holidayMapper.findHoliday(shiftForm));
		//日付を曜日付きに変換
		for (Attendance attendance : shiftResult.getAttendanceList()) {
			LocalDate ld = attendance.getDate();
			attendance.setConvertedDate(dateService.convertDate(ld));
		}

		return shiftResult;
	}

	/**
	 * 月初から月末までの勤怠をTrueで登録
	 *
	 * @param attendance
	 * @param id
	 */
	@Transactional
	public void makeTrue(Attendance attendance, String id) {
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
			holidayMapper.registerHoliday(attendance);
			//firstDayを1日進める
			firstDay = firstDay.plusDays(1);
		}
	}

	/**
	 * 各拠点で休み希望を提出したドライバーの数を取得
	 *
	 * @return
	 */
	public List<Integer> findSubmittedNumber() {
		//来月の1日のLocalDateを取得
		LocalDate date = LocalDate.now().plusMonths(1).withDayOfMonth(1);
		//リストの初期化
		List<Integer> submittedNumber = new ArrayList<>();
		//各拠点の休み希望提出者数を取得
		for (int i = 1; i < 6; i++) {
			submittedNumber.add(holidayMapper.findSubmittedNumber(i, date));
		}
		return submittedNumber;
	}

	/**
	 * 指定した拠点のドライバーが休み希望を提出しているかを確認
	 *
	 * @param areaId
	 * @return
	 */
	public List<Integer> isSubmitted(int areaId) {
		//来月の1日のLocalDateを取得
		LocalDate date = LocalDate.now().plusMonths(1).withDayOfMonth(1);
		//ドライバー数を取得
		int totalDrivers = areaService.findTotalDrivers(areaId);
		//Listを初期化
		List<Integer> list = new ArrayList<>();
		//各ドライバーが休み希望を提出したかどうかを確認
		for (int i = 1; i < totalDrivers + 1; i++) {
			int isSubmitted = holidayMapper.isSubmitted(areaId, i, date);
			list.add(isSubmitted);
		}
		return list;
	}

	public int isSubmitted(int areaId, int courseId) {
		//来月の1日のLocalDateを取得
		LocalDate date = LocalDate.now().plusMonths(1).withDayOfMonth(1);
		return holidayMapper.isSubmitted(areaId, courseId, date);
	}

	/**
	 * 指定したドライバーの休み希望を代理登録
	 *
	 * @param areaId
	 * @param courseId
	 * @param registerHolidayForm
	 */
	@Transactional
	public void proxyRegister(int areaId, int courseId, RegisterHolidayForm registerHolidayForm) {
		String id = userMapper.getId(areaId, courseId);
		Attendance attendance = new Attendance();
		registerAttendances(attendance, id);
		registerHoliday(registerHolidayForm, attendance, id);
	}

}
