package com.masahiro.nakamoto.domain.holiday;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class Attendance {

	private LocalDate date;

	private String convertedDate;

	private Boolean isAttendance;

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
