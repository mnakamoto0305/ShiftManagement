package com.masahiro.nakamoto.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ShiftForm {


	/**
	 * 拠点ID
	 */
	private Integer area;

	/**
	 * コースID
	 */
	private Integer courseId;

	/**
	 * ログインID
	 */
	private String id;

	/**
	 * シフト検索に利用
	 */
	private LocalDate date;
	private String year;
	private String month;
	private LocalDate first;
	private LocalDate last;

}
