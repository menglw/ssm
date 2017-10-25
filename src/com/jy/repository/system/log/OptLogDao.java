package com.jy.repository.system.log;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy.common.mybatis.Page;
import com.jy.entity.system.log.OptLog;
import com.jy.repository.base.JYBatis;
@JYBatis
public interface OptLogDao {

	public List<OptLog> findByPage(@Param("param")OptLog o,Page<OptLog> page);
	
	public void insert(OptLog o);
	
	public void deleteBatch(List<String> os);
}
