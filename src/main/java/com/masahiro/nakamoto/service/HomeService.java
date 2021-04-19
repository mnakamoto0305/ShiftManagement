package com.masahiro.nakamoto.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.shift.ShiftForm;
import com.masahiro.nakamoto.mybatis.ShiftMapper;

/**
 * ホーム画面に表示する勤怠情報を取得するサービス
 */
@Service
public class HomeService {

	@Autowired
	ShiftMapper shiftMapper;

	/**
	 * 代走ドライバーが走るコースをマッピングする(1日分)
	 *
	 * @param shiftForm
	 * @param course
	 * @return
	 */
	public Map<Integer, Integer> findSubstituteShift(ShiftForm shiftForm, Course course) {

		//通常コースで休みのドライバーを取得
		List<Integer> holidayDriver = shiftMapper.findHolidayDriver(shiftForm, course);
		//代走ドライバの勤怠を取得
		List<Integer> attendances = shiftMapper.findSubstituteShift(shiftForm, course);

		//代走ドライバーの出勤日に走るコースをマッピング
		Map<Integer, Integer> substituteMap = new HashMap<>();
		int count = 0;
		int courseId = course.getTotalCourses() + 1;
		for (Integer attendance : attendances) {
			if (attendance == 0) {
				substituteMap.put(courseId, 0);
			} else if (attendance == 1) {
				substituteMap.put(courseId, holidayDriver.get(count));
				count++;
			}
			courseId++;
		}

		return substituteMap;
	}

	/**
	 * 代走ドライバーが走るコースをマッピングする(1月分)
	 *
	 * @param shiftForm
	 * @param course
	 * @param id
	 * @return
	 */
	public List<Map<Integer, Integer>> findSubstituteShiftMonth(ShiftForm shiftForm, Course course) {
		//月初と月末の指定
		LocalDate first = LocalDate.now().withDayOfMonth(1);
		LocalDate last = first.plusMonths(1).minusDays(1);

		List<Map<Integer, Integer>> mapList = new ArrayList<>();

		while (!first.equals(last.plusDays(1))) {
			shiftForm.setDate(first);
			Map<Integer, Integer> substituteMap = findSubstituteShift(shiftForm, course);
			mapList.add(substituteMap);
			first = first.plusDays(1);
		}

		return mapList;

	}

	/**
	 * コース情報をリストで取得
	 *
	 * @param mapList
	 * @param courseId
	 * @return
	 */
	public List<Integer> findCourse(List<Map<Integer, Integer>> mapList, int courseId) {
		List<Integer> list = new ArrayList<>();

		for (Map<Integer, Integer> map : mapList) {
			list.add(map.get(courseId));
		}

		return list;
	}

}
