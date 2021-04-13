package com.masahiro.nakamoto.mybatis;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.masahiro.nakamoto.domain.holiday.Attendance;
import com.masahiro.nakamoto.domain.shift.ShiftForm;


/**
 * Attendancesマッパー
 * 休み希望をデータベースに登録する
 */
@Mapper
public interface HolidayMapper {

	/**
	 * 勤怠を登録
	 *
	 * @param attendance
	 * @return
	 */
	public boolean registerAttendances(Attendance attendance);

	/**
	 * 受け取った休み希望日の勤怠をfalseに更新
	 *
	 * @param attendance
	 * @return
	 */
	public boolean registerHoliday(Attendance attendance);


	/**
	 * 登録した休み希望日を確認する
	 *
	 * @param shiftForm
	 * @return
	 */
	public List<Attendance> findHoliday(ShiftForm shiftForm);

	/**
	 * 指定した拠点で休み希望日を提出している人数を取得
	 *
	 * @param areaId
	 * @return
	 */
	public int findSubmittedNumber(@Param("areaId") int areaId, @Param("date") LocalDate date);

	/**
	 * 指定したドライバーが休み希望を提出しているかを検索
	 *
	 * @param areaId
	 * @param courseId
	 * @param date
	 * @return
	 */
	public int isSubmitted(@Param("areaId") int areaId, @Param("courseId") int courseId, @Param("date") LocalDate date);

}
