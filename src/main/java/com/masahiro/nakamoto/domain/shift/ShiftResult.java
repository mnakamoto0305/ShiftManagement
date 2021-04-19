package com.masahiro.nakamoto.domain.shift;

import java.util.List;

import org.springframework.stereotype.Component;

import com.masahiro.nakamoto.domain.holiday.Attendance;

import lombok.Data;

/**
 * ドライバー個人ページ用のシフト情報を表現するオブジェクト
 */
@Data
@Component
public class ShiftResult {

	/**
	 * 勤怠情報
	 */
	private List<Attendance> attendanceList;

	/**
	 * 勤怠情報がTrueの数
	 */
	private int numberOfTrue;

	/**
	 * 出勤日数
	 */
	private int workingDays;

}
