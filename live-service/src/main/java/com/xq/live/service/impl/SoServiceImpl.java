package com.xq.live.service.impl;

import com.xq.live.common.Constants;
import com.xq.live.common.Pager;
import com.xq.live.common.QRCodeUtil;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.SoService;
import com.xq.live.service.UploadService;
import com.xq.live.vo.in.ProRuInVo;
import com.xq.live.vo.in.SoInVo;
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
    private CouponMapper couponMapper;

    @Autowired
    private PaidLogMapper paidLogMapper;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private PromotionRulesMapper promotionRulesMapper;

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
            ProRuInVo proRuInVo = new ProRuInVo();
            proRuInVo.setSkuId(soOut.getSkuId());
            proRuInVo.setSkuCode(soOut.getSkuCode());
            PromotionRules rules = promotionRulesMapper.selectBySkuIdAndSkuCode(proRuInVo);
            if(rules!=null){
                soOut.setRuleDesc(rules.getRuleDesc());
            }
        }
        return list;
    }

    @Override
    public Long create(SoInVo inVo) {
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        //2、查询券信息
        CouponSku couponSku = couponSkuMapper.selectBySkuId(inVo.getSkuId());

        //3、计算订单金额
        BigDecimal soAmount = BigDecimal.valueOf(inVo.getSkuNum()).multiply(sku.getSellPrice());

        //4、保存订单信息
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_WAIT_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            return null;
        }
        Long id = inVo.getId();

        //5、保存订单明细信息
        SoDetail sd = new SoDetail();
        sd.setSoId(id);
        sd.setSkuId(inVo.getSkuId());
        sd.setSkuCode(sku.getSkuCode());
        sd.setSkuName(sku.getSkuName());
        sd.setSkuNum(inVo.getSkuNum());
        sd.setUnitPrice(sku.getSellPrice());
        soDetailMapper.insert(sd);

        //7、保存订单日志
        SoLog soLog = new SoLog();
        soLog.setSoId(id);
        soLog.setUserId(inVo.getUserId());
        soLog.setUserName(inVo.getUserName());
        soLog.setOperateType(SoLog.SO_STATUS_WAIT_PAID);
        try {
            soLogMapper.insert(soLog);
        } catch (Exception e) {
            logger.error("下单保存订单日志失败");
        }

        return id;
    }



    @Override
    public SoOut get(Long id) {
        return soMapper.selectByPk(id);
    }

    @Override
    public SoForOrderOut getForOrder(Long id) {
        return soMapper.selectByPkForOrder(id);
    }


    @Override
    public Integer paid(SoInVo inVo) {
        inVo.setId(inVo.getId());
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);
        int ret = soMapper.paid(inVo);
        if (ret > 0) {
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
                //            //新开一个线程上传二维码并生成抵用券
                //            new Thread(new Runnable() {
                //                @Override
                //                public void run() {
                //                    logger.error("1、上传二维码");
                //                    logger.error("2、生成抵用券");
                //                    //                exceptionAnalysisService.runAnalysisStrategyJob(planId, taskBeginTime); //参数必须是final类型的
                //                    String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
                //                    coupon.setQrcodeUrl(qrcodeUrl);
                //                    couponMapper.insert(coupon);
                //                }
                //            }).start();
            }

            try {
                //4、支付日志
                PaidLog paidLog = new PaidLog();
                paidLog.setSoId(inVo.getId());
                paidLog.setPaidType(So.SO_PAY_TYPE_WX);
                paidLog.setUserId(inVo.getUserId());
                paidLog.setUserName(inVo.getUserName());
                paidLogMapper.insert(paidLog);

                //5、订单日志
                SoLog soLog = new SoLog();
                soLog.setSoId(inVo.getId());
                soLog.setUserId(inVo.getUserId());
                soLog.setUserName(inVo.getUserName());
                soLog.setOperateType(SoLog.SO_STATUS_PAID);
                soLogMapper.insert(soLog);
            } catch (Exception e) {
                logger.error("支付保存订单日志失败,soId : " + inVo.getId());
            }
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
            try {
                //2、订单日志
                SoLog soLog = new SoLog();
                soLog.setSoId(inVo.getId());
                soLog.setUserId(inVo.getUserId());
                soLog.setUserName(inVo.getUserName());
                soLog.setOperateType(SoLog.SO_STATUS_CANCEL);
                soLogMapper.insert(soLog);
            } catch (Exception e) {
                logger.error("取消订单保存日志失败,soId : " + inVo.getId());
            }
        }
        return ret;
    }


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
        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }

        //删除服务器上临时文件
        uploadService.deleteTempImage(new Triplet<String, String, String>(destPath, null, null));
        return imgUrl;
    }
}
