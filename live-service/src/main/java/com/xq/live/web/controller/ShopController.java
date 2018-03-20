package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Shop;
import com.xq.live.model.User;
import com.xq.live.service.CountService;
import com.xq.live.service.ShopService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.out.ShopOut;
import com.xq.live.web.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 商家controller
 *
 * @author zhangpeng32
 * @create 2018-01-17 18:01
 **/
@RestController
@RequestMapping(value = "/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserService userService;

    @Autowired
    private CountService countService;

    /**
     * 根据商家id查询商家信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<ShopOut> getShopById(@PathVariable(value = "id") Long id) {
        countService.topicHits(id);
        ShopOut result = shopService.findShopOutById(id);
        return new BaseResp<ShopOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 进入商家详细页
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public BaseResp<Shop> detail(@Valid ShopInVo inVo, BindingResult result, HttpServletRequest request){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Shop>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        inVo.setUserIp(IpUtils.getIpAddr(request));
        Shop shop = shopService.detail(inVo);
        return new BaseResp<Shop>(ResultStatus.SUCCESS, shop);
    }

    /**
     * 新增一条商家记录
     *
     * @param shop
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid Shop shop, BindingResult result) {
        //必填参数校验
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        //检查用户id是否正确
        User user = userService.getUserById(shop.getUserId());
        if(user == null){
            return new BaseResp<Long>(ResultStatus.error_input_user_id);
        }

        //该用户是否已经入驻，如果存在记录不允许再入驻
        Shop s = shopService.getShopByUserId(user.getId());
        if(s != null){
            return new BaseResp<Long>(ResultStatus.error_user_shop_exist);
        }

        Long id = shopService.addShop(shop);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 删除一条商家记录
     *
     * @param id
     * @return
     */
/*    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public BaseResp<Integer> delete(@PathVariable(value = "id") Long id) {
        int result = shopService.deleteShopById(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }*/

    /**
     * 更新一条商家记录
     *
     * @param shop
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(Shop shop) {
        int result = shopService.updateShop(shop);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据条件查询商家列表信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<ShopOut>> list(ShopInVo inVo, HttpServletRequest request){
        Pager<ShopOut> result = shopService.list(inVo);
        return new BaseResp<Pager<ShopOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 排序查热门商家——后续优化
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<ShopOut>> top(ShopInVo inVo){
        List<ShopOut> result = shopService.top(inVo);
        return new BaseResp<List<ShopOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据用户id（userId）查询商家信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getByUserId/{userId}", method = RequestMethod.GET)
    public BaseResp<Shop> getShopByUserId(@PathVariable(value = "userId") Long userId) {
        Shop result = shopService.getShopByUserId(userId);
        return new BaseResp<Shop>(ResultStatus.SUCCESS, result);
    }
}
