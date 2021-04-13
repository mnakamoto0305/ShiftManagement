package com.masahiro.nakamoto.domain.shift;

import org.springframework.stereotype.Component;

import com.masahiro.nakamoto.domain.holiday.MultiAttendances;

import lombok.Data;

@Data
@Component
public class ShiftConfirm {

	private MultiAttendances courseAttendances;

	private MultiAttendances substituteAttendances;

	public ShiftConfirm(MultiAttendances courseAttendances, MultiAttendances substituteAttendances) {
		this.courseAttendances = courseAttendances;
		this.substituteAttendances = substituteAttendances;
	}

}
