package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.masahiro.nakamoto.domain.Employee;
import com.masahiro.nakamoto.domain.SiteUser;

@Mapper
public interface UserMapper {

	/**
	 * 入力されたIDからUser情報を取得
	 *
	 * @param id
	 * @return
	 */
	public SiteUser identifyUser(String id);

	/**
	 * 社員情報を登録
	 *
	 * @param employee
	 */
	public void createEmployee(Employee employee);

	/**
	 * 社員情報を更新
	 *
	 * @param employee
	 */
	public void updateEmployee(Employee employee);

	/**
	 * 社員情報を削除
	 *
	 * @param emoloyee
	 */
	public void deleteEmployee(String id);

}