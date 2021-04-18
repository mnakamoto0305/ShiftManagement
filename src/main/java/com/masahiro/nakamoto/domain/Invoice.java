package com.masahiro.nakamoto.domain;

import lombok.Data;

/**
 * 請求書作成に必要な情報を表現するオブジェクト
 */
@Data
public class Invoice {

	/**
	 * ドライバー名
	 */
	private String name;

	/**
	 * 電話番号
	 */
	private String phoneNumber;

	/**
	 * メールアドレス
	 */
	private String email;

	/**
	 * 拠点名
	 */
	private String area;

	/**
	 * 出勤日数
	 */
	private int workingDays;

	/**
	 * 単価
	 */
	private int dailyWages;

	/**
	 * 経費
	 */
	private int monthlyExpenses;

	/**
	 * 売上
	 */
	private int amount;

	/**
	 * 利益
	 */
	private int earnings;

	/**
	 * 引数なしコンストラクタ
	 */
	public Invoice() {
		super();
	}

	/**
	 * コンストラクタ
	 *
	 * @param name
	 * @param phoneNumber
	 * @param email
	 * @param area
	 * @param workingDays
	 * @param dailyWages
	 * @param monthlyExpenses
	 * @param amount
	 * @param earnings
	 */
	public Invoice(String name, String phoneNumber, String email, String area, int workingDays, int dailyWages, int monthlyExpenses, int amount, int earnings) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.area = area;
		this.workingDays = workingDays;
		this.dailyWages = dailyWages;
		this.monthlyExpenses = monthlyExpenses;
		this.amount = amount;
		this.earnings = earnings;
	}

}
