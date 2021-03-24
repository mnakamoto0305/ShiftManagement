package com.masahiro.nakamoto.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Attendance {

	private LocalDate date;

	private boolean isAttendance;

	private String id;

}
