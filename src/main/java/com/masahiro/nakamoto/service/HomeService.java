package com.masahiro.nakamoto.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public Map<Integer, Integer> findSubstituteShift(ShiftForm shiftForm, Course course) {

		List<Integer> holidayDriver = shiftMapper.findHolidayDriver(shiftForm, course);
		List<Integer> attendances = shiftMapper.findSubstituteShift(shiftForm, course);

		System.out.println(holidayDriver);
		System.out.println(attendances);

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

}
