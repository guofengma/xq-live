package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Coupon;
import com.xq.live.model.Shop;
import com.xq.live.model.SoWriteOff;
import com.xq.live.model.User;
import com.xq.live.service.CouponService;
import com.xq.live.service.ShopService;
import com.xq.live.service.SoWriteOffService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.out.SoWriteOffOut;
import com.xq.live.web.utils.CutOutTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author lipeng
 * @date 2018-02-21 18:18
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/app/hx")
public class SoWriteOffForAppController {

    @Autowired
    private SoWriteOffService soWriteOffService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CouponService couponService;

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<SoWriteOff> get(@PathVariable("id") Long id) {
        SoWriteOff soWriteOff = soWriteOffService.get(id);
        return new BaseResp<SoWriteOff>(ResultStatus.SUCCESS, soWriteOff);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid SoWriteOff soWriteOff, BindingResult result) {
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //1、参数校验--验证券是否被核销过
        Coupon coupon = couponService.get(soWriteOff.getCouponId());
        if(coupon == null || coupon.getIsUsed() == Coupon.COUPON_IS_USED_YES){
            return new BaseResp<Long>(ResultStatus.error_coupon_is_used);
        }

        //验证扫码人id
        User cashier = userService.getUserById(soWriteOff.getCashierId());
        if(cashier == null){
            return new BaseResp<Long>(ResultStatus.error_para_cashier_id);
        }

        //验证账号类型：商家账号
        if(cashier.getUserType() != User.USER_TYPE_SJ){
            return new BaseResp<Long>(ResultStatus.error_para_cashier_user_type);
        }

        //验证商家信息
        if(cashier.getShopId() == null){
            return new BaseResp<Long>(ResultStatus.error_para_user_shop_id);
        }
        Shop shop = shopService.getShopByUserId(cashier.getId());
        if(shop ==  null){
            return new BaseResp<Long>(ResultStatus.error_shop_info_empty);
        }



        soWriteOff.setShopId(shop.getId());
        soWriteOff.setShopName(shop.getShopName());
        soWriteOff.setPaidAmount(soWriteOff.getShopAmount().subtract(soWriteOff.getCouponAmount()));
        //2、核销抵用券
        Long id = soWriteOffService.add(soWriteOff);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 查询每个商家核销的票卷的信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<SoWriteOffOut>> list(SoWriteOffInVo inVo){
        if(inVo==null||inVo.getShopId()==null||inVo.getBegainTime()==null||inVo.getEndTime()==null){
            return new BaseResp<Pager<SoWriteOffOut>>(ResultStatus.error_param_empty);
        }
        Pager<SoWriteOffOut> list = soWriteOffService.list(inVo);
        return new BaseResp<Pager<SoWriteOffOut>>(ResultStatus.SUCCESS,list);
    }
    /**
     * 返回时间段内各个月份的金额
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listAmount",method = RequestMethod.GET)
    public BaseResp<Map<Integer,SoWriteOffOut>> listAmount(SoWriteOffInVo inVo){
        SoWriteOffInVo offInVo=null;
        //获取分开好的月份
        List<SoWriteOffInVo> listInVo= CutOutTimeUtils.getValueForDate(inVo);
        if (listInVo.size()==0||listInVo==null){
            return new BaseResp<Map<Integer,SoWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        Map<Integer,SoWriteOffOut> map = new HashMap<Integer,SoWriteOffOut>();

        for (int i=0;i<listInVo.size();i++){
            //可以将没有记录的月份不返回
            /*if (soWriteOffService.listAmount(listInVo.get(i)).get(0)!=null){
                map.put(i+1, soWriteOffService.listAmount(listInVo.get(i)).get(0));
            }*/
            map.put(i+1, soWriteOffService.listAmount(listInVo.get(i)).get(0));
        }
        if (map==null){
            return new BaseResp<Map<Integer,SoWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        return new BaseResp<Map<Integer,SoWriteOffOut>>(ResultStatus.SUCCESS,map);
    }

}
