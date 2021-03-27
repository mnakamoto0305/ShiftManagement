package com.masahiro.nakamoto.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.masahiro.nakamoto.domain.Attendance;
import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.ShiftForm;

@Mapper
public interface ShiftMapper {

	/**
	 * 指定した期間と拠点のシフトを検索
	 *
	 * @param shiftForm
	 * @return
	 */
	public List<Attendance> findAttendances(ShiftForm shiftForm, Course course);

	/**
	 * 1人分のシフトを更新
	 *
	 * @param attendancesList
	 * @return
	 */
	public boolean updateAttendances(@Param("attendancesList") List<Attendance> attendancesList);

	public Integer findTotalCourses(ShiftForm shiftForm);

}
