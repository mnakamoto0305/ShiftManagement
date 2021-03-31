package com.masahiro.nakamoto.domain.attendance;

import java.util.List;

import org.springframework.stereotype.Component;

import com.masahiro.nakamoto.domain.shift.ShiftResult;

import lombok.Data;

@Data
@Component
public class MultiAttendances {

	private List<ShiftResult> multiAttendances;

	public MultiAttendances(List<ShiftResult> multiAttendances) {
		this.multiAttendances = multiAttendances;
	}

}
