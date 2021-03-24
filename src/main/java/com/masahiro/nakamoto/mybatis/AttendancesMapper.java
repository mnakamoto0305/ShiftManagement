package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.masahiro.nakamoto.domain.Attendance;


/**
 * Attendancesマッパー
 * 休み希望をデータベースに登録する
 */
@Mapper
public interface AttendancesMapper {

	//勤怠の登録時に利用
	public boolean registerAttendances(Attendance attendance);

	//受け取った休み希望日の勤怠をfalseに更新
	public boolean registerHoliday(Attendance attendance);

}
