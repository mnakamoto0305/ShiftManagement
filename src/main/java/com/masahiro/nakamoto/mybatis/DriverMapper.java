package com.masahiro.nakamoto.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.form.DriverForm;
import com.masahiro.nakamoto.domain.form.InfomationForm;

/**
 * ドライバー情報に関するリポジトリ
 */
@Mapper
public interface DriverMapper {

	/**
	 * 指定したIDのドライバー情報を取得する
	 *
	 * @param id
	 * @return
	 */
	public Driver findDriverInfo(String id);

	/**
	 * 指定した拠点のドライバー情報を取得する
	 *
	 * @param areaId
	 * @return
	 */
	public List<Driver> findAreaDriver(int areaId);

	/**
	 * 指定した拠点ID・コースIDのドライバー情報を取得
	 *
	 * @param areaId
	 * @param courseId
	 * @return
	 */
	public Driver findCourseDriver(@Param("areaId") int areaId, @Param("courseId") int courseId);

	/**
	 * ドライバー情報を全件取得
	 *
	 * @return
	 */
	public List<Driver> findAll();

	/**
	 * フォーム検索
	 *
	 * @param employeeForm
	 * @return
	 */
	public List<Driver> findFromForm(DriverForm driverForm);

	/**
	 * 指定した拠点・コースのドライバー情報を取得する
	 *
	 * @param driver
	 * @return
	 */
	public Driver isRegistered(Driver driver);

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
	 * 個人ページから登録情報を更新
	 *
	 * @param infomationForm
	 */
	public void updateInfomation(InfomationForm infomationForm);

	/**
	 * 更新用の情報を取得
	 *
	 * @param id
	 * @return
	 */
	public InfomationForm getInfomationForm(String id);

}
