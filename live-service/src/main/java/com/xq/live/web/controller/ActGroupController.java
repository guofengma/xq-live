package com.xq.live.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActGroup;
import com.xq.live.service.ActGroupService;
import com.xq.live.vo.in.ActGroupInVo;
import com.xq.live.vo.out.ActGroupOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2018/5/2.
 */
@RestController
@RequestMapping("/actGroup")
public class ActGroupController {

    @Autowired
    private ActGroupService actGroupService;

    /**
     * 查询全部参加活动的小组列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/selectOutAll",method = RequestMethod.GET)
    public BaseResp<List<ActGroupOut>> selectOutAll(ActGroupInVo inVo){
        List<ActGroupOut> result = actGroupService.top(inVo);
        return new BaseResp<List<ActGroupOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据指定条件查询参加活动的小组列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/selectByOutID",method = RequestMethod.GET)
    public BaseResp<List<ActGroupOut>> selectByOutID(ActGroupInVo inVo){
        List<ActGroupOut> result = actGroupService.listForID(inVo);
        return new BaseResp<List<ActGroupOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 批量插入参加活动的小组信息
     * @param list
     * @return
     */
    @RequestMapping(value = "/addListGroup",method = RequestMethod.POST)
    public BaseResp<Integer> addListGroup(@RequestBody List<ActGroup> list) throws ParseException {

        if(list==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //前台无需分组
/*        for (int index=0;index<list.size();index++){
            if (list.get(index).getUserId()==null||list.get(index).getShopId()==null){
                return new BaseResp<Integer>(ResultStatus.error_param_empty);
            }
        }*/
        //失败返回0成功返回1
        int flas=actGroupService.addListGroup(list);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,flas);
    }

    //删除
    @RequestMapping(value = "/updateByID",method = RequestMethod.POST)
    public BaseResp<Long> updateByID(ActGroup actGroup){
        if(actGroup==null||actGroup.getActId()==null){
            return null;
        }
        Long id=actGroupService.updateID(actGroup);
        return new BaseResp<Long>(ResultStatus.SUCCESS,id);
    }

}
