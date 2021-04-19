package com.masahiro.nakamoto.domain.holiday;

import java.util.List;

import org.springframework.stereotype.Component;

import com.masahiro.nakamoto.domain.shift.ShiftResult;

import lombok.Data;

/**
 * 複数人・複数日の勤怠情報を表現するオブジェクト
 */
@Data
@Component
public class MultiAttendances {

	/**
	 * 勤怠情報を集めたリスト
	 */
	private List<ShiftResult> multiAttendances;

	public MultiAttendances(List<ShiftResult> multiAttendances) {
		this.multiAttendances = multiAttendances;
	}

}
