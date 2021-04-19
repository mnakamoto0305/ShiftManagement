package com.masahiro.nakamoto.domain.shift;

import org.springframework.stereotype.Component;

import com.masahiro.nakamoto.domain.holiday.MultiAttendances;

import lombok.Data;

/**
 * 作成済みのシフト情報を表現するオブジェクト
 */
@Data
@Component
public class ShiftConfirm {

	/**
	 * 通常コースのシフト
	 */
	private MultiAttendances courseAttendances;

	/**
	 * 代走ドライバーのシフト
	 */
	private MultiAttendances substituteAttendances;

	/**
	 * 引数なしコンストラクタ
	 */
	public ShiftConfirm() {
		super();
	}

	/**
	 * コンストラクタ
	 *
	 * @param courseAttendances
	 * @param substituteAttendances
	 */
	public ShiftConfirm(MultiAttendances courseAttendances, MultiAttendances substituteAttendances) {
		this.courseAttendances = courseAttendances;
		this.substituteAttendances = substituteAttendances;
	}


}
