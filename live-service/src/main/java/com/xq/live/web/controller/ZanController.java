package com.xq.live.web.controller;/**
 * 点赞控制类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:10
 */

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Zan;
import com.xq.live.service.ZanService;
import com.xq.live.vo.in.ZanInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 点赞控制类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:10
 **/
@RestController
@RequestMapping(value = "/zan")
public class ZanController {

    @Autowired
    private ZanService zanService;
    /**
     * 查询一条记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Zan getZanById(@PathVariable(value = "id") Long id) {
        return new Zan();
    }

    /**
     * 新增一条记录
     *
     * @param zan
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid Zan zan, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = zanService.add(zan);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 取消赞
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResp<Integer> delete(Long id){
        if(id == null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty_id);
        }
        int res = zanService.delete(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, res);
    }
    /*public BaseResp<Integer> delete(@Valid ZanInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), 0);
        }
        int res = zanService.delete(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, res);
    }*/

}
