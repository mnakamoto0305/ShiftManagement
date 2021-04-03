package com.masahiro.nakamoto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.attendance.Attendance;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.domain.shift.ShiftResult;
import com.masahiro.nakamoto.domain.shift.Today;
import com.masahiro.nakamoto.mybatis.AreaMapper;
import com.masahiro.nakamoto.mybatis.ShiftMapper;

@Service
public class ShiftService {

	@Autowired
	ShiftMapper shiftMapper;

	@Autowired
	AreaMapper areaMapper;

	@Autowired
	DateService dateService;

	@Autowired
	Course course;

	public Today findTodayShift(String id) {
		Today today = shiftMapper.findTodayShift(LocalDate.now(), id);
		today.setAreaName(areaMapper.findAreaName(today.getAreaId()).getName());
		return today;
	}

	/**
	 * 指定したエリア・年月のシフトを検索する(シフト作成用)
	 *
	 * @param shiftForm
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public List<ShiftResult> makeMultiAttendances(ShiftForm shiftForm) throws Exception {
		//月初と月末の指定
		LocalDate first = LocalDate.now().plusMonths(1).withDayOfMonth(1);
		LocalDate last = first.plusMonths(1).minusDays(1);

		List<ShiftResult> multiAttendances = new ArrayList<>();
		List<Attendance> attendancesList;

		int totalDrivers = findCourseInfo(shiftForm).getTotalDrivers();

		//各コースの勤怠を月初から月末までのループで取得
		while (!first.equals(last.plusDays(1))) {
			shiftForm.setDate(first);
			attendancesList = shiftMapper.findAttendances(shiftForm);
			if (attendancesList.size() != totalDrivers) {
				throw new Exception();
			}
			//日付を曜日付きに変換
			for (Attendance attendance : attendancesList) {
				LocalDate ld = attendance.getDate();
				attendance.setConvertedDate(dateService.convertDate(ld));
			}
			ShiftResult shiftResult = new ShiftResult();
			shiftResult.setAttendanceList(attendancesList);
			shiftResult.setNumberOfTrue(shiftMapper.findNumberOfTrue(shiftForm));
			multiAttendances.add(shiftResult);
			first = first.plusDays(1);
		}


		return multiAttendances;
	}

	/**
	 * 指定したエリア・年月のシフトを検索する(シフト確認用)
	 *
	 * @param shiftForm
	 * @return
	 */
	@Transactional
	public List<ShiftResult> findMultiAttendances(ShiftForm shiftForm) {
		//フォームから受け取った日付をLocalDateに変換
		String designatedDate = shiftForm.getYear() + "/" + shiftForm.getMonth();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate date = LocalDate.parse(designatedDate + "/01", formatter);
		//月初と月末の指定
		LocalDate first = date.withDayOfMonth(1);
		LocalDate last = date.withDayOfMonth(1).plusMonths(1).minusDays(1);

		List<ShiftResult> multiAttendances = new ArrayList<>();
		List<Attendance> attendancesList;

		//各コースの勤怠を月初から月末までのループで取得
		while (!first.equals(last.plusDays(1))) {
			shiftForm.setDate(first);
			attendancesList = shiftMapper.findAttendances(shiftForm);
			//日付を曜日付きに変換
			for (Attendance attendance : attendancesList) {
				LocalDate ld = attendance.getDate();
				attendance.setConvertedDate(dateService.convertDate(ld));
			}
			ShiftResult shiftResult = new ShiftResult();
			shiftResult.setAttendanceList(attendancesList);
			shiftResult.setNumberOfTrue(shiftMapper.findNumberOfTrue(shiftForm));
			multiAttendances.add(shiftResult);
			first = first.plusDays(1);
		}

		return multiAttendances;
	}

	/**
	 * 通常コースのシフト検索
	 *
	 * @param shiftForm
	 * @param course
	 * @return
	 */
	public List<ShiftResult> findCourseAttendances(ShiftForm shiftForm, Course course) {
		//フォームから受け取った日付をLocalDateに変換
		String designatedDate = shiftForm.getYear() + "/" + shiftForm.getMonth();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate date = LocalDate.parse(designatedDate + "/01", formatter);
		//月初と月末の指定
		LocalDate first = date.withDayOfMonth(1);
		LocalDate last = date.withDayOfMonth(1).plusMonths(1).minusDays(1);

		List<ShiftResult> multiAttendances = new ArrayList<>();
		List<Attendance> attendancesList;

		//各コースの勤怠を月初から月末までのループで取得
		while (!first.equals(last.plusDays(1))) {
			shiftForm.setDate(first);
			attendancesList = shiftMapper.findCourseAttendances(shiftForm, course);
			//日付を曜日付きに変換
			for (Attendance attendance : attendancesList) {
				LocalDate ld = attendance.getDate();
				attendance.setConvertedDate(dateService.convertDate(ld));
 			}
			ShiftResult shiftResult = new ShiftResult();
			shiftResult.setAttendanceList(attendancesList);
			shiftResult.setNumberOfTrue(shiftMapper.findNumberOfTrue(shiftForm));
			multiAttendances.add(shiftResult);
			first = first.plusDays(1);
		}

		return multiAttendances;
	}

	/**
	 * 代走ドライバーのシフト検索
	 *
	 * @param shiftForm
	 * @param course
	 * @return
	 */
	public List<ShiftResult> findSubstituteAttendances(ShiftForm shiftForm, Course course) {
		//フォームから受け取った日付をLocalDateに変換
		String designatedDate = shiftForm.getYear() + "/" + shiftForm.getMonth();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate date = LocalDate.parse(designatedDate + "/01", formatter);
		//月初と月末の指定
		LocalDate first = date.withDayOfMonth(1);
		LocalDate last = date.withDayOfMonth(1).plusMonths(1).minusDays(1);

		List<ShiftResult> multiAttendances = new ArrayList<>();
		List<Attendance> attendancesList;

		//各コースの勤怠を月初から月末までのループで取得
		while (!first.equals(last.plusDays(1))) {
			shiftForm.setDate(first);
			attendancesList = shiftMapper.findSubstituteAttendances(shiftForm, course);
			ShiftResult shiftResult = new ShiftResult();
			shiftResult.setAttendanceList(attendancesList);
			shiftResult.setNumberOfTrue(shiftMapper.findNumberOfTrue(shiftForm));
			multiAttendances.add(shiftResult);
			first = first.plusDays(1);
		}

		return multiAttendances;
	}


	/**
	 * 指定した年月のシフトを検索(ドライバーの個人ページ用)
	 *
	 * @param shiftForm
	 * @return
	 */
	@Transactional
	public ShiftResult findShift(ShiftForm shiftForm) {
		//フォームから受け取った日付をLocalDateに変換
		String designatedDate = shiftForm.getYear() + "/" + shiftForm.getMonth();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate date = LocalDate.parse(designatedDate + "/01", formatter);
		//月初と月末の指定
		LocalDate first = date.withDayOfMonth(1);
		LocalDate last = date.withDayOfMonth(1).plusMonths(1).minusDays(1);
		shiftForm.setFirst(first);
		shiftForm.setLast(last);
		ShiftResult shiftResult = new ShiftResult();
		shiftResult.setAttendanceList(shiftMapper.findMonthShift(shiftForm));
		//出勤日数をセット
		shiftResult.setWorkingDays(shiftMapper.findWorkingDays(shiftForm));

		return shiftResult;
	}

	/**
	 * フォームから受け取ったシフトを登録する
	 *
	 * @param multiAttendances
	 */
	@Transactional
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
	@Transactional
	public Course findCourseInfo(ShiftForm shiftForm) {
		return shiftMapper.findCourseInfo(shiftForm);
	}

	/**
	 * 指定した拠点のドライバー名を取得
	 *
	 * @param shiftForm
	 * @return
	 */
	@Transactional
	public List<Driver> findDriverName(ShiftForm shiftForm) {
		return shiftMapper.findDriverName(shiftForm);
	}

	/**
	 * シフト作成画面に各ドライバーの出勤日数を表示
	 *
	 * @param shiftForm
	 * @return
	 */
	@Transactional
	public List<Integer> findTotal(ShiftForm shiftForm) {
		//月初と月末の指定
		LocalDate first = LocalDate.now().plusMonths(1).withDayOfMonth(1);
		LocalDate last = first.plusMonths(1).minusDays(1);
		shiftForm.setFirst(first);
		shiftForm.setLast(last);
		//ドライバー数を取得
		int numberOfDrivers = findCourseInfo(shiftForm).getTotalDrivers();
		//出勤数のリスト
		List<Integer> total = new ArrayList<>();

		for (int i = 1; i < numberOfDrivers + 1; i++) {
			shiftForm.setCourseId(i);
			total.add(shiftMapper.findTotal(shiftForm));
		}

		return total;
	}

	/**
	 * シフト確認画面に各ドライバーの出勤日数を表示(アドミン画面)
	 *
	 * @param shiftForm
	 * @return
	 */
	@Transactional
	public List<Integer> findTotalAttendance(ShiftForm shiftForm) {
		//フォームから受け取った日付をLocalDateに変換
		String designatedDate = shiftForm.getYear() + "/" + shiftForm.getMonth();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate date = LocalDate.parse(designatedDate + "/01", formatter);
		//月初と月末の指定
		LocalDate first = date.withDayOfMonth(1);
		LocalDate last = date.withDayOfMonth(1).plusMonths(1).minusDays(1);

		shiftForm.setFirst(first);
		shiftForm.setLast(last);

		//ドライバー数を取得
		int numberOfDrivers = findCourseInfo(shiftForm).getTotalDrivers();
		//出勤数のリスト
		List<Integer> total = new ArrayList<>();

		for (int i = 1; i < numberOfDrivers + 1; i++) {
			shiftForm.setCourseId(i);
			total.add(shiftMapper.findTotal(shiftForm));
		}

		return total;
	}

}
