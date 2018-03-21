package com.xq.live.service.impl;

import com.xq.live.dao.ShopEnterMapper;
import com.xq.live.dao.ShopMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.Shop;
import com.xq.live.model.ShopEnter;
import com.xq.live.model.User;
import com.xq.live.service.ShopEnterService;
import com.xq.live.vo.out.ShopEnterOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 2018/3/8.
 */
@Service
public class ShopEnterServiceImpl implements ShopEnterService{
    @Autowired
    private ShopEnterMapper shopEnterMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Long add(ShopEnter shopEnter) {
        int res = shopEnterMapper.insert(shopEnter);
        if(res < 1){
            return null;
        }
        return shopEnter.getId();
    }

    @Override
    public ShopEnterOut searchByUserId(Long userId) {
        ShopEnterOut shopEnterOut = shopEnterMapper.selectByUserId(userId);
        return shopEnterOut;
    }

    @Override
    public Integer addShop(ShopEnter shopEnter) {
        ShopEnterOut shopEnterOut = shopEnterMapper.selectByUserId(shopEnter.getUserId());
        if(shopEnterOut!=null&&shopEnterOut.getStatus()!=null&&shopEnterOut.getStatus()==1){
            Shop shop = new Shop();
            Long userId = shopEnterOut.getUserId();
            String address = shopEnterOut.getAddress();
            String businessCate = shopEnterOut.getBusinessCate();
            String city = shopEnterOut.getCity();
            BigDecimal locationX = shopEnterOut.getLocationX();
            BigDecimal locationY = shopEnterOut.getLocationY();
            String mobile = shopEnterOut.getMobile();
            String shopName = shopEnterOut.getShopName();
            String userName = shopEnterOut.getUserName();
            shop.setAddress(address);
            shop.setLocationX(locationX);
            shop.setLocationY(locationY);
            boolean flag = isMobile(mobile);
            if(flag==true){
                shop.setMobile(mobile);
            }else{
                shop.setPhone(mobile);
            }
            shop.setShopName(shopName);
            shop.setUserId(userId);
            shop.setBusinessCate(businessCate);
            int insert = shopMapper.insert(shop);
            /**
             * 判断插入shop表是否成功，失败返回-2
             */
            if(insert<1){
                return -2;
            }
            Long id = null;
            if(shop.getId()!=null) {
                 id = shop.getId();//插入得到的shop_id
            }
            User user = new User();
            user.setId(userId);
            user.setShopId(id);
            user.setUserType(2);

            int i = userMapper.updateUserType(user);
            /**
             * 判断更改用户状态是否成功,失败返回-1
             */
            if(i<1){
                return -1;
            }
        }else {
            return -3;//判断用户是否入驻且审批通过
        }
        return 0;
    }

    @Override
    public ShopEnterOut selectByUserIdAndShopName(ShopEnter shopEnter) {
        ShopEnterOut shopEnterOut = shopEnterMapper.selectByUserIdAndShopName(shopEnter);
        return shopEnterOut;
    }

    /**
     * 先不做，按具体需求在来改
     */
    @Override
    public Integer deleteShopEnterForStatus(ShopEnter shopEnter) {
        /*ShopEnterOut out = shopEnterMapper.selectByUserId(shopEnter.getUserId());
        if(out==null){
            return -2;//商家还未入驻
        }
        if(out.getStatus()==2){

        }*/
        return null;
    }

    /**
     * 判断是否是手机号
     * @param str
     * @return
     */
    public boolean isMobile(String str){
        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(str);
        return m.find();
    }
}
