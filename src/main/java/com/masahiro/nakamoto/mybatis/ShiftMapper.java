package com.masahiro.nakamoto.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.masahiro.nakamoto.domain.Course;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.attendance.Attendance;
import com.masahiro.nakamoto.domain.shift.ShiftForm;

/**
 * @author nakamotomasahiro
 *
 */
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
	 * 1人分のシフトを検索
	 *
	 * @param shiftForm
	 * @return
	 */
	public List<Attendance> findShift(ShiftForm shiftForm);

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

	/**
	 * その日の出勤者数を検索
	 *
	 * @param shiftForm
	 * @return
	 */
	public int findNumberOfTrue(ShiftForm shiftForm);

	/**
	 * 各ドライバーの出勤日数を検索(エリア全体)
	 *
	 * @param shiftForm
	 * @return
	 */
	public int findTotal(ShiftForm shiftForm);

	/**
	 * 各ドライバーの出勤日数を検索(1人分)
	 *
	 * @param shiftForm
	 * @return
	 */
	public int findWorkingDays(ShiftForm shiftForm);

	/**
	 * 各コースのドライバー名を検索
	 *
	 * @param shiftForm
	 * @return
	 */
	public List<Driver> findDriverName(ShiftForm shiftForm);

}
