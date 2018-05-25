package com.xq.live.service.impl;

import com.xq.live.common.Constants;
import com.xq.live.common.Pager;
import com.xq.live.common.QRCodeUtil;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.AccountService;
import com.xq.live.service.SoService;
import com.xq.live.service.UploadService;
import com.xq.live.vo.in.ProRuInVo;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.SoForOrderOut;
import com.xq.live.vo.out.SoOut;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-09 14:29
 * @copyright:hbxq
 **/
@Service
@Transactional
public class SoServiceImpl implements SoService {

    private Logger logger = Logger.getLogger(SoServiceImpl.class);

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CouponSkuMapper couponSkuMapper;

    @Autowired
    private SoDetailMapper soDetailMapper;

    @Autowired
    private SoLogMapper soLogMapper;

    @Autowired
    private SoShopLogMapper soShopLogMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private PaidLogMapper paidLogMapper;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private PromotionRulesMapper promotionRulesMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountService accountService;

    @Override
    public Pager<SoOut> list(SoInVo inVo) {
        Pager<SoOut> ret = new Pager<SoOut>();
        int total = soMapper.listTotal(inVo);
        if (total > 0) {
            List<SoOut> list = soMapper.list(inVo);
            ret.setList(list);
        }
        ret.setTotal(total);
        ret.setPage(inVo.getPage());
        return ret;
    }

    @Override
    public List<SoOut> findSoList(SoInVo inVo) {
        List<SoOut> list = soMapper.list(inVo);
        for (SoOut soOut : list) {
            soOut.setRuleDesc(" ");
        }
        return list;
    }

    @Override
    @Transactional
    public Long create(SoInVo inVo) {
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        if(sku == null){
            return null;
        }
        //2、计算订单金额
        BigDecimal soAmount = BigDecimal.valueOf(inVo.getSkuNum()).multiply(sku.getSellPrice());

        //3、保存订单信息
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_WAIT_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            logger.error("保存订单失败,userId : "+inVo.getUserId()+" skuId : "+inVo.getSkuId());
            return null;
        }
        Long id = inVo.getId();
        //4、保存订单明细信息
        inVo.setId(id);
        this.saveSoDetail(inVo, sku);

        //5、保存订单日志
        this.saveSoLog(inVo, SoLog.SO_STATUS_WAIT_PAID);

        //6、判断支付方式，如果是享七支付，则直接扣减账户余额，并修改订单状态，如果是微信支付，则只生成待支付的订单
        inVo.setId(id);
        if(inVo.getPayType() == So.SO_PAY_TYPE_XQ){
            UserAccount account = accountService.findAccountByUserId(inVo.getUserId());
            if(account == null){    //账户信息不存在，请检查账户
                logger.error("用户id："+ inVo.getUserId() + " 的账户信息不存在，请检查！");
                throw new RuntimeException("用户id："+ inVo.getUserId() + " 的账户信息不存在，请检查！");
            }
            if(account.getAccountAmount().compareTo(soAmount) == -1){   //账户余额小于订单金额
                logger.error("用户id："+ inVo.getUserId() + " 的账户余额不足，无法使用余额支付");
                throw new RuntimeException("用户id："+ inVo.getUserId() + " 的账户余额不足，无法使用余额支付");
            }
            UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(inVo.getUserId());
            accountInVo.setOccurAmount(soAmount);
            accountService.payout(accountInVo, "订单支付，订单号："+ id);

            //更新订单支付状态，写入订单日志
            this.paid(inVo);
        }

        return id;
    }

    @Override
    public Long createForShop(SoInVo inVo) {
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        if(sku == null){
            return null;
        }
        //2、保存订单信息
        inVo.setSoStatus(So.SO_STATUS_WAIT_PAID);
        inVo.setSoType(So.SO_TYPE_SJ);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            logger.error("保存订单失败,userId : "+inVo.getUserId()+" skuId : "+inVo.getSkuId());
            return null;
        }
        Long id = inVo.getId();
        //3、保存订单明细信息
        inVo.setId(id);
        //4、保存订单日志
        this.saveSoShopLog(inVo, SoLog.SO_STATUS_WAIT_PAID);



        return id;
    }

    @Override
    public Long freeOrder(SoInVo inVo){
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        //2、计算订单金额
        BigDecimal soAmount = BigDecimal.ZERO;

        //3、保存订单信息,免费订单的状态直接写为“已支付”
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            return null;
        }
        Long id = inVo.getId();

        //4、保存订单明细信息
        inVo.setId(id);
        this.saveSoDetail(inVo, sku);

        //5.生成代金券
        this.createCoupon(inVo);

        //6、保存订单日志，订单状态:已支付
        this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
        return id;
    }

    @Override
    public Long freeOrderForAct(SoInVo inVo) {
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        //2、计算订单金额
        BigDecimal soAmount = BigDecimal.ZERO;

        //3、保存订单信息,免费订单的状态直接写为“已支付”
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            return null;
        }
        Long id = inVo.getId();

        //4、保存订单明细信息
        inVo.setId(id);
        this.saveSoDetail(inVo, sku);

        //5.生成活动代金券(只能参与活动的商家才能使用)
        this.createCouponForAct(inVo);

        //6、保存订单日志，订单状态:已支付
        this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
        return id;
    }

    /*@Override
    public Long freeOrderForAgio(SoInVo inVo) {
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        //2、计算订单金额
        BigDecimal soAmount = BigDecimal.ZERO;

        //3、保存订单信息,免费订单的状态直接写为“已支付”
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            return null;
        }
        Long id = inVo.getId();

        //4、保存订单明细信息
        inVo.setId(id);
        this.saveSoDetail(inVo, sku);

        //5.生成套餐折扣券(只能被套餐使用)
        this.createCouponForAgio(inVo);

        //6、保存订单日志，订单状态:已支付
        this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
        return id;
    }*/

    //判断用户是否领过卷，0没有，1有过
    @Override
    public Integer hadBeenGiven(SoInVo inVo) {
        Integer i=soMapper.hadBeenGiven(inVo);
        if (i>0){
            return 1;
        }
        return 0;
    }
    //根据订单ID获取userID(代码不可用！重写)
/*    @Override
    public Long getUserIDBySoId(SoInVo inVo) {
        Long i=soMapper.getUserIDBySoId(inVo);
        return i;
    }*/


    @Override
    public SoOut get(Long id) {
        return soMapper.selectByPk(id);
    }

    @Override
    public So selectForShop(Long id) {
        return soMapper.selectByPrimaryKey(id);
    }


    @Override
    public SoForOrderOut getForOrder(Long id) {
        SoForOrderOut soForOrderOut = soMapper.selectByPkForOrder(id);
        if(soForOrderOut==null||soForOrderOut.getUserId()==null){
            return null;
        }
        User user = userMapper.selectByPrimaryKey(soForOrderOut.getUserId());
        if(user==null){
            return null;
        }
        soForOrderOut.setMoblie(user.getMobile());
        return soForOrderOut;
    }


    @Override
    public Integer paid(SoInVo inVo) {
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);
        int ret = soMapper.paid(inVo);
        if (ret > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 3);     //有效时间为3个月
            //2、支付成功生成抵用券
            this.createCoupon(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
        }
        return ret;
    }

    @Override
    public Integer paidForShop(SoInVo inVo) {
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);

        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(inVo.getUserId());
        accountInVo.setOccurAmount(inVo.getSoAmount());
        accountService.income(accountInVo, "用户买单，订单号：" + inVo.getId());

        int ret = soMapper.paid(inVo);
        if (ret > 0) {
            SoShopLog soShopLog = soShopLogMapper.selectBySoId(inVo.getId());
            inVo.setSkuId(soShopLog.getSkuId());//为了让paidForShop接口的skuId的值能获取到
            inVo.setShopId(soShopLog.getShopId());//为了能让wxNotifyForShop接口的shopId的值能够获取到
            //2、商家订单日志
            this.saveSoShopLog(inVo, SoLog.SO_STATUS_PAID);
        }
        return ret;
    }

    @Override
    public Integer finished(SoInVo inVo) {
        return null;
    }

    @Override
    public Integer cancel(SoInVo inVo) {
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_CANCEL);
        int ret = soMapper.cancel(inVo);

        if (ret > 0) {
            this.saveSoLog(inVo, SoLog.SO_STATUS_CANCEL);
        }
        return ret;
    }

    @Override
    public Integer selectByUserIdTotal(Long userId){
        return soMapper.selectByUserIdTotal(userId);
    }


    /**
     * 生成券二维码图片并上传到腾讯云服务器
     * @param code
     * @return
     */
    private String uploadQRCodeToCos(String code) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";
        String destPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator + code + ".jpg";
        String text = Constants.DOMAIN_XQ_URL + "/cp/getByCode/"+code;

        //生成logo图片到destPath
        try {
            QRCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "coupon");
        int i=0;
        do {
            i++;
           if (imgUrl==null){
               imgUrl=uploadService.uploadFileToCos(destPath, "coupon");
           }
            if (imgUrl!=null){
                break;
            }
            if (i==4){
                break;
            }
        }while (true);

        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }

        //删除服务器上临时文件
        uploadService.deleteTempImage(new Triplet<String, String, String>(destPath, null, null));
        return imgUrl;
    }

    /**
     * 保存订单明细信息
     * @param inVo
     * @return
     */
    private Integer saveSoDetail(SoInVo inVo, Sku sku){
        SoDetail sd = new SoDetail();
        sd.setSoId(inVo.getId());
        sd.setSkuId(inVo.getSkuId());
        sd.setSkuCode(sku.getSkuCode());
        sd.setSkuName(sku.getSkuName());
        sd.setSkuNum(inVo.getSkuNum());
        sd.setUnitPrice(sku.getSellPrice());
        return soDetailMapper.insert(sd);
    }

    /**
     * 保存订单日志
     * @return
     */
    private Integer saveSoLog(SoInVo inVo, int operateType){
        int result = 0;
        SoLog soLog = new SoLog();
        soLog.setSoId(inVo.getId());
        soLog.setUserId(inVo.getUserId());
        soLog.setUserName(inVo.getUserName());
        soLog.setUserIp(inVo.getUserIp());
        soLog.setOperateType(operateType);
        try {
            result = soLogMapper.insert(soLog);
        } catch (Exception e) {
            logger.error("保存订单操作日志失败，订单soId :"+ inVo.getId()+" 操作类型，operateType: "+operateType);
        }
        return result;
    }

    /**
     * 保存商家订单日志
     * @return
     */
    private Integer saveSoShopLog(SoInVo inVo, int operateType){
        int result = 0;
        SoShopLog soShopLog = new SoShopLog();
        soShopLog.setSoId(inVo.getId());
        soShopLog.setUserId(inVo.getUserId());
        soShopLog.setUserName(inVo.getUserName());
        soShopLog.setUserIp(inVo.getUserIp());
        soShopLog.setShopId(inVo.getShopId());
        soShopLog.setSkuId(inVo.getSkuId());
        soShopLog.setOperateType(operateType);
        try {
            result = soShopLogMapper.insert(soShopLog);
        } catch (Exception e) {
            logger.error("保存商家订单操作日志失败，订单soId :"+ inVo.getId()+" 操作类型，operateType: "+operateType);
        }
        return result;
    }

    /**
     * 根据订单信息生成代金券
     * @param inVo
     * @return
     */
    private int createCoupon(SoInVo inVo){
        int res = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);     //有效时间为3个月
        //2、支付成功生成抵用券
        for (int i = 0; i < inVo.getSkuNum(); i++) {
            //3、查询券信息
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            CouponSku couponSku = couponSkuMapper.selectBySkuId(inVo.getSkuId());
            SoOut soOut = this.get(inVo.getId());
            Coupon coupon = new Coupon();
            coupon.setSoId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            coupon.setType(Coupon.COUPON_TYPE_PLAT);
            coupon.setUserId(soOut.getUserId());
            coupon.setUserName(soOut.getUserName());
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            couponMapper.insert(coupon);
            res++;
        }
        return res;
    }

    /**
     * 根据订单信息生成活动代金券
     * @param inVo
     * @return
     */
    private int createCouponForAct(SoInVo inVo){
        int res = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);     //有效时间为3个月
        //2、支付成功生成抵用券
        for (int i = 0; i < inVo.getSkuNum(); i++) {
            //3、查询券信息
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            CouponSku couponSku = couponSkuMapper.selectBySkuId(inVo.getSkuId());
            SoOut soOut = this.get(inVo.getId());
            Coupon coupon = new Coupon();
            coupon.setSoId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            coupon.setType(Coupon.OUNPON_TYPE_ACT);//这里为活动券，只能参与活动的商家使用
            coupon.setUserId(soOut.getUserId());
            coupon.setUserName(soOut.getUserName());
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            couponMapper.insert(coupon);
            res++;
        }
        return res;
    }

    /**
     * 根据订单信息生成折扣券
     * @param inVo
     * @return
     */
    /*private int createCouponForAgio(SoInVo inVo){
        int res = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);     //有效时间为3个月
        //2、支付成功生成抵用券
        for (int i = 0; i < inVo.getSkuNum(); i++) {
            //3、查询券信息
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            CouponSku couponSku = couponSkuMapper.selectBySkuId(inVo.getSkuId());
            SoOut soOut = this.get(inVo.getId());
            Coupon coupon = new Coupon();
            coupon.setSoId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            coupon.setType(Coupon.OUNPON_TYPE_AGIO);// 这里套餐折扣券(只能被套餐使用)
            coupon.setUserId(soOut.getUserId());
            coupon.setUserName(soOut.getUserName());
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            couponMapper.insert(coupon);
            res++;
        }
        return res;
    }*/

    /**
     * 保存支付日志
     * @param inVo
     * @return
     */
    private int savePayLog(SoInVo inVo){
        int res = 0;
        PaidLog paidLog = new PaidLog();
        paidLog.setSoId(inVo.getId());
        paidLog.setPaidType(So.SO_PAY_TYPE_WX);
        paidLog.setUserId(inVo.getUserId());
        paidLog.setUserName(inVo.getUserName());
        try {
            res = paidLogMapper.insert(paidLog);
        } catch (Exception e) {
            logger.error("支付保存订单日志失败,soId : " + inVo.getId());
        }
        return res;
    }
}
