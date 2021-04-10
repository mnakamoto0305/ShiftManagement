package com.masahiro.nakamoto.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.domain.Accounting;
import com.masahiro.nakamoto.domain.Driver;

@Service
public class AccountingService {

	/**
	 * ドライバーの個人ページに支払予定額を表示
	 *
	 * @param dailyWages
	 * @param monthlyExpenses
	 * @param workingDays
	 * @return
	 */
	public int getProfit(int dailyWages, int monthlyExpenses, int workingDays) {
		int profit = dailyWages * workingDays - monthlyExpenses;
		return profit;
	}

	/**
	 * 売上を算出
	 *
	 * @param dailyWages
	 * @param workingDays
	 * @return
	 */
	public int getEarnings(int dailyWages, int workingDays) {
		int earnings = dailyWages * workingDays;
		return earnings;
	}

	/**
	 * 指定した地区の支払額を算出
	 *
	 * @param driverList
	 * @param workingDaysList
	 * @return
	 */
	public List<Integer> getPayment(List<Driver> driverList, List<Integer> workingDaysList) {
		List<Integer> paymentList = new ArrayList<>();
		for (int i = 0; i < driverList.size(); i++) {
			int dailyWages = driverList.get(i).getDailyWages();
			int monthlyExpenses = driverList.get(i).getMonthlyExpenses();
			int workingDays = workingDaysList.get(i);
			paymentList.add(getProfit(dailyWages, monthlyExpenses, workingDays));
		}
		return paymentList;
	}

	/**
	 * 1,000円単位でカンマを表示
	 *
	 * @param number
	 * @return
	 */
	public String commaOf1000(int number) {
    	return String.format("%,d", number);
    }

	/**
	 * リストで受け取った数字をカンマに変換
	 *
	 * @param numberList
	 * @return
	 */
	public List<Accounting> commaOf1000(List<Driver> driverList, List<Integer> paymentList) {
		List<Accounting> list = new ArrayList<>();
		for (int i = 0; i < driverList.size(); i++) {
			Accounting accounting = new Accounting();
			String dailyWages = commaOf1000(driverList.get(i).getDailyWages());
			accounting.setDailyWages(dailyWages);
			String monthlyExpenses = commaOf1000(driverList.get(i).getMonthlyExpenses());
			accounting.setMonthlyExpenses(monthlyExpenses);
			if (paymentList.get(i) < 0) {
				accounting.setProfit("0");
			} else {
				String profit = commaOf1000(paymentList.get(i));
				accounting.setProfit(profit);
			}
			list.add(accounting);
		}
		return list;
	}

}
