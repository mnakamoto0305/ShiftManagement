package com.masahiro.nakamoto.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 拠点情報を表現するオブジェクト
 */
@Data
@Component
public class Area {

	/**
	 * 拠点名
	 */
	private String name;

}
