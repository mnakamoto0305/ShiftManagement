package com.masahiro.nakamoto.domain.shift;

import java.util.List;

import org.springframework.stereotype.Component;

import com.masahiro.nakamoto.domain.holiday.Attendance;

import lombok.Data;

@Data
@Component
public class ShiftResult {

	private List<Attendance> attendanceList;

	private int numberOfTrue;

	private int workingDays;

}
