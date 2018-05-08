package com.xq.live.service;

import com.xq.live.model.ActGroup;
import com.xq.live.vo.in.ActGroupInVo;
import com.xq.live.vo.out.ActGroupOut;
import com.xq.live.vo.out.ActShopOut;
import com.xq.live.vo.out.ActUserOut;

import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/5/2.
 */
public interface ActGroupService {

    /**
     * 查询所有活动列表信息
     * @param inVo
     * @return
     */
    List<ActGroupOut> top(ActGroupInVo inVo);

    /**
     * 根据ID查询活动列表信息
     * @param inVo
     * @return
     */
    List<ActGroupOut> listForID(ActGroupInVo inVo);

    /**
     * 添加活动列表信息
     * @param actGroup
     * @return
     */
    Long add(ActGroup actGroup);

    /**
     * 批量添加活动列表信息
     * @param list
     * @return
     */
    int addListGroup(List<ActGroup> list);

    /**
     * 根据ID删除活动列表信息
     * @param actGroup
     * @return
     */
    Long updateID(ActGroup actGroup);

    /**
     * 接收商家ID和用户列表
     * @param shopOuts,userOuts
     * @return
     */
    Map<Long,ActUserOut> mapList(List<Long> shopOuts,List<ActUserOut> userOuts);

    /**
     * 接收商家列表和用户列表
     * @param shopOuts,userOuts
     * @return
     */
    Map<ActShopOut,ActUserOut> map(List<ActShopOut> shopOuts,List<ActUserOut> userOuts);
}