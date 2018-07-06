package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.PushMsg;
import com.xq.live.model.Shop;
import com.xq.live.model.User;
import com.xq.live.service.CountService;
import com.xq.live.service.ShopService;
import com.xq.live.service.SoService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.vo.out.ActShopByShopIdOut;
import com.xq.live.vo.out.ShopOut;
import com.xq.live.web.utils.GtPush;
import com.xq.live.web.utils.IpUtils;
import org.apache.log4j.Logger;
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
import java.util.Map;

/**
 * 商家controller
 *
 * @author zhangpeng32
 * @create 2018-01-17 18:01
 **/
@RestController
@RequestMapping(value = "/shop")
public class ShopController {

    private static Logger logger = Logger.getLogger(ShopController.class);

    @Autowired
    private SoService soService;

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
        Integer pops = countService.shopPops(id);
        ShopOut result = shopService.findShopOutById(id);
        result.setPopNum(pops);
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
     * 通过Code查询一条商家记录
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/getByCode", method = RequestMethod.GET)
    public BaseResp<ShopOut> getShopById(String code) {
        ShopOut result = shopService.getShopByCode(code);
        if (result==null){
            return new BaseResp<ShopOut>(ResultStatus.getError_allocation_selectList);
        }
        return new BaseResp<ShopOut>(ResultStatus.SUCCESS, result);
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
     * 根据条件查询首页各类热门餐厅
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listForHomePage", method = RequestMethod.GET)
    public BaseResp<Map<String, List<ShopOut>>> listForHomePage(ShopInVo inVo, HttpServletRequest request){
        Map<String, List<ShopOut>> result = shopService.listForHomePage(inVo);
        return new BaseResp<Map<String, List<ShopOut>>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据条件查询商家列表信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listForChuangXiang", method = RequestMethod.GET)
    public BaseResp<Pager<ShopOut>> listForChuangXiang(ShopInVo inVo, HttpServletRequest request){
        Pager<ShopOut> result = shopService.listForChuangXiang(inVo);
        return new BaseResp<Pager<ShopOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 排序查热门商家——后续优化
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<ShopOut>> top(ShopInVo inVo){
        inVo.setBrowSort(Shop.BROW_SORT_POP);//前期先通过人气排序
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

    /**
     * 查询商家参与的活动列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listForActByShopId",method = RequestMethod.GET)
    public BaseResp<List<ActShopByShopIdOut>> listForActByShopId(ShopInVo inVo){
        List<ActShopByShopIdOut> result = shopService.listForActByShopId(inVo);
        return new BaseResp<List<ActShopByShopIdOut>>(ResultStatus.SUCCESS,result);
    }


    /**
     * 生成商家二维码图(包括商家id和商家code)这是跳转商家详情页
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/CreateCodeByInfo")
    public BaseResp<String> freeOrderByInfo(ShopInVo inVo){
        if (inVo==null||inVo.getId()==null||inVo.getShopCode()==null) {
            return new BaseResp<String>(ResultStatus.error_param_empty);
        }
        Long id=inVo.getId();
        ShopOut out=shopService.findShopOutById(id);
        if (out==null){
            return new BaseResp<String>(ResultStatus.error_shop_info_empty);
        }
        out.setShopCode(inVo.getShopCode());
        //ShopServiceImpl codeUrl=new ShopServiceImpl();
        String shopUrl= shopService.uploadQRCodeToCosByInfo(out);
        if (shopUrl==null){
            return new BaseResp<String>(ResultStatus.error_shop_code);
        }
        return new BaseResp<String>(ResultStatus.SUCCESS,shopUrl);

    }

    /**
     * 生成商家二维码图(包括商家id和商家code)这是跳转商家订单页
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/CreateCodeBySo")
    public BaseResp<String> freeOrderBySo(ShopInVo inVo){
        if (inVo==null||inVo.getId()==null||inVo.getShopCode()==null) {
            return new BaseResp<String>(ResultStatus.error_param_empty);
        }
        Long id=inVo.getId();
        ShopOut out=shopService.findShopOutById(id);
        if (out==null){
            return new BaseResp<String>(ResultStatus.error_shop_info_empty);
        }
        out.setShopCode(inVo.getShopCode());
        //ShopServiceImpl codeUrl=new ShopServiceImpl();
        String shopUrl= shopService.uploadQRCodeToCosBySo(out);
        if (shopUrl==null){
            return new BaseResp<String>(ResultStatus.error_shop_code);
        }
        return new BaseResp<String>(ResultStatus.SUCCESS,shopUrl);

    }
    /**
     * 群推推送信息给商家（客户端升级）
     * @return
     */
    @RequestMapping(value = "/pushInfo")
    public BaseResp<String> GTPush(PushMsg msg){
        String push= GtPush.pushInfoByShop(msg);
        return new BaseResp<String>(ResultStatus.SUCCESS,push);
    }

    /**
     * 点对点推送信息给一个商家的所有人（客户端升级）
     * @return
     */
    @RequestMapping(value = "/pushSoByShop")
    public BaseResp<String> GTPushSoBuShop(PushMsg msg){
        if (msg.getShopId()!=null){
            UserInVo inVo=new UserInVo();
            inVo.setShopId(msg.getShopId());
            List<User> user=userService.listForShopId(inVo);
            for (int i =0;i<user.size();i++){
                PushMsg pushMsg= new PushMsg();
                pushMsg.setAlias(user.get(i).getId().toString());
                pushMsg.setTitle(msg.getTitle());
                pushMsg.setMessageInfo(msg.getMessageInfo());
                pushMsg.setIos(msg.getIos());
                pushMsg.setType(msg.getType());
                pushMsg.setBadge(msg.getBadge());
                GtPush.sendMessage(pushMsg);
            }
        }
        if (msg.getShopId()==null) {
            GtPush.sendMessage(msg);
        }
        return new BaseResp<String>(ResultStatus.SUCCESS,"发出消息");
    }



}
