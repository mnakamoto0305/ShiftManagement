package com.masahiro.nakamoto.domain.form;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * ドライバー情報を検索するフォームオブジェクト
 */
@Data
@Component
public class DriverForm {

	/**
	 * 検索ワード
	 */
	private String searchWord;

	/**
	 * 拠点ID
	 */
	private int areaId;

}
