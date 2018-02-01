package com.xq.live.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
/*	@Resource(name = "userService")
	private UserService userService;

	*//**
	 * <p>
	 * 列表页面
	 * <p>
	 * @param inVo
	 * <p>
	 * @param response
	 * <p>
	 * @throws Exception
	 * <p>
	 * User: Zhang Peng
	 * <p>
	 * Date: 2015年11月16日
	 *//*
	@RequestMapping("/dataList")
	public void dataList(UserInVo inVo, HttpServletResponse response) throws Exception {
		ServiceResult<List<User>> result = userService.queryWithPg(inVo);
		// 设置页面数据
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("total", result.getTotal());
		jsonMap.put("rows", result.getData());
		HtmlUtil.writerJson(response, jsonMap);
	}

	*//**
	 * 
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/list")
	public ModelAndView list(UserInVo inVo, HttpServletRequest request) throws Exception {
		Map<String, Object> context = new HashMap<String, Object>();
		return forword("user/user", context);
	}

	*//**
	 * <p>
	 * 根据id查询用户信息
	 * <p>
	 * User: Zhang Peng
	 * <p>
	 * Date: 2015年11月16日
	 *//*
	@RequestMapping("/getId")
	public void getId(UserInVo inVo, HttpServletResponse response) throws Exception {
		Map<String, Object> context = new HashMap<String, Object>();
		ServiceResult<User> result = userService.queryUserById(inVo);
		User user = result.getData();
		if (user == null) {
			sendFailureMessage(response, "没有找到对应的记录!");
			return;
		}
		context.put(SUCCESS, true);
		context.put("data", user);
		HtmlUtil.writerJson(response, context);
	}
	*//**
	 * <p> 保存用户信息
	 * <p> @param inVo
	 * <p> @param response
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 *//*
	@RequestMapping("/save")
	public void save(UserInVo inVo,HttpServletResponse response) throws Exception{
		if(inVo.getId() == null){
			userService.addUser(inVo);
		}else{
			userService.updateUser(inVo);
		}
		sendSuccessMessage(response, "保存成功~");
	}	
	*//**
	 * <p> 删除用户
	 * <p> @param ids
	 * <p> @param response
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 *//*
	@RequestMapping("/delete")
	public void delete(Long[] ids,HttpServletResponse response) throws Exception{
//		userService.delete(ids);
		sendSuccessMessage(response, "删除成功");
	}	*/
}
