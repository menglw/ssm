package com.jy.service.system.log;


import java.util.Date;
import java.util.List;

import org.apache.catalina.connector.ResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jy.common.mybatis.Page;
import com.jy.common.utils.JsonUtil;
import com.jy.common.utils.base.UuidUtil;
import com.jy.entity.system.log.OptLog;
import com.jy.repository.system.log.OptLogDao;

@Service("OperateLogService")
public class OptLogServiceImpl implements OptLogService {

	@Autowired
	private OptLogDao dao;

	@Override
	public void log(String optName,String method, String url,String ip,String userId,Object... args) {
		OptLog log=new OptLog();
		log.setId(UuidUtil.get32UUID());
		log.setOptName(optName);
		log.setMethod(method);
		log.setUrl(url);
		log.setIp(ip);
		log.setOptTime(new Date());
		log.setUserId(userId);
		if(args != null){
			Object[] argss = new Object[args.length];
			for(int i=0;i<args.length;i++){
				Object arg = args[i];
				
				if(arg instanceof MultipartFile || 
						arg instanceof org.apache.catalina.connector.Response ||
						arg instanceof ResponseFacade){
				}else{
					argss[i] = arg;
				}
			}
			String data = JsonUtil.toJson(argss);
			log.setData(data);
		}
		dao.insert(log);
	}

	@Override
	public Page<OptLog> findByPage(OptLog o, Page<OptLog> page) {
		page.setResults(dao.findByPage(o, page));
		return page;
	}

	@Override
	public void deleteBatch(List<String> os) {
		dao.deleteBatch(os);	
	}

	
}
