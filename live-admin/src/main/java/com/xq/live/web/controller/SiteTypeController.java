package com.xq.live.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xq.live.service.SiteTypeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xq.live.model.SiteType;
import com.xq.live.utils.HtmlUtil;
import com.xq.live.vo.SiteTypeInVo;
 
@Controller
@RequestMapping("/siteType") 
public class SiteTypeController extends BaseController{
	
	private final static Logger log= Logger.getLogger(SiteTypeController.class);
	
	@Autowired
	private SiteTypeService siteTypeService;
	
	/**
	 * ilook 首页
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/list") 
	public ModelAndView  list(SiteTypeInVo model,HttpServletRequest request) throws Exception{
		Map<String,Object>  context = new HashMap<String, Object>();
		return forword("siteMain/siteType",context); 
	}
	
	/**
	 * json 列表页面
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/typeListJson") 
	public void  typeListJson(HttpServletResponse response) throws Exception{
		List<SiteType> dataList = siteTypeService.queryByAll();
		HtmlUtil.writerJson(response, dataList);
	}
	
	
	/**
	 * json 列表页面
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/dataList") 
	public void  dataList(SiteTypeInVo model,HttpServletResponse response) throws Exception{
		List<SiteType> dataList = siteTypeService.queryWithPg(model);
		//设置页面数据
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("total",model.getPager().getRowCount());
		jsonMap.put("rows", dataList);
		HtmlUtil.writerJson(response, jsonMap);
	}
	

	
	/**
	 * 添加或修改数据
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/save")
	public void save(SiteType bean,HttpServletResponse response) throws Exception{
		if(bean.getId() == null){
			siteTypeService.add(bean);
		}else{
			siteTypeService.update(bean);
		}
		sendSuccessMessage(response, "保存成功~");
	}
	
	@RequestMapping("/getId")
	public void getId(Integer id,HttpServletResponse response) throws Exception{
		Map<String,Object>  context = new HashMap();
		SiteType bean  = siteTypeService.queryById(id);
		if(bean  == null){
			sendFailureMessage(response, "没有找到对应的记录!");
			return;
		}
		context.put(SUCCESS, true);
		context.put("data", bean);
		HtmlUtil.writerJson(response, context);
	}
	
	
	
	@RequestMapping("/delete")
	public void delete(Integer[] id,HttpServletResponse response) throws Exception{
		siteTypeService.delete(id);
		sendSuccessMessage(response, "删除成功");
	}
	
}
