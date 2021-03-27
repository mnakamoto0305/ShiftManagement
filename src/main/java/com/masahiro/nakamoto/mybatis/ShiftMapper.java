package com.masahiro.nakamoto.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.masahiro.nakamoto.domain.Attendance;
import com.masahiro.nakamoto.domain.ShiftForm;

@Mapper
public interface ShiftMapper {

	public List<Attendance> findAttendances(ShiftForm shiftForm);

	public boolean updateAttendances(@Param("attendancesList") List<Attendance> attendancesList);

}
