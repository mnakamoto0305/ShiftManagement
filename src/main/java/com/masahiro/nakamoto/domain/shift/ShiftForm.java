package com.masahiro.nakamoto.domain.shift;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * シフト検索のフォームオブジェクト
 */
@Data
@Component
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

	/**
	 * 年
	 */
	private String year;

	/**
	 * 月
	 */
	private String month;

	/**
	 * 月初の日付
	 */
	private LocalDate first;

	/**
	 * 月末の日付
	 */
	private LocalDate last;

}
