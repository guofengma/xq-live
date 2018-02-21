package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActInfo;
import com.xq.live.model.User;
import com.xq.live.service.ActInfoService;
import com.xq.live.vo.in.ActInfoInVo;
import com.xq.live.vo.in.UserInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 平台活动controller
 * @author zhangpeng32
 * @create 2018-02-07 17:14
 **/
@RestController
@RequestMapping(value = "/act")
public class ActInfoController {

    @Autowired
    private ActInfoService actInfoService;

    /**
     * 根据id查询一条活动记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<ActInfo> get(@PathVariable("id") Long id){
        ActInfo actInfo = actInfoService.selectOne(id);
        return new BaseResp<ActInfo>(ResultStatus.SUCCESS, actInfo);
    }


    /**
     * 新增记录
     * @param actInfo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> addUser(@Valid ActInfo actInfo, BindingResult result){
        //1、必填参数校验
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id  = actInfoService.add(actInfo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 更新
     * @param actInfo
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(ActInfo actInfo){
        Integer result = actInfoService.update(actInfo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<ActInfo>> userList(ActInfoInVo inVo){
        Pager<ActInfo> result = actInfoService.list(inVo);
        return new BaseResp<Pager<ActInfo>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询热门活动
     * @param inVo  page 为页数 rows 为展示行数
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<ActInfo>> findHotActInfos(ActInfoInVo inVo){
        List<ActInfo> list = actInfoService.top(inVo);
        return new BaseResp<List<ActInfo>>(ResultStatus.SUCCESS, list);
    }
}
