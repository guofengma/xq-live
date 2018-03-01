package com.xq.live.service.impl;/**
 * 商家sevice实现类
 *
 * @author zhangpeng32
 * @create 2018-01-17 17:57
 */

import com.xq.live.common.Pager;
import com.xq.live.dao.AccessLogMapper;
import com.xq.live.dao.ShopMapper;
import com.xq.live.model.AccessLog;
import com.xq.live.model.Shop;
import com.xq.live.model.User;
import com.xq.live.service.ShopService;
import com.xq.live.vo.in.ShopInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商家sevice实现类
 *
 * @author zhangpeng32
 * @create 2018-01-17 17:57
 **/
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;


    @Autowired
    private AccessLogMapper accessLogMapper;

    @Override
    public Shop getShopById(Long id) {
        return shopMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long addShop(Shop shop) {
        int r = shopMapper.insert(shop);
        if(r < 1){
            return null;
        }
        return shop.getId();
    }

    /**
     * 更新一条商家记录
     *
     * @param shop
     * @return
     */
    @Override
    public int updateShop(Shop shop) {
        return shopMapper.updateByPrimaryKeySelective(shop);
    }

    /**
     * 根据id删除一条商家记录
     *
     * @param id
     * @return
     */
    @Override
    public int deleteShopById(Long id) {
        return shopMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Pager<Shop> list(ShopInVo inVo){
        Pager<Shop> result = new Pager<Shop>();
        int listTotal = shopMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if(listTotal > 0){
            List<Shop> list = shopMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<Shop> top(ShopInVo inVo){
        return shopMapper.list(inVo);
    }

    @Override
    public Shop detail(ShopInVo inVo) {
        /**
         * 1、查询用户是否存在访问记录
         * 2、记录用户访问日志
         */
        AccessLog accessLog = new AccessLog();
        accessLog.setUserId(inVo.getUserId());
        accessLog.setUserName(inVo.getUserName());
        accessLog.setUserIp(inVo.getUserIp());
        accessLog.setSource(inVo.getSourceType());
        accessLog.setRefId(inVo.getId());
        accessLog.setBizType(AccessLog.BIZ_TYPE_SHOP_VIEW);
        int cnt = accessLogMapper.checkRecordExist(accessLog);
        if(cnt == 0){
            try {
                int logCnt = accessLogMapper.insert(accessLog);
                if(logCnt > 0){
                    shopMapper.updatePopNum(inVo.getId());  //增加人气数值
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //3、根据id查询商家信息
        Shop shop = shopMapper.selectByPrimaryKey(inVo.getId());
        return shop;
    }
}
