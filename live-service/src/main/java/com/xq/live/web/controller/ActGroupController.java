package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.ActShopMapper;
import com.xq.live.dao.ActUserMapper;
import com.xq.live.dao.SoWriteOffMapper;
import com.xq.live.model.ActGroup;
import com.xq.live.service.ActGroupService;
import com.xq.live.vo.in.ActGroupInVo;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.out.ActGroupOut;
import com.xq.live.vo.out.ActShopOut;
import com.xq.live.vo.out.ActUserOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

/**
 * Created by ss on 2018/5/2.
 */
@RestController
@RequestMapping("/actGroup")
public class ActGroupController {

    @Autowired
    private SoWriteOffMapper soWriteOffMapper;
    @Autowired
    private ActGroupService actGroupService;
    @Autowired
    private ActUserMapper actUserMapper;
    @Autowired
    private ActShopMapper actShopMapper;

    /**
     * 根据活动ID查询全部参加活动的小组列表
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
    public BaseResp<Integer> addListGroup(List<ActGroup> list) throws ParseException {

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

    //判断是否插入成功
    @Transactional
    public Integer addGroup(List<ActGroup> list){
        int flas=actGroupService.addListGroup(list);
        return flas;
    }

    //根据主键ID和活动ID删除小组信息
    @RequestMapping(value = "/updateByID",method = RequestMethod.POST)
    public BaseResp<Long> updateByID(ActGroup actGroup){
        if(actGroup==null||actGroup.getActId()==null){
            return null;
        }
        Long id=actGroupService.updateID(actGroup);
        return new BaseResp<Long>(ResultStatus.SUCCESS,id);
    }

    //根据活动ID将商家和用户进行匹配分组得到商家ID和用户(只是查询)
    @RequestMapping(value = "/groupList",method = RequestMethod.GET)
    public  BaseResp<Map<Long,ActUserOut>> mapList(Long actID){
        if (actID==null){
            return new BaseResp<Map<Long,ActUserOut>>(ResultStatus.error_param_empty);
        }
        ActUserInVo userInVo = new ActUserInVo();
        userInVo.setActId(actID);
        ActShopInVo shopInVo = new ActShopInVo();
        shopInVo.setActId(actID);
        //根据活动ID查询商家和用户信息
        List<ActShopOut> shopOuts= actShopMapper.listForNewAct(shopInVo);
        List<Long> shopID=new ArrayList<Long>();
        for (int i=0;i<shopOuts.size();i++){
            shopID.add(i, shopOuts.get(i).getShopId());
        }
        List<ActUserOut> userOuts= actUserMapper.listForNewAct(userInVo);
        Map<Long,ActUserOut> mapList=actGroupService.mapList(shopID, userOuts);
        return new BaseResp<Map<Long,ActUserOut>>(ResultStatus.SUCCESS,mapList);
    }

    //根据活动ID将商家和用户进行匹配分组(在查询后自动添加分组信息并且落选其他用户)
    @RequestMapping(value = "/groupListAll",method = RequestMethod.GET)
    public BaseResp<Map<ActShopOut,ActUserOut>> map(Long actID){
        if (actID==null){
            return new BaseResp<Map<ActShopOut,ActUserOut>>(ResultStatus.error_param_empty);
        }
        ActUserInVo userInVo = new ActUserInVo();
        userInVo.setActId(actID);
        ActShopInVo shopInVo = new ActShopInVo();
        shopInVo.setActId(actID);
        //根据活动ID查询商家和用户信息
        List<ActShopOut> shopOuts= actShopMapper.listForNewAct(shopInVo);
        List<ActUserOut> userOuts= actUserMapper.listForNewAct(userInVo);
        //判断用户和商家数量
        int sizeNum;
        if (shopOuts.size()>userOuts.size()){
            sizeNum=0;
            for (int i=userOuts.size()-1;i<shopOuts.size();i++){
                shopOuts.remove(i);
            }
        }else if (shopOuts.size()<userOuts.size()){
            for (int i=shopOuts.size()-1;i<userOuts.size();i++){
                userOuts.remove(i);
            }
            sizeNum=1;
        }else {
            sizeNum=2;
        }
        //接收分组好后的小组Map集合
        Map<ActShopOut,ActUserOut> mapList=actGroupService.map(shopOuts, userOuts);
        Set keySet = mapList.keySet();
        Iterator iterator = keySet.iterator();
        //获取分组后的商家和用户列表
        List<ActShopOut> shopList=new ArrayList<ActShopOut>();
        List<ActUserOut> userList=new ArrayList<ActUserOut>();

        List<ActGroup> groupList = new ArrayList<ActGroup>();

        while (iterator.hasNext()){
            Object k=iterator.next(); // key商家
            Object v=mapList.get(k);  //value用户
            shopList.add((ActShopOut) k);
            userList.add((ActUserOut) v);
        }

        for (int i=0;i<shopList.size();i++){
            ActGroup actGroup = new ActGroup();
            actGroup.setActId(shopList.get(i).getActId());
            actGroup.setShopId(shopList.get(i).getShopId());
            actGroup.setUserId(mapList.get(shopList.get(i)).getUserId());
            groupList.add(i,actGroup);
        }

        int s=updateState(userList,shopList,sizeNum);
        //根据i来判断是否更新成功
        if (s==0){
            return new BaseResp<Map<ActShopOut,ActUserOut>>(ResultStatus.error_act_update);
        }
        int addGroup=addGroup(groupList);
        if (addGroup==0){
            return new BaseResp<Map<ActShopOut,ActUserOut>>(ResultStatus.error_act_insert);
        }

            return new BaseResp<Map<ActShopOut,ActUserOut>>(ResultStatus.SUCCESS,mapList);
    }

    /**
     * 根据活动ID和入选小组人数落选小组(批量更新小组落选信息)
     * @param  //传入开始和结束时间，票券id
     * @return
     */
    @RequestMapping(value = "/updateLouXuan",method = RequestMethod.GET)
    public BaseResp<Integer> updateLuoXuan(Long actID,Integer length,Long skuId,Date begainTime,Date endTime){
        if (length==null||actID==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        SoWriteOffInVo inVo= new SoWriteOffInVo();
        inVo.setSkuId(skuId);
        inVo.setBegainTime(begainTime);
        inVo.setEndTime(endTime);

        //商家列表
        List<ActShopOut> listShop=actShopMapper.listByActId(actID);
        ActGroupInVo groupInVo=new ActGroupInVo();
        groupInVo.setActId(actID);

        for (int i=0;i<listShop.size();i++){
            inVo.setShopId(listShop.get(i).getShopId());
            groupInVo.setShopId(listShop.get(i).getShopId());
            //获取小组票数
           /* ActGroupOut out=actGroupService.listActByShop(groupInVo);
            Integer groupNum=out.getGroupVoteNum();*/
            Integer total = soWriteOffMapper.listTotal(inVo);
            groupInVo.setGroupVoteNum(total);
            int shop=actGroupService.updateGroupNum(groupInVo);
            if (shop!=1){
                return new BaseResp<Integer>(ResultStatus.error_group_list);
            }

        }
        //查看小组列表
        List<ActGroupOut> list=actGroupService.selectGroupOut();
        if (list==null||list.size()==0){
            return new BaseResp<Integer>(ResultStatus.error_group_list);
        }
        //获得入选小组人数列表
        //List<ActGroupOut> listOut=new ArrayList<ActGroupOut>();

        for (int i=list.size()-1;i>length-1;i--){
            list.remove(i);
        }
        //获得入选商家ID
        List<ActShopOut> shopList=new ArrayList<ActShopOut>();
        //获得入选选手ID
        List<ActUserOut> userList=new ArrayList<ActUserOut>();
        for (int i=0;i<list.size();i++){
            ActShopOut shopOut=new ActShopOut();
            ActUserOut userOut=new ActUserOut();
            shopOut.setShopId(list.get(i).getShopId());
            shopOut.setActId(list.get(i).getActId());
            userOut.setUserId(list.get(i).getUserId());
            userOut.setActId(list.get(i).getActId());
            shopList.add(i,shopOut);
            userList.add(i,userOut);
        }
        int s=actShopMapper.udateByLuoTwo(shopList);
        if (s==0){
            return new BaseResp<Integer>(ResultStatus.error_act_update);
        }
        int u=actUserMapper.udateByLuoXuanTwo(userList);
        if (u==0){
            return new BaseResp<Integer>(ResultStatus.error_act_update);
        }
        int i =actGroupService.updateByGroup(list);
        //1是成功0是失败
        if (i==1){
            return new BaseResp<Integer>(ResultStatus.SUCCESS,i);
        }
        return new BaseResp<Integer>(ResultStatus.error_act_update);
    }

    //查询小组信息，按票数排序
    @RequestMapping(value = "/selectGroupList",method = RequestMethod.GET)
    public BaseResp<List<ActGroupOut>> selectGroupList(){
        List<ActGroupOut> list = actGroupService.selectGroupOut();
        if (list==null||list.size()==0){
            return new BaseResp<List<ActGroupOut>>(ResultStatus.error_group_list);
        }
        return new BaseResp<List<ActGroupOut>>(ResultStatus.SUCCESS,list);
    }
    //批量更新商家和用户落选信息
    @Transactional
    public int updateState(List<ActUserOut> userOuts,List<ActShopOut> shopOuts,int i){
        int s;
        int o;
        if (i==0){
            s=actShopMapper.udateByLuo(shopOuts);
            if (s>0){
                return 1;
            }
        }else if (i==2){
            return 1;
        }else {
            o=actUserMapper.udateByLuoXuan(userOuts);
            if (o>0){
                return 1;
            }
        }
        //成功返回1失败返回0
        return 0;
    }




}
