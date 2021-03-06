package com.masahiro.nakamoto.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

/**
 * 日付処理を行うサービス
 */
@Service
public class DateService {

	/**
	 * 今日の日時を曜日付きで取得
	 *
	 * @return
	 */
	public String getToday() {
		LocalDate ld = LocalDate.now();

		DateTimeFormatter formmater = DateTimeFormatter.ofPattern("yyyy年MM月dd日(E)", Locale.JAPANESE);

		String today = ld.format(formmater);

		return today;
	}

	/**
	 * yy/MM/dd形式の日付に曜日情報を追加
	 *
	 * @param date
	 * @return
	 */
	public String convertDate(LocalDate date) {
		DateTimeFormatter formmater = DateTimeFormatter.ofPattern("MM月dd日(E)", Locale.JAPANESE);

		String convertedDate = date.format(formmater);

		return convertedDate;
	}

	/**
	 * 今の月を返す
	 */
	public String convertDate() {
		LocalDate ld = LocalDate.now();

		DateTimeFormatter formmater = DateTimeFormatter.ofPattern("MM");

		String month = ld.format(formmater);

		return month;
	}

	/**
	 * 来月の日数を返す
	 */
	public int getNextMonthNum() {
		LocalDate thisMonth = LocalDate.now().plusMonths(1);

		int monthNum = thisMonth.lengthOfMonth();

		return monthNum;
	}

	/**
	 * 今月の日数を返す
	 */
	public int getThisMonthNum() {
		LocalDate thisMonth = LocalDate.now();

		int monthNum = thisMonth.lengthOfMonth();

		return monthNum;
	}

}
