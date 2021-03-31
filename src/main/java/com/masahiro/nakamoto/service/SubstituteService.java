package com.masahiro.nakamoto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.Substitute;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.mybatis.ShiftMapper;

@Service
public class SubstituteService {

	@Autowired
	ShiftMapper shiftMapper;

	@Autowired
	ShiftService shiftService;

	/**
	 * 通常コースで休みのドライバーを検索
	 *
	 * @param shiftForm
	 * @param course
	 * @return
	 */
	@Transactional
	public List<Substitute> findHolidayDriver(ShiftForm shiftForm, Course course) {
		//フォームから受け取った日付をLocalDateに変換
		String designatedDate = shiftForm.getYear() + "/" + shiftForm.getMonth();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate date = LocalDate.parse(designatedDate + "/01", formatter);
		//月初と月末の指定
		LocalDate first = date.withDayOfMonth(1);
		LocalDate last = date.withDayOfMonth(1).plusMonths(1).minusDays(1);

		List<Substitute> substituteList = new ArrayList<>();


		while (!first.equals(last.plusDays(1))) {
			shiftForm.setDate(first);
			Substitute substitute = new Substitute();
			substitute.setDriverList(shiftMapper.findHolidayDriver(shiftForm, course));
			substituteList.add(substitute);
			first = first.plusDays(1);
		}

		return substituteList;
	}

	/**
	 * 代走ドライバーのシフトを検索
	 *
	 * @param shiftForm
	 * @param course
	 * @return
	 */
	@Transactional
	public List<List<Integer>> findSubstituteShift(ShiftForm shiftForm, Course course) {
		//フォームから受け取った日付をLocalDateに変換
		String designatedDate = shiftForm.getYear() + "/" + shiftForm.getMonth();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate date = LocalDate.parse(designatedDate + "/01", formatter);
		//月初と月末の指定
		LocalDate first = date.withDayOfMonth(1);
		LocalDate last = date.withDayOfMonth(1).plusMonths(1).minusDays(1);
		//拠点のコース情報をセット
		int totalCourses = course.getTotalCourses();
		int totalDrivers = course.getTotalDrivers();
		//走るコース情報をセットしたリスト
		List<List<Integer>> substituteList = new ArrayList<>();
		//代走ドライバーの勤怠リスト
		List<List<Integer>> booleanLists = new ArrayList<>();
		//休みが発生しているコース番号のリスト
		List<List<Substitute>> sl = new ArrayList<>();

		//リストに情報を格納
		while (!first.equals(last.plusDays(1))) {
			shiftForm.setDate(first);
			List<Substitute> holidayDriver = findHolidayDriver(shiftForm, course);
			sl.add(holidayDriver);
			shiftForm.setDate(first);
			List<Integer> booleanList = shiftMapper.findSubstituteShift(shiftForm, course);
			booleanLists.add(booleanList);
			first = first.plusDays(1);
		}

		//代走ドライバーの出勤日に担当するコース番号をセット
		int a = 0;
		for (List<Integer> booleanList : booleanLists) {
			int b = 0;
			//System.out.println(booleanList);
			List<Integer> substitute = new ArrayList<>();
			int count = 0;
			for (int i = totalCourses + 1; i < totalDrivers + 1; i++) {
				int isAttendance = booleanList.get(b);

				if (isAttendance == 0) {
					substitute.add(0);
				} else if (isAttendance == 1) {
					substitute.add(sl.get(0).get(a).getDriverList().get(count));
					count++;
				}

				b++;
			}
			substituteList.add(substitute);
			a++;
		}

		return substituteList;
	}
}
