package com.masahiro.nakamoto.domain.shift;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.Data;

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
	private String year;
	private String month;
	private LocalDate first;
	private LocalDate last;

}
