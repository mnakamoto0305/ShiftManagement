package com.masahiro.nakamoto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.domain.Employee;
import com.masahiro.nakamoto.domain.form.EmployeeForm;
import com.masahiro.nakamoto.mybatis.EmployeeMapper;
import com.masahiro.nakamoto.mybatis.UserMapper;

@Service
public class EmployeeService {

	@Autowired
	EmployeeMapper employeeMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * 社員情報の全件検索
	 */
	@Transactional
	public List<Employee> findAll(){
		return employeeMapper.findAll();
	};

	/**
	 * 社員IDから社員情報を検索
	 *
	 * @param id
	 * @return
	 */
	@Transactional
	public Employee findEmployee(String id) {
		return employeeMapper.findEmployee(id);
	}

	@Transactional
	public List<Employee> findFromForm(EmployeeForm employeeForm) {
		return employeeMapper.findFromForm(employeeForm);
	}

	/**
	 * 社員情報を登録
	 *
	 * @param employee
	 */
	@Transactional
	public void createEmployee(Employee employee) {
		employee.setPositionId(1);
		employeeMapper.createEmployee(employee);
		userMapper.createEmployee(employee);
	}

	/**
	 * 社員情報を更新
	 *
	 * @param employee
	 */
	@Transactional
	public void updateEmployee(Employee employee) {
		employeeMapper.updateEmployee(employee);
		userMapper.updateEmployee(employee);
	}

	/**
	 * 指定したIdの社員情報を削除
	 *
	 * @param id
	 */
	@Transactional
	public void deleteEmployee(String id) {
		employeeMapper.deleteEmployee(id);
		userMapper.deleteEmployee(id);
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

}
