package com.masahiro.nakamoto.domain;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class Attendance {

	private LocalDate date;

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


}
