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
	 * 1人分のシフトを更新
	 *
	 * @param attendancesList
	 * @return
	 */
	public boolean updateAttendances(@Param("attendancesList") List<Attendance> attendancesList);

	/**
	 * 指定したエリア・年月のシフトを検索
	 *
	 * @param shiftForm
	 * @return
	 */
	public List<Attendance> findAttendances(ShiftForm shiftForm);

	/**
	 * 指定したエリアのコース数とドライバー数を検索
	 *
	 * @param shiftForm
	 * @return
	 */
	public Course findCourseInfo(ShiftForm shiftForm);

}
