package com.masahiro.nakamoto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.mybatis.PositionMapper;

/**
 * 社員とドライバーを判断するサービス
 */
@Service
public class PositionService {

	@Autowired
	PositionMapper positionMapper;

	/**
	 * 指定したIDが社員かドライバーかを確認
	 */
	public int findPosition(String id) {
		return positionMapper.findPosition(id);
	}

}
