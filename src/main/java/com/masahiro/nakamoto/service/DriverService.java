package com.masahiro.nakamoto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.AlreadyRegisteredException;
import com.masahiro.nakamoto.IllegalCourseException;
import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.domain.form.DriverForm;
import com.masahiro.nakamoto.domain.form.InfomationForm;
import com.masahiro.nakamoto.domain.form.PassChangeConfirmForm;
import com.masahiro.nakamoto.mybatis.CourseMapper;
import com.masahiro.nakamoto.mybatis.DriverMapper;
import com.masahiro.nakamoto.mybatis.ShiftMapper;
import com.masahiro.nakamoto.mybatis.UserMapper;

/**
 * ドライバー情報に関する処理を行うサービス
 */
@Service
public class DriverService {

	@Autowired
	DriverMapper driverMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	CourseMapper courseMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	ShiftMapper shiftMapper;

	/**
	 * 指定したIDのドライバー情報を取得
	 *
	 * @param id
	 * @return
	 */
	public Driver findDriverInfo(String id) {
		return driverMapper.findDriverInfo(id);
	}

	/**
	 * 指定した拠点のドライバー情報を取得
	 *
	 * @param areaId
	 * @return
	 */
	public List<Driver> findAreaDriver(int areaId) {
		return driverMapper.findAreaDriver(areaId);
	}

	/**
	 * 指定した拠点ID・コースIDのドライバー情報を取得
	 *
	 * @param areaId
	 * @param courseId
	 * @return
	 */
	public Driver findCourseDriver(int areaId, int courseId) {
		return driverMapper.findCourseDriver(areaId, courseId);
	}

	/**
	 * ドライバー情報を全件取得
	 */
	public List<Driver> findAll() {
		return driverMapper.findAll();
	}

	/**
	 * フォーム検索に合致するドライバー情報をリストで返す
	 */
	public List<Driver> findFromForm(DriverForm driverForm) {
		return driverMapper.findFromForm(driverForm);
	}

	/**
	 * 指定した拠点・コースのドライバー情報が既に登録されている場合に例外を送出する
	 *
	 * @param driver
	 * @return
	 * @throws Exception
	 */
	public void isRegistered(Driver driver) throws Exception {
		Driver dr = driverMapper.isRegistered(driver);
		if (dr != null) {
			throw new AlreadyRegisteredException();
		}
	}

	/**
	 * 登録するコース番号が不正なものであった場合に例外を送出する
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void isCorrectCourse(Driver driver) throws Exception {
		int areaId = driver.getAreaId();
		int courseId = driver.getCourseId();
		int maxCourse = courseMapper.findTotalCourses(areaId);
		if (courseId > maxCourse) {
			throw new IllegalCourseException();
		}
	}

	/**
	 * ドライバー情報を登録
	 *
	 * @param driver
	 */
	@Transactional
	public void createDriver(Driver driver) {
		driver.setPositionId(2);
		driverMapper.createDriver(driver);
		userMapper.createDriver(driver);
	}

	/**
	 * ドライバー情報を更新
	 *
	 * @param driver
	 */
	@Transactional
	public void updateDriver(Driver driver) {
		shiftMapper.updateId(driver.getPreviousId(), driver.getId());
		driverMapper.updateDriver(driver);
		userMapper.updateDriver(driver);
	}

	/**
	 * ドライバー情報を削除
	 *
	 * @param id
	 */
	@Transactional
	public void deleteDriver(String id) {
		driverMapper.deleteDriver(id);
		userMapper.deleteDriver(id);
	}

	/**
	 * 入力されたパスワードと登録済みのパスワードが一致するかを確認
	 *
	 * @param inputPassword
	 * @param id
	 * @return
	 */
	public boolean isCorrectPassword(String inputPassword, String id) {
		String dbPassword = userMapper.getPassword(id);
		return passwordEncoder.matches(inputPassword, dbPassword);
	}

	/**
	 * パスワードを変更
	 *
	 * @param passChangeConfirmForm
	 */
	@Transactional
	public void changePassword(PassChangeConfirmForm passChangeConfirmForm) {
		userMapper.changePassword(passChangeConfirmForm);
	}

	/**
	 * 個人ページから登録情報を更新
	 *
	 * @param infomationForm
	 */
	@Transactional
	public void updateInfomation(InfomationForm infomationForm) {
		shiftMapper.updateId(infomationForm.getPreviousId(), infomationForm.getId());
		userMapper.updateInfomation(infomationForm);
		driverMapper.updateInfomation(infomationForm);
	}

	/**
	 * 更新用の情報を取得
	 *
	 * @param id
	 * @return
	 */
	public InfomationForm getInfomation(String id) {
		return driverMapper.getInfomationForm(id);
	}

}
