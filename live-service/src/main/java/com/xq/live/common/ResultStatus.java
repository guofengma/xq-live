package com.xq.live.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:45
 */
public enum ResultStatus {


    // -1为通用失败（根据ApiResult.java中的构造方法注释而来）
    FAIL(-1, "common fail"), // 0为成功
    SUCCESS(0, "success"),


    error_param_empty(100001, "缺少参数"),

    error_param_empty_id(100002, "id必填"),

    error_so_not_exist(100003, "订单不存在"),

    error_so_cancel_status_error(100004, "订单状态错误，不允许取消"),

    error_user_exist(200001, "用户已存在"),

    error_coupon_sku_exist(200002, "记录已存在"),

    error_file_upload_error(200003, "文件上传失败"),

    error_file_upload_empty(200004, "上传文件为空"),

    error_para_user_empty(200005, "用户信息为空"),

    error_para_user_login(200006, "用户名或密码错误"),

    error_para_coupon_code_empty(200007, "您的券码有误，请核查!"),

    error_so_not_wait_pay(200008, "订单不是待支付状态"),

    error_input_user_id(200009, "用户id错误"),

    error_user_shop_exist(200010, "该用户已经入驻，不能重复申请"),

    error_act_shop_exist(200011, "已经报名该活动，不能重复报名"),

    error_weixin_user_code_empty(200012, "获取openId异常，缺少参数code"),

    error_so_paid(200013, "订单已支付，不能重复支付"),

    error_param_shop_id_empty(200014, "缺少参数shopId"),

    error_para_cashier_id(200015, "扫码人cashierId错误"),

    error_para_cashier_user_type(200016, "不是商家账号"),

    error_para_user_shop_id(200017, "商家账户的shopId为空"),

    error_shop_info_empty(200018, "没有查询到商家信息"),

    error_coupon_is_used(200019, "券不存在或者已经被使用"),

    error_unified_order_fail(200020, "调用统一下单接口失败"),

    error_param_open_id_empty(200021, "参数openId必填"),

    error_param_open_id(200022, "参数openId错误"),

    error_user_not_new(200023, "用户存在下单历史，不允许免费领取"),

    error_param_message_text_id_empty(200024, "messageTextId必填"),

    error_param_mobile_empty(200025,"参数mobile必填"),

    error_act_user_exist(200026, "已经报名该活动，不能重复报名"),

    error_act_sign_exist(200027, "已经报名该活动，不能重复报名"),

    error_act_sign_fail(200028,"报名失败，请检查报名材料!"),

    error_vote_fail(200029,"今天已经投票，不能重复投票!"),

    error_sku_fail(200030,"今天还未投票，不能领取活动券!"),

    error_act_update(200031,"更新失败!"),

    error_act_insert(200032,"插入失败"),

    error_user_account_info(200035,"用户账户信息错误!"),

    error_cash_apply_amount(200036,"账户余额不足"),

    error_so_had(200037,"一天只能领取一张"),

    error_user_play(200038,"支付失败"),

    error_user_account(200039,"余额不足"),

    error_user_getaccount(200040,"领取红包失败"),

    error_parm_user_id_empty(200050,"用户id为空！"),

    error_parm_coupon_id_empty(200051,"享七券id为空！"),

    error_coupon_null(200052,"享七券不存在！"),

    error_coupon_user_id(200053,"不是本人买的券，无法领取红包！"),

    error_coupon_is_not_used(200054,"未核销享七券，无法领取红包！"),

    error_receive_red_packet_fail(200055,"未核销享七券，无法领取红包！"),

    error_user_account_not_enough(200056,"账户余额不足，请选择其他支付方式！"),

    error_sowriteoff_amount(200041,"没有历史记录"),

    error_agio_fail(200042,"还有未使用的折扣券，请使用！"),

    error_group_list(200043,"没有记录"),

    error_mobile_is_null(200044,"该用户没有手机号，请发送验证码完成注册!"),

    error_use_coupon_limit(200045,"该用户今日使用券数目达到上限，欢迎明天使用!"),

    error_many_cashier(200046,"该用户已经存在于shop_cashier表里了!"),

    error_allocation_update(200047,"更新失败，请检查参数"),

    error_allocation_insert(200048,"插入信息失败，请检查参数"),

    getError_allocation_selectList(200049,"没有查询到此信息，请检查参数"),

    error_para_coupon_id_empty(200050,"您的券id有误，请核查!"),

    error_para_shop_id_empty(200051,"shopId不能为空"),

    error_para_shop_allocation_empty(200052,"该商家并没有配置付款方式或查出来没数据,请重新核查!"),

    error_weixin_user_unionid_empty(200053, "获取unionId异常，缺少参数"),

    error_shop_code(200054,"生成商家二维码失败"),

    error_actuser_code(200055,"没有查询到此活动"),

    error_act_sku_not_use(200056,"还有未使用的7.7元活动券，请使用！"),

    error_act_sku_not_empty(200057,"已经存在了食典券！"),

    error_act_shop_not_right(200058, "此张食典券并不是到该商家购买"),

    error_pic_file(3, "非法图片文件"), error_pic_upload(4, "图片上传失败"), error_record_not_found(5, "没有找到对应的数据"), error_max_page_size(6, "请求记录数超出每次请求最大允许值"), error_create_failed(7, "新增失败"), error_update_failed(8, "修改失败"), error_delete_failed(9, "删除失败"), error_search_failed(10, "查询失败"), error_count_failed(11, "查询数据总数失败"), error_string_to_obj(12, "字符串转java对象失败"), error_invalid_argument(13, "参数不合法"), error_update_not_allowed(14, "更新失败：%s"), error_duplicated_data(15, "数据已存在"), error_unknown_database_operation(16, "未知数据库操作失败，请联系管理员解决"), error_column_unique(17, "字段s%违反唯一约束性条件"), error_file_download(18, "文件下载失败"), error_file_upload(19, "文件上传失败"),

    //100-511为http 状态码
    // --- 4xx Client Error ---
    http_status_bad_request(400, "Bad Request"), http_status_unauthorized(401, "Unauthorized"), http_status_payment_required(402, "Payment Required"), http_status_forbidden(403, "Forbidden"), http_status_not_found(404, "Not Found"), http_status_method_not_allowed(405, "Method Not Allowed"), http_status_not_acceptable(406, "Not Acceptable"), http_status_proxy_authentication_required(407, "Proxy Authentication Required"), http_status_request_timeout(408, "Request Timeout"), http_status_conflict(409, "Conflict"), http_status_gone(410, "Gone"), http_status_length_required(411, "Length Required"), http_status_precondition_failed(412, "Precondition Failed"), http_status_payload_too_large(413, "Payload Too Large"), http_status_uri_too_long(414, "URI Too Long"), http_status_unsupported_media_type(415, "Unsupported Media Type"), http_status_requested_range_not_satisfiable(416, "Requested range not satisfiable"), http_status_expectation_failed(417, "Expectation Failed"), http_status_im_a_teapot(418, "I'm a teapot"), http_status_unprocessable_entity(422, "Unprocessable Entity"), http_status_locked(423, "Locked"), http_status_failed_dependency(424, "Failed Dependency"), http_status_upgrade_required(426, "Upgrade Required"), http_status_precondition_required(428, "Precondition Required"), http_status_too_many_requests(429, "Too Many Requests"), http_status_request_header_fields_too_large(431, "Request Header Fields Too Large"),

    // --- 5xx Server Error ---
    http_status_internal_server_error(500, "系统错误"), http_status_not_implemented(501, "Not Implemented"), http_status_bad_gateway(502, "Bad Gateway"), http_status_service_unavailable(503, "Service Unavailable"), http_status_gateway_timeout(504, "Gateway Timeout"), http_status_http_version_not_supported(505, "HTTP Version not supported"), http_status_variant_also_negotiates(506, "Variant Also Negotiates"), http_status_insufficient_storage(507, "Insufficient Storage"), http_status_loop_detected(508, "Loop Detected"), http_status_bandwidth_limit_exceeded(509, "Bandwidth Limit Exceeded"), http_status_not_extended(510, "Not Extended"), http_status_network_authentication_required(511, "Network Authentication Required"),

    // --- 8xx common error ---
    EXCEPTION(800, "exception"), INVALID_PARAM(801, "invalid.param"), INVALID_PRIVI(802, "invalid.privi"),

    //1000以内是系统错误，
    no_login(1000, "没有登录"), config_error(1001, "参数配置表错误"), user_exist(1002, "用户名已存在"), userpwd_not_exist(1003, "用户名不存在或者密码错误"),;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultStatus.class);

    private int code;
    private String msg;

    ResultStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static int getCode(String define) {
        try {
            return ResultStatus.valueOf(define).code;
        } catch (IllegalArgumentException e) {
            LOGGER.error("undefined error code: {}", define);
            return FAIL.getErrorCode();
        }
    }

    public static String getMsg(String define) {
        try {
            return ResultStatus.valueOf(define).msg;
        } catch (IllegalArgumentException e) {
            LOGGER.error("undefined error code: {}", define);
            return FAIL.getErrorMsg();
        }
    }

    public static String getMsg(int code) {
        for (ResultStatus err : ResultStatus.values()) {
            if (err.code == code) {
                return err.msg;
            }
        }
        return "errorCode not defined ";
    }

    public int getErrorCode() {
        return code;
    }

    public String getErrorMsg() {
        return msg;
    }
}
