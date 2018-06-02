package com.xq.live.service.impl;

import com.xq.live.common.Constants;
import com.xq.live.common.Pager;
import com.xq.live.common.QRCodeUtil;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.*;
import com.xq.live.model.AccessLog;
import com.xq.live.model.Shop;
import com.xq.live.model.User;
import com.xq.live.service.ShopService;
import com.xq.live.service.UploadService;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.out.PromotionRulesOut;
import com.xq.live.vo.out.ShopOut;
import com.xq.live.vo.out.ShopTopPicOut;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

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
    private ShopTopPicMapper shopTopPicMapper;

    @Autowired
    private PromotionRulesMapper promotionRulesMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Shop getShopById(Long id) {
        return shopMapper.selectByPrimaryKey(id);
    }

    @Override
    public ShopOut findShopOutById(Long id) {
        ShopOut out = shopMapper.findShopOutById(id);
        if (out != null) {
            List<ShopTopPicOut> picOutList = shopTopPicMapper.selectByShopId(out.getId());
            List<Pair<String, String>> picList = new ArrayList<>();
            if (picOutList != null && picOutList.size() > 0) {
                for (ShopTopPicOut picOut : picOutList) {
                    picList.add(new Pair<String, String>(picOut.getAttachment().getSmallPicUrl(), picOut.getAttachment().getPicUrl()));    //小图和大图url
                }
            }
            out.setShopTopPics(picList);
        }
        return out;
    }

    @Override
    public Long addShop(Shop shop) {
        //保存记录到shop表
        int r = shopMapper.insert(shop);
        if (r < 1) {
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
    public Pager<ShopOut> list(ShopInVo inVo) {
        Pager<ShopOut> result = new Pager<ShopOut>();
        int listTotal = shopMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ShopOut> list = shopMapper.list(inVo);
            /**
             * 将用户减免规则加入
             */
            for (ShopOut shopOut : list) {
                /**
                 * 及时读取人气数目
                 */
                String key = "shopPops_" + shopOut.getId().toString();
                Integer pops = redisCache.get(key, Integer.class);
                if(pops!=null){
                    shopOut.setPopNum(pops);
                }
                List<PromotionRulesOut> promotionRulesOuts = promotionRulesMapper.selectByShopId(shopOut.getId().intValue());
                List<String> stringList = new ArrayList<String>();
                for (PromotionRulesOut promotionRulesOut : promotionRulesOuts) {
                    stringList.add(promotionRulesOut.getRuleDesc());
                }
                shopOut.setRuleDescs(stringList);
            }
            /**
             * 根据综合排序 0 口味 1服务 2 人气
             *//*
            if (inVo != null && inVo.getBrowSort()!= null &&  inVo.getBrowSort() == Shop.BROW_SORT_POP) {
                Collections.sort(list);
            }*/
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public Pager<ShopOut> listForChuangXiang(ShopInVo inVo) {
        Pager<ShopOut> result = new Pager<ShopOut>();
        List<ShopOut> list = shopMapper.listForChuanXiang(inVo);
        for (ShopOut shopOut : list) {
            /**
             * 及时读取人气数目
             */
            String key = "shopPops_" + shopOut.getId().toString();
            Integer pops = redisCache.get(key, Integer.class);
            if(pops!=null){
                shopOut.setPopNum(pops);
            }
            List<PromotionRulesOut> promotionRulesOuts = promotionRulesMapper.selectByShopId(shopOut.getId().intValue());
            List<String> stringList = new ArrayList<String>();
            for (PromotionRulesOut promotionRulesOut : promotionRulesOuts) {
                stringList.add(promotionRulesOut.getRuleDesc());
            }
            shopOut.setRuleDescs(stringList);
        }
        result.setTotal(list.size());
        result.setList(list);
        result.setPage(inVo.getPage());
        return result;

    }

    @Override
    public Map<String, List<ShopOut>> listForHomePage(ShopInVo inVo) {
        Map<String, List<ShopOut>> map = new HashMap<String,List<ShopOut>>();
        List<ShopOut> shopOuts = shopMapper.listForChuanXiang(inVo);
        map.put("CX",shopOuts);
        inVo.setBusinessCate("海鲜");
        shopOuts = shopMapper.list(inVo);
        map.put("HX",shopOuts);
        inVo.setBusinessCate("火锅");
        shopOuts = shopMapper.list(inVo);
        map.put("HG",shopOuts);
        inVo.setBusinessCate("烧烤");
        shopOuts = shopMapper.list(inVo);
        map.put("SK",shopOuts);
        inVo.setBusinessCate("西餐");
        shopOuts = shopMapper.list(inVo);
        map.put("XC",shopOuts);
        inVo.setBusinessCate("自助餐");
        shopOuts = shopMapper.list(inVo);
        map.put("ZZ",shopOuts);
        inVo.setBusinessCate("商务");
        shopOuts = shopMapper.list(inVo);
        map.put("SW",shopOuts);
        inVo.setBusinessCate("聚会");
        shopOuts = shopMapper.list(inVo);
        map.put("JH",shopOuts);
        inVo.setBusinessCate("约会");
        shopOuts = shopMapper.list(inVo);
        map.put("YH",shopOuts);
        return map;
    }

    @Override
    public List<ShopOut> top(ShopInVo inVo) {
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
        if (cnt == 0) {
            try {
                int logCnt = accessLogMapper.insert(accessLog);
                /*if (logCnt > 0) {
                    shopMapper.updatePopNum(inVo.getId());  //增加人气数值l
                }*/
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

    @Override
    public ShopOut getShopByCode(String code) {
        ShopOut out = shopMapper.findShopOutByCode(code);
        if (out!=null){
            return out;
        }
        return null;
    }


    /**
     * 生成券二维码图片并上传到腾讯云服务器
     * @param out
     * @return
     */
    public String uploadQRCodeToCos(ShopOut out) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";
        String destPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator + out.getShopCode() + ".jpg";
        String text = Constants.DOMAIN_XQ_URL + "/service?shopId="+out.getId()+"&flag="+1;

        //生成logo图片到destPath
        try {
            QRCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UploadServiceImpl uploadService = new UploadServiceImpl();
        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "shopcode");
        int i=0;
        do {
            i++;
            if (imgUrl==null){
                imgUrl=uploadService.uploadFileToCos(destPath, "shopcode");
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
}
