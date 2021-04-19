package com.masahiro.nakamoto.domain.shift;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 代走ドライバーの情報を表現するオブジェクト
 */
@Data
@Component
public class Substitute {

	/**
	 * 代走ドライバーのリスト
	 */
	private List<Integer> driverList;

}
