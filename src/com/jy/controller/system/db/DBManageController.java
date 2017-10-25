package com.jy.controller.system.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jy.common.ajax.AjaxRes;
import com.jy.common.mybatis.Page;
import com.jy.common.utils.base.Const;
import com.jy.common.utils.db.DBUtil;
import com.jy.common.utils.db.TColumn;
import com.jy.controller.base.BaseController;
/**
 * 数据库管理
 */
@Controller
@RequestMapping("/backstage/db/dbManage/")
public class DBManageController extends BaseController<Object>{

	private static final String SECURITY_URL ="/backstage/db/dbManage/index";
	
	private static final String DATA_SECURITY_URL ="/backstage/db/dbManage/dbData";
	
	private static final String STRUCTURE_SECURITY_URL ="/backstage/db/dbManage/dbStructure";
	/**
	 * 数据库管理页面
	 */
	@RequestMapping("index")
	public String index(Model model) {	
		if(doSecurityIntercept(Const.RESOURCES_TYPE_MENU)){
			model.addAttribute("permitBtn", getPermitBtn(Const.RESOURCES_TYPE_FUNCTION));
			return "system/db/dbManage/list";
		}
		return Const.NO_AUTHORIZED_URL;
	}
	
	@RequestMapping(value="findAllTables", method=RequestMethod.POST)
	@ResponseBody
	public AjaxRes findAllTables(){
		AjaxRes ar=getAjaxRes();	
		if(ar.setNoAuth(doSecurityIntercept(Const.RESOURCES_TYPE_MENU,SECURITY_URL))){
			try {	
				Map<String, Object> p=new HashMap<String, Object>();
				List<String> result=DBUtil.getTables();					
				p.put("permitBtn",getPermitBtn(Const.RESOURCES_TYPE_BUTTON));
				p.put("list",result);
				ar.setSucceed(p);
			} catch (Exception e) {
				logger.error(e.toString(),e);
				ar.setFailMsg(Const.DATA_FAIL);
			}	
		}
		return ar;
	}
	
	@RequestMapping("dbData")
	public String dbData(Model model,String tName) {	
		if(doSecurityIntercept(Const.RESOURCES_TYPE_BUTTON)){
			model.addAttribute("tName", tName);
			return "system/db/dbManage/dbData";
		}
		return Const.NO_AUTHORIZED_URL;
	}
	
	@RequestMapping(value="getData", method=RequestMethod.POST)
	@ResponseBody
	public AjaxRes getData(Page<List<Object>> page,String tName){
		AjaxRes ar=getAjaxRes();	
		if(ar.setNoAuth(doSecurityIntercept(Const.RESOURCES_TYPE_BUTTON,DATA_SECURITY_URL))){
			try {	
				Map<String, Object> p=new HashMap<String, Object>();			
				if(StringUtils.isNotBlank(tName)){
					Page<List<Object>> result=DBUtil.queryByTableName(tName,page);	
					p.put("list",result);
					ar.setSucceed(p);
				}else{
					ar.setFailMsg(Const.DATA_FAIL);
				}							
			} catch (Exception e) {
				logger.error(e.toString(),e);
				ar.setFailMsg(Const.DATA_FAIL);
			}	
		}
		return ar;
	}
	
	/**
	 * 表结构
	 * @param model
	 * @return
	 */
	@RequestMapping("dbStructure")
	public String dbStructure(Model model,String tName) {	
		if(doSecurityIntercept(Const.RESOURCES_TYPE_BUTTON)){
			model.addAttribute("tName", tName);
			return "system/db/dbManage/dbStructure";
		}
		return Const.NO_AUTHORIZED_URL;
	}
	
	@RequestMapping(value="getStructure", method=RequestMethod.POST)
	@ResponseBody
	public AjaxRes getStructure(String tName){
		AjaxRes ar=getAjaxRes();	
		if(ar.setNoAuth(doSecurityIntercept(Const.RESOURCES_TYPE_BUTTON,STRUCTURE_SECURITY_URL))){
			try {	
				Map<String, Object> p=new HashMap<String, Object>();
				if(StringUtils.isNotBlank(tName)){
					List<TColumn> result=DBUtil.getTableColumns("select * from "+tName);	
					p.put("list",result);
					ar.setSucceed(p);
				}else{
					ar.setFailMsg(Const.DATA_FAIL);
				}
			} catch (Exception e) {
				logger.error(e.toString(),e);
				ar.setFailMsg(Const.DATA_FAIL);
			}	
		}
		return ar;
	}
}
