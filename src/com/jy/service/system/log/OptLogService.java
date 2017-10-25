package com.jy.service.system.log;


import java.util.List;

import org.springframework.stereotype.Service;

import com.jy.common.mybatis.Page;
import com.jy.entity.system.log.OptLog;

@Service("OperateLogService")
public interface OptLogService {
	/**
	 * 记录日志
	 * @param optName
	 * @param method
	 * @param url
	 * @param ip
	 * @param userId
	 * @param args
	 */
	public void log(String optName,String method, String url,String ip,String userId,Object... args);
	/**
	 * 分页日志
	 * @param o
	 */
	public Page<OptLog> findByPage(OptLog o,Page<OptLog> page);	
	/**
	 * 批量删除日志
	 * @param o
	 */
	public void deleteBatch(List<String> os);
}
