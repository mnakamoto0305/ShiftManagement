package com.masahiro.nakamoto.domain.form;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 社員情報を検索するフォームオブジェクト
 */
@Data
@Component
public class EmployeeForm {

	/**
	 * 検索ワード
	 */
	private String searchWord;

}
