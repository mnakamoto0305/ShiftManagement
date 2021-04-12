package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.Employee;
import com.masahiro.nakamoto.domain.InfomationForm;
import com.masahiro.nakamoto.domain.PassChangeConfirmForm;
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

	/**
	 * ドライバー情報を登録
	 *
	 * @param driver
	 */
	public void createDriver(Driver driver);

	/**
	 * ドライバー情報を更新
	 *
	 * @param driver
	 */
	public void updateDriver(Driver driver);

	/**
	 * ドライバー情報を削除
	 *
	 * @param id
	 */
	public void deleteDriver(String id);

	/**
	 * 拠点IDとコースIDからドライバーのメールアドレスを取得
	 *
	 * @param areaId
	 * @param courseId
	 * @return
	 */
	public String getId(@Param("areaId") int areaId, @Param("courseId") int courseId);

	/**
	 * 指定したIDのパスワードを取得
	 *
	 * @param id
	 * @return
	 */
	public String getPassword(String id);

	/**
	 * パスワードを変更
	 *
	 * @param driver
	 */
	public void changePassword(PassChangeConfirmForm passChangeConfirmForm);

	/**
	 * 登録情報を更新
	 *
	 * @param infomationForm
	 */
	public void updateInfomation(InfomationForm infomationForm);

}