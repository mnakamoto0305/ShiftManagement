package com.masahiro.nakamoto.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ShiftResult {

	private List<Attendance> attendanceList;

	private int numberOfTrue;

	private int workingDays;

}
