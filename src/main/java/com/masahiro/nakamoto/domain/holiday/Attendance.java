package com.masahiro.nakamoto.domain.holiday;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

/**
 * 勤怠情報を表現するオブジェクト
 */
@Component
public class Attendance {

	/**
	 * 日付
	 */
	private LocalDate date;

	/**
	 * yyyy/MM/dd/(E)へ変換した日付
	 */
	private String convertedDate;

	/**
	 * 勤怠
	 */
	private Boolean isAttendance;

	/**
	 * id
	 */
	private String id;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Boolean getIsAttendance() {
		return isAttendance;
	}

	public void setIsAttendance(Boolean isAttendance) {
		this.isAttendance = isAttendance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConvertedDate() {
		return convertedDate;
	}

	public void setConvertedDate(String convertedDate) {
		this.convertedDate = convertedDate;
	}

}
