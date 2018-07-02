package com.xq.live.service.impl;

import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.ShopEnterService;
import com.xq.live.vo.in.ShopCashierInVo;
import com.xq.live.vo.out.ShopEnterOut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Autowired
    private ShopAllocationMapper shopAllocationMapper;

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
        List<ShopEnterOut> list = shopEnterMapper.selectByUserId(userId);
        if(list==null||list.size()==0){
            return null;
        }

        return list.get(list.size()-1);//  返回最后一条数据,前面的数据无需做判断
    }

    @Override
    @Transactional
    public Integer addShop(ShopEnter shopEnter) {
        List<ShopEnterOut> list = shopEnterMapper.selectByUserId(shopEnter.getUserId());
        if(list==null||list.size()==0){
            return null;
        }
        ShopEnterOut shopEnterOut = list.get(list.size()-1);//返回最后一条数据,前面的数据无需做判断
        ShopEnter shopEnter1 = new ShopEnter();
        shopEnter1.setId(shopEnterOut.getId());
        shopEnter1.setStatus(ShopEnter.SHOP_ENTER_CAN);
        shopEnterMapper.updateByPrimaryKeySelective(shopEnter1);

        shopEnterOut = shopEnterMapper.selectByPrimaryKey(shopEnterOut.getId());
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
            boolean flag = isMobile(mobile);//判断是否是手机号
            if(flag==true){
                shop.setMobile(mobile);
            }else{
                shop.setPhone(mobile);
            }
            shop.setShopName(shopName);
            shop.setUserId(userId);
            shop.setBusinessCate(businessCate);
            shop.setLogoUrl(shopEnterOut.getLogoPic());
            shop.setIndexUrl(shopEnterOut.getDoorPic());
            shop.setShopHours(shopEnterOut.getShopHours());
            shop.setOtherService(shopEnterOut.getOtherService());
            shop.setCity(shopEnterOut.getCity());
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
            User user1 = userMapper.selectByPrimaryKey(userId);
            //给商家加入管理员权限
            ShopCashierInVo shopCashierInVo = new ShopCashierInVo();
            shopCashierInVo.setCashierId(user1.getId());
            shopCashierInVo.setCashierName(user1.getUserName());
            shopCashierInVo.setShopId(id);
            shopCashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_IS_ADMIN);
            shopCashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);
            shopCashierMapper.insert(shopCashierInVo);

            //给商家配置收款方式，先配置已删除,为平台代收的数据，后期可以根据业务来修改
            ShopAllocation shopAllocation = new ShopAllocation();
            shopAllocation.setShopId(id);
            shopAllocation.setIsDelete(ShopAllocation.SHOP_ALLOCATION_IS_DELETED);
            shopAllocation.setPaymentMethod(ShopAllocation.SHOP_ALLOCATION_DS);
            shopAllocationMapper.insert(shopAllocation);
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

    @Override
    public Shop searchByShopName(String shopName) {
        Shop shop = shopMapper.selectByShopName(shopName);
        return shop;
    }

    /**
     * 判断是否是手机号
     * @param str
     * @return
     */
    public boolean isMobile(String str){
        String regExp = "^[1][3,4,5,7,8][0-9]{9}$";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(str);
        return m.find();
    }
}
