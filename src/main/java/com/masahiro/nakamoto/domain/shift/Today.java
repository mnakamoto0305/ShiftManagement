package com.masahiro.nakamoto.domain.shift;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 指定した日・拠点・コースの勤怠情報を表現するオブジェクト
 */
@Data
@Component
public class Today {

	/**
	 * 勤怠情報
	 */
	private boolean isAttendance;

	/**
	 * 拠点ID
	 */
	private int areaId;

	/**
	 * コースID
	 */
	private int courseId;

	/**
	 * 拠点名
	 */
	private String areaName;

}
