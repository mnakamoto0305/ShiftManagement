package com.masahiro.nakamoto.domain;

import lombok.Data;

@Data
public class Invoice {

	private String name;

	private String phoneNumber;

	private String email;

	private String area;

	private int workingDays;

	private int dailyWages;

	private int monthlyExpenses;

	private int amount;

	private int earnings;


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
