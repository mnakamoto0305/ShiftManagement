package com.masahiro.nakamoto.domain.holiday;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 休み希望日の登録フォームオブジェクト
 */
@Data
public class RegisterHolidayForm {

	/**
	 * 休み希望日
	 */
	@Size(min=87,max=87,message="休み希望は8日分入力してください")
	private String holiday;

	/**
	 * String型の休み希望日をLocalDate型に変換したリスト
	 */
	private List<LocalDate> holidayList;

}
