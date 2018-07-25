package com.xq.live.web.controllerForApp;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.FreeSkuConfig;
import com.xq.live.model.Shop;
import com.xq.live.model.So;
import com.xq.live.poientity.SoDetailEntity;
import com.xq.live.service.ShopService;
import com.xq.live.service.SoService;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.out.SoForOrderOut;
import com.xq.live.vo.out.SoOut;
import com.xq.live.web.utils.POIUtils;
import com.xq.live.poientity.example.TemplateExcelExportEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单controller
 *
 * @author lipeng
 * @date 2018-02-09 14:24
 * @copyright:hbxq
 */
@RestController
@RequestMapping(value = "/app/so")
public class SoForAppController {

    @Autowired
    private SoService soService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private FreeSkuConfig freeSkuConfig;

    /**
     * 查一条记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<SoOut> get(@PathVariable("id") Long id) {
        SoOut soOut = soService.get(id);
        return new BaseResp<SoOut>(ResultStatus.SUCCESS, soOut);
    }

    /**
     * 查询我的订单中的订单详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getForOrder/{id}", method = RequestMethod.GET)
    public BaseResp<SoForOrderOut> getForOrder(@PathVariable("id") Long id) {
        SoForOrderOut forOrder = soService.getForOrder(id);
        return new BaseResp<SoForOrderOut>(ResultStatus.SUCCESS, forOrder);
    }

    /**
     * 分页查询列表
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<SoOut>> list(SoInVo inVo) {
        Pager<SoOut> result = soService.list(inVo);
        return new BaseResp<Pager<SoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查我的订单
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/myorder", method = RequestMethod.GET)
    public BaseResp<List<SoOut>> top(SoInVo inVo) {
        List<SoOut> result = soService.findSoList(inVo);
        return new BaseResp<List<SoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询商家端中平台代收的营业额
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/totalAmount", method = RequestMethod.GET)
    public BaseResp<BigDecimal> totalAmount(SoInVo inVo) {
        BigDecimal bigDecimal = soService.totalAmount(inVo);
        return new BaseResp<BigDecimal>(ResultStatus.SUCCESS, bigDecimal);
    }

    /**
     * 查我的商家订单
     * 查询我的订单中的商家订单，基本参数与平台订单相同  userId ,page,rows,soStatus
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/myorderForShop", method = RequestMethod.GET)
    public BaseResp<Pager<SoOut>> myorderForShop(SoInVo inVo) {
        inVo.setSoType(So.SO_TYPE_SJ);
        Pager<SoOut> result = soService.findSoListForShop(inVo);
        return new BaseResp<Pager<SoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 生成订单
     *
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResp<Long> create(@Valid SoInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = soService.create(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 新用户首单免费
     *
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/freeOrder", method = RequestMethod.POST)
    public BaseResp<Long> freeOrder(@Valid SoInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        Integer soOutNum = soService.selectByUserIdTotal(inVo.getUserId());
        if (soOutNum > 0) {
            return new BaseResp<Long>(ResultStatus.error_user_not_new);
        }
        inVo.setSkuId(freeSkuConfig.getSkuId());
        inVo.setSkuNum(freeSkuConfig.getSkuNum());

        Long id = soService.freeOrder(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 订单支付
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/paid", method = RequestMethod.POST)
    public BaseResp<Integer> paid(SoInVo inVo) {
        //入参校验
        if (inVo.getId() == null || inVo.getUserId() == null || StringUtils.isEmpty(inVo.getUserName())) {
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //订单不存在
        SoOut soOut = soService.get(inVo.getId());
        if (soOut == null) {
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        //订单不是待支付状态，不能修改支付状态
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Integer>(ResultStatus.error_so_not_wait_pay);
        }
        inVo.setSkuId(soOut.getSkuId());
        int ret = soService.paid(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

    /**
     * 订单取消
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public BaseResp<Integer> cancel(SoInVo inVo) {
        //入参校验
        if (inVo.getId() == null || inVo.getUserId() == null || StringUtils.isEmpty(inVo.getUserName())) {
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //订单不存在
        SoOut soOut = soService.get(inVo.getId());
        if (soOut == null) {
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        //只有待支付的订单才能取消
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Integer>(ResultStatus.error_so_cancel_status_error);
        }

        int ret = soService.cancel(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

    /**
     * poi导出报表----示例1
     *
     * @return
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取数据
        List<Object[]> list = new ArrayList<Object[]>();
        Object[] objs = new Object[6];
        objs[0] = 0;
        objs[1] = "12345";
        objs[2] = "99999";
        objs[3] = "未审批";
        objs[4] = "李鹏";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        objs[5] = date;
        list.add(objs);

        //excel标题
        String[] rowsName = new String[]{"序号", "货物运输批次号", "提运单号", "状态", "录入人", "录入时间"};

        //excel文件名
        String fileName = "订单信息表" + System.currentTimeMillis() + ".xls";

        //sheet名
        String title = "订单信息表";

        //创建HSSFWorkbook
        HSSFWorkbook wb = POIUtils.getHSSFWorkbook(title, rowsName, list);

        //响应到客户端
        if (wb != null) {
            try {
                this.setResponseHeader(response, fileName);
                OutputStream os = response.getOutputStream();
                wb.write(os);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**如果上面的方法不行,可以使用下面的用法
     * 同样的效果,只不过是直接问输出了,不经过view了
     *
     * easypoi导出示例
     * @param modelMap
     * @param request
     * @param response
     */

    @RequestMapping("exampleLoad")
    public void downloadByPoiBaseView(ModelMap modelMap, HttpServletRequest request,
                                      HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                Thread.currentThread().getContextClassLoader().getResource("").getPath() + "doc" + File.separator + "example.xlsx");
        List<TemplateExcelExportEntity> list = new ArrayList<TemplateExcelExportEntity>();

        for (int i = 0; i < 4; i++) {
            TemplateExcelExportEntity entity = new TemplateExcelExportEntity();
            entity.setIndex(i + 1 + "");
            entity.setAccountType("开源项目");
            entity.setProjectName("EasyPoi " + i + "期");
            entity.setAmountApplied(i * 10000 + "");
            entity.setApprovedAmount((i + 1) * 10000 - 100 + "");
            list.add(entity);
        }
        map.put("entitylist", list);
        map.put("manmark", "1");
        map.put("letest", "12345678");
        map.put("fntest", "12345678.2341234");
        map.put("fdtest", null);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> testMap = new HashMap<String, Object>();

            testMap.put("id", "xman");
            testMap.put("name", "小明" + i);
            testMap.put("sex", "1");
            mapList.add(testMap);
        }
        map.put("maplist", mapList);

        mapList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 6; i++) {
            Map<String, Object> testMap = new HashMap<String, Object>();

            testMap.put("si", "xman");
            mapList.add(testMap);
        }
        map.put("sitest", mapList);
        modelMap.put(TemplateExcelConstants.FILE_NAME, "用户信息");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }

    /**
     * 订单明细导出
     * @param modelMap
     * @param request
     * @param response
     */
    @RequestMapping("soDetailExport")
    public void soDetailExport(ModelMap modelMap, SoInVo inVo,HttpServletRequest request,
                                      HttpServletResponse response) {
        if(inVo==null||inVo.getSoStatus()==null||inVo.getShopId()==null||inVo.getBeginTime()==null||inVo.getEndTime()==null){
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                Thread.currentThread().getContextClassLoader().getResource("").getPath() + "doc" + File.separator + "soDetail.xlsx");
        Map<String,Object> mapColl = soService.soDetailExport(inVo);
        Shop shopById = shopService.getShopById(inVo.getShopId());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<SoDetailEntity> list = (List<SoDetailEntity>)mapColl.get("list");
        map.put("list",list);
        map.put("shopName",shopById.getShopName());
        map.put("beginTime",formatter.format(inVo.getBeginTime()));
        map.put("endTime",formatter.format(inVo.getEndTime()));
        map.put("rowSize",list.size());
        map.put("totalAcAmount",mapColl.get("totalAcAmount"));
        map.put("totalService",mapColl.get("totalService"));
        map.put("totalReAmount",mapColl.get("totalReAmount"));

        modelMap.put(TemplateExcelConstants.FILE_NAME, shopById.getShopName()+"订单信息");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }



}
