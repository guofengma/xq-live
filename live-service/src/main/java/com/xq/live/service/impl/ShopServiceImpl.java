package com.xq.live.service.impl;/**
 * 商家sevice实现类
 *
 * @author zhangpeng32
 * @create 2018-01-17 17:57
 */

import com.xq.live.common.Pager;
import com.xq.live.dao.AccessLogMapper;
import com.xq.live.dao.ShopMapper;
import com.xq.live.dao.SkuMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.AccessLog;
import com.xq.live.model.Shop;
import com.xq.live.model.Sku;
import com.xq.live.model.User;
import com.xq.live.service.ShopService;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.ShopOut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Override
    public Shop getShopById(Long id) {
        return shopMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long addShop(Shop shop) {
        //保存记录到shop表
        int r = shopMapper.insert(shop);
        if(r < 1){
            return null;
        }

        //更新user表，升级为商家账号，记录商家id
        User user = new User();
        user.setUserType(User.USER_TYPE_SJ);
        user.setShopId(shop.getId());
        user.setId(shop.getUserId());
        userMapper.updateUserType(user);

        //返回商家主键
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
    public Pager<ShopOut> list(ShopInVo inVo){
        Pager<ShopOut> result = new Pager<ShopOut>();
        int listTotal = shopMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if(listTotal > 0){
            List<Shop> list = shopMapper.list(inVo);
            List<ShopOut> listForOut = new ArrayList<ShopOut>();
            for (Shop shop : list) {
                SkuInVo skuInVo = new SkuInVo();
                skuInVo.setShopId(shop.getId());
                skuInVo.setSkuType(Sku.SKU_TYPE_TSC);
                List<Sku> skus = skuMapper.queryTscList(skuInVo);
                ShopOut shopOut = new ShopOut();
                BeanUtils.copyProperties(shop,shopOut);
                if(skus.size()>0&&skus!=null){
                    shopOut.setSkuName(skus.get(0).getSkuName());
                }
                listForOut.add(shopOut);

            }
            /**
             * 根据综合排序 0 口味 1服务 2 人气
             */
            if(inVo!=null&&inVo.getBrowSort()==2){
                Collections.sort(listForOut);
            }
            result.setList(listForOut);

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

    @Override
    public Shop getShopByUserId(Long userId) {
        return shopMapper.getShopByUserId(userId);
    }
}
