package com.masahiro.nakamoto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.domain.Employee;
import com.masahiro.nakamoto.mybatis.EmployeeMapper;
import com.masahiro.nakamoto.mybatis.UserMapper;

@Service
public class EmployeeService {

	@Autowired
	EmployeeMapper employeeMapper;

	@Autowired
	UserMapper userMapper;

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

	@Transactional
	public void deleteEmployee(String id) {
		employeeMapper.deleteEmployee(id);
		userMapper.deleteEmployee(id);
	}

}
