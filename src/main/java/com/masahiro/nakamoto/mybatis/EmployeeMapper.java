package com.masahiro.nakamoto.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.masahiro.nakamoto.domain.Employee;
import com.masahiro.nakamoto.domain.EmployeeForm;

@Mapper
public interface EmployeeMapper {

	/**
	 * 社員情報の全件検索
	 */
	public List<Employee> findAll();

	/**
	 * 社員情報の追加
	 */
	public void createEmployee(Employee employee);

	/**
	 * 社員IDから社員情報を検索
	 */
	public Employee findEmployee(String id);

	public List<Employee> findFromForm(EmployeeForm employeeForm);

	/**
	 * 社員情報を更新
	 *
	 * @param employee
	 */
	public void updateEmployee(Employee employee);

	/**
	 * 社員情報を削除
	 *
	 * @param employee
	 */
	public void deleteEmployee(String id);

}
