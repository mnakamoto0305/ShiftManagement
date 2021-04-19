package com.masahiro.nakamoto.domain;

import lombok.Data;

/**
 * 支払予定額情報を表現するオブジェクト
 */
@Data
public class Accounting {

	/**
	 * 単価
	 */
	private String dailyWages;

	/**
	 * 経費
	 */
	private String monthlyExpenses;

	/**
	 * 支払額
	 */
	private String profit;

}
