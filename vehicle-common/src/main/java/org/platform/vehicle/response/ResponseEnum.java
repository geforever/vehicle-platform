package org.platform.vehicle.response;

/**
 * 接口响应枚举类
 */
public enum ResponseEnum {
    SUCCESS(ResponseCode.SUCCESS, "success"),
    LOGOUT_SUCCESS(ResponseCode.SUCCESS, "退出登录成功"),
    NOT_KNOWN_EXCEPTION(ResponseCode.UNKNOW_EXCEPTION, "未知异常"),
    PASSWORD_EXCEPTION(ResponseCode.NOT_EXIST_USER, "密码不能为空"),
    USER_DISABLE_ER_EXCEPTION(ResponseCode.USER_DISABLE_ER, "用户已被禁用"),
    ROLE_DISABLE_ER_EXCEPTION(ResponseCode.USER_DISABLE_ER, "角色权限已被禁用"),
    ROLE_NOT_FOUND_ER_EXCEPTION(ResponseCode.USER_DISABLE_ER,
            "当前账号没有分配角色权限，请联系管理员"),
    USER_NOT_EXIST_EXCEPTION(ResponseCode.NOT_EXIST_USER, "用户不存在"),
    USER_REPETITION_EXCEPTION(ResponseCode.USER_CODE_REPEAT_ERROR,
            "系统中存在重复用户，请联系管理员"),
    USERCODE_EXCEPTION(ResponseCode.NOT_EXIST_USER, "账号不能为空"),
    OUT_TIME_TOKEN_EXCEPTION(ResponseCode.OUT_TIME_TOKEN, "token 失效"),
    NOT_LOGIN_EXCEPTION(ResponseCode.NOT_LOGIN, "用户未登录或token过期,请重新登录"),
    NOT_EXIST_USER_EXCEPTION(ResponseCode.NOT_EXIST_USER, "用户名或密码错误"),
    NOT_CACHED_USER_EXCEPTION(ResponseCode.NOT_CACHED_USER, "用户未在线，请重新登录"),
    NOT_EXIST_TOKEN_EXCEPTION(ResponseCode.NOT_EXIST_TOKEN, "不存在的token"),
    NOT_EXIST_ROLE_EXCEPTION(ResponseCode.NOT_EXIST_ROLE, "没有相关权限"),
    DATA_NULL_ERROR(ResponseCode.DATA_NULL_ERROR, "空指针异常"),
    FEIGN_ERROR(ResponseCode.FEIGN_ERROR, "Feign调用异常"),
    OPERATION_WITHOUT_PERMISSION(ResponseCode.OPERATION_WITHOUT_PERMISSION, "没有操作权限"),
    WXORDER_TRANSFER_ERROR(ResponseCode.WXORDER_TRANSFER_ERROR, "支付异常,请联系专业人员!"),
    PHOTO_DOWNLOAD_ERROR(ResponseCode.PHOTO_DOWNLOAD_ERROR, "图片下载错误"),
    PICTURE_UPLOAD_ERROR(ResponseCode.FILEUPLOAD_ERROR, "文件上传失败"),
    PICTURE_UPLOAD_NO_FILE(ResponseCode.FILEUPLOAD_ERROR, "上传文件为空"),
    PICTURE_UPLOAD_FORMAT_ERROR(ResponseCode.FILEUPLOAD_ERROR, "文件格式错误"),
    USER_NOT_ADMIN(ResponseCode.USER_NOT_ADMIN, "此账号无管理员权限"),
    PICTURE_UPLOAD_TOO_BIG(ResponseCode.FILEUPLOAD_ERROR, "图片上传失败,图上大小不能超过10M"),
    VIDEO_UPLOAD_TOO_BIG(ResponseCode.FILEUPLOAD_ERROR, "视频上传失败,视频大小不能超过200M"),
    GOODONETYPE_NOT_EXIST(ResponseCode.GOODONETYPE_NOT_EXIST,
            "商品一级分类不存在[可能被停用或删除]"),
    GOODTWOTYPE_NOT_EXIST(ResponseCode.GOODTWOTYPE_NOT_EXIST,
            "商品二级分类不存在[可能被停用或删除]"),
    GOODTHREETYPE_NOT_EXIST(ResponseCode.GOODTWOTYPE_NOT_EXIST,
            "商品三级分类不存在[可能被停用或删除]"),
    GOODINFO_NOT_ADMIN(ResponseCode.GOODINFO_NOT_ADMIN, "此账号无商品管理权限"),
    GOODINFO_NOT_ADMIN_CATEGORY(ResponseCode.GOODINFO_NOT_ADMIN, "非管理员不能管理分类信息"),
    GOODINFO_NOT_ADMIN_SHOWGOODINFO(ResponseCode.GOODINFO_NOT_ADMIN,
            "非管理员不能修改查看商品信息"),
    GOODINFO_NOT_ADMIN_TOAUDIT(ResponseCode.GOODINFO_NOT_ADMIN, "非管理员不能修改商品信息"),
    GOODINFO_NOT_ADMIN_AUDIT(ResponseCode.GOODINFO_NOT_ADMIN, "非管理员不能查看审核列表"),
    GOODINFO_NOT_ADMIN_PUTUPDOWN(ResponseCode.GOODINFO_NOT_ADMIN, "非管理员不能上/下架商品"),
    GOODINFO_NOT_ADMIN_SORT(ResponseCode.GOODINFO_NOT_ADMIN, "非管理员不能设置商品展示排序"),
    GOODINFO_NOT_ADMIN_SET_TOP(ResponseCode.GOODINFO_NOT_ADMIN, "非管理员不能置顶商品"),
    GOODINFO_NOT_ADMIN_DELETE(ResponseCode.GOODINFO_NOT_ADMIN, "非管理员不能删除商品"),
    GOODINFO_NOT_ADMIN_EXPORT(ResponseCode.GOODINFO_NOT_ADMIN, "非管理员无法导出列表"),
    GOODINFO_SUPPLIER_ERROR(ResponseCode.GOODS_SUPPLIER_ERROR, "商家信息不能为空"),
    GOODINFO_SUPPLIER_NO_ERROR(ResponseCode.GOODS_SUPPLIER_ERROR,
            "商品的商家信息与商家信息不一致"),
    GOODINFO_NAME_EXIST(ResponseCode.GOODINFO_EXIST, "此商品名称已存在"),
    GOODINFO_NAME_NOT_EXIST(ResponseCode.GOODINFO_EXIST, "商品名称不能为空"),
    GOODINFO_NAME_TOO_LONG(ResponseCode.GOODINFO_EXIST, "商品名称不能超过50个字符"),
    GOODINFO_DESC_TOO_LONG(ResponseCode.GOODINFO_EXIST, "商品副标题不能超过30个字符"),
    GOODINFO_NO_PHOTO(ResponseCode.GOODINFO_EXIST, "缺少商品图片信息"),
    GOODINFO_SPEC_NO_PHOTO(ResponseCode.GOODINFO_EXIST, "缺少商品规格图片信息"),
    GOODINFO_EXIST(ResponseCode.GOODINFO_EXIST, "此商品已添加成功，请勿重复添加"),
    GOODINFONAME_NOT_EXIST(ResponseCode.GOODINFO_NOT_EXIST, "商品名称不能为空"),
    GOODINFO_GOODSID_NOT_EXIST(ResponseCode.GOODINFO_NOT_EXIST, "商品ID不能为空"),
    GOODBRAND_NOT_EXIST(ResponseCode.GOODBRAND_NOT_EXIST, "不存在的品牌信息"),
    GOODINFO_CREATE_SUCCESS(ResponseCode.SUCCESS, "商品信息添加成功"),
    GOODSSKUCODE_NO_EXIST(ResponseCode.GOODSSKUCODE_TOO_LONG, "商品SKU编码必填"),
    GOODSSKUCODE_TOO_LONG(ResponseCode.GOODSSKUCODE_TOO_LONG, "商品编码不能超过30个字符"),
    GOODS_STOCKNUM_ERROR(ResponseCode.STOCK_MIN_DATA_ERR, "库存数量设置错误"),
    GOODS_WARINGSTOCKNUM_ERROR(ResponseCode.STOCK_MIN_DATA_ERR, "预警库存数量设置错误"),
    GOODS_PRICE_ERROR(ResponseCode.TRAN_PRICE_ERROR, "商品售价需在0-99999999.99区间"),
    GOODS_MAX_GRADE_PRICE_ERROR(ResponseCode.TRAN_PRICE_ERROR,
            "最高会员等级售价需在0-99999999.99区间"),
    GOODS_PUR_PRICE_ERROR(ResponseCode.TRAN_PRICE_ERROR, "商品采购价售价需在0-99999999.99区间"),
    GOODS_COST_PRICE_ERROR(ResponseCode.TRAN_PRICE_ERROR, "商品成本价需在0-99999999.99区间"),
    GOODS_MARINGPRICE_ERROR(ResponseCode.TRAN_PRICE_ERROR, "划线价需在0-99999999.99区间"),
    GOODS_TRANSGPRICE_ERROR(ResponseCode.TRAN_PRICE_ERROR, "统一运费需在0-99999区间"),
    GOODS_SPEC_TOO_MUCH(ResponseCode.JSON_SPECE_OPEN, "商品主规格不能超过5项"),
    GOODS_SPEC_TOO_MUCH2(ResponseCode.JSON_SPECE_OPEN, "商品子规格数量不能超30项"),
    GOODS_SPEC_NOT_EXIST(ResponseCode.JSON_SPECE_OPEN, "商品规格信息不存在"),
    GOODS_SPEC_NAME_SAME(ResponseCode.JSON_SPECE_OPEN, "商品规格名称重复"),
    GOODS_SPEC_YT_UNITS_ERROR(ResponseCode.JSON_SPECE_OPEN, "商品赢途对应单位不能为空"),
    GOODS_SPEC_SKU_SAME(ResponseCode.JSON_SPECE_OPEN, "商品规格SKU重复"),
    GOODS_SPEC_SKU_EXIST(ResponseCode.JSON_SPECE_OPEN, "商品SKU已存在"),
    GOODS_SPEC_NO_SUPPLIER(ResponseCode.JSON_SPECE_OPEN, "商品代发商家为空"),
    GOODS_SPEC_NAME_NOT_EXIST(ResponseCode.JSON_SPECE_OPEN, "商品规格信息不存在"),
    GOODS_TOP_NUM_LIMIT(ResponseCode.GOODS_TOP_NUM_LIMIT, "商品规格信息不存在"),
    GOODS_SORT_NO_ERROR(ResponseCode.GOODS_SORT_NO_ERROR, "排序序号需在0-99999999区间"),
    GOODS_AUDIT_ERROR(ResponseCode.GOODS_SORT_NO_ERROR, "审核状态有误"),
    GOODS_AUDIT_ERROR2(ResponseCode.GOODS_SORT_NO_ERROR, "不是待审核商品无法审核"),
    GOODINFO_NOT_SUPPLIER(ResponseCode.GOODINFO_NOT_SUPPLIER_ERROR, "非商家账号无法管理商品信息"),
    /*------------------------------------------------------订单相关-------------------------------------------------**/
    GENERATE_ORDER_NO_ERROR(ResponseCode.GENERATE_ORDER_NO_ERROR, "系统繁忙, 请稍后再试"),
    GOODS_NOT_EXIST(ResponseCode.GOODS_NOT_EXIST, "商品不存在"),
    PRODUCT_ORDER_NULL(ResponseCode.PRODUCT_ORDER_NULL, "订单不存在"),
    PRODUCT_ORDER_STATUS_ERROR(ResponseCode.ORDER_STATUS_ERROR, "订单状态错误"),
    PRODUCT_SIZE_ERROR(ResponseCode.PRODUCT_SIZE_ERROR, "订单商品数量错误"),
    PRODUCT_IS_IS_NULL(ResponseCode.PRODUCT_ORDER_NULL, "订单商品不存在"),
    CREATE_ORDER_ERROR(ResponseCode.CREATE_ORDER_ERROR, "创建订单失败"),
    CREATE_ORDER_ERROR_PREPARATION_NOT_EXIST(ResponseCode.CREATE_ORDER_ERROR,
            "创建订单失败,预订单不存在"),
    CREATE_ORDER_ERROR_ORDER_IS_EXIST(ResponseCode.CREATE_ORDER_ERROR, "创建订单失败,订单已存在"),
    CREATE_ORDER_ERROR_RECEIVE_ADDRESS_NOT_EXIST(ResponseCode.CREATE_ORDER_ERROR,
            "创建订单失败,收货地址不存在"),
    CREATE_ORDER_ERROR_RECEIVE_ADDRESS_NOT_SALE(ResponseCode.CREATE_ORDER_ERROR,
            "创建订单失败,该地区暂不支持销售"),
    CREATE_ORDER_ERROR_STOCK_IS_NOT_ENOUGH(ResponseCode.CREATE_ORDER_ERROR,
            "商品库存不足,请重新下单"),
    PAY_ERROR(ResponseCode.PAY_ERROR, "支付异常"),
    PAY_ERROR_UNAUTHORIZED(ResponseCode.PAY_ERROR, "支付失败,用户未授权"),
    PAY_ERROR_ORDER_NOT_EXIST(ResponseCode.PAY_ERROR, "支付失败,订单不存在"),
    PAY_ERROR_ORDER_STATUS_ERROR(ResponseCode.PAY_ERROR, "支付失败,订单状态不正确"),
    PAY_PARAM_ERROR(ResponseCode.PAY_PARAM_ERROR, "支付参数错误"),
    PHONE_CODE_ERROR(ResponseCode.PHONE_CODE_ERROR, "短信验证码无效或已过期"),
    PHONE_JSCODE_ERROR(ResponseCode.PHONE_JSCODE_ERROR, "微信jscode为空"),
    PHONE_NO_CODE_ERROR(ResponseCode.PHONE_NO_CODE_ERROR, "短信验证码不正确"),
    PHONE_CODE_SEND_LIMMIT(ResponseCode.PHONE_CODE_SEND_LIMMIT, "验证码发送频繁,请60秒后重试"),
    STOCK_IS_NOT_ENOUGH(ResponseCode.STOCK_IS_NOT_ENOUGH, "库存不足"),
    CART_IS_NOT_EXIST(ResponseCode.CART_IS_NOT_EXIST, "购物车为空"),

    ORDER_COMMENT_CAN_NOT_MODIFY(ResponseCode.ORDER_COMMENT_CAN_NOT_MODIFY, "订单评论不能修改"),

    NOT_ENOUGH_POINTS(ResponseCode.NOT_ENOUGH_POINTS, "积分不足"),

    /*------------------------------------------------------订单退货退款售后相关-------------------------------------------------**/
    REQUEST_PROCESSING(ResponseCode.REQUEST_PROCESSING, "请求处理中"),
    ADDRESS_NOT_EXIST(ResponseCode.ADDRESS_NOT_EXIST, "收货地址不存在"),
    ORDER_RETURN_NOT_EXIST_ERROR(ResponseCode.RETURN_NO_NOT_EXIST, "售后单号不存在"),
    ORDER_RETURN_APPROVED(ResponseCode.ORDER_RETURN_APPROVED, "退货已审核"),
    ORDER_RETURN_APPROVED_ERROR(ResponseCode.ORDER_REFUND_APPROVED, "退款已审核"),
    ORDER_RETURN_APPROVED_STATUS_ERROR(ResponseCode.ORDER_RETURN_STATUS_ERROR, "售后记录状态错误"),
    ORDER_RETURN_SUPPLIER_ERROR(ResponseCode.ORDER_RETURN_SUPPLIER_ERROR, "商家错误"),
    ORDER_RETURN_TYPE_ERROR(ResponseCode.ORDER_RETURN_TYPE_ERROR, "售后类型错误"),
    EXPRESS_INFO_IS_NOT_EXIST_ERROR(ResponseCode.EXPRESS_INFO_IS_NOT_EXIST_ERROR, "快递信息不存在"),
    REFUND_APPLY_ERROR(ResponseCode.REFUND_APPLY_ERROR, "退款金额错误"),

    REPEAT_SERVICE_SITE_NUMBER(ResponseCode.REPEAT_SERVICE_SITE_NUMBER, "服务站编号重复"),

    STOCK_OUT_MORE_THAN_STOCK(ResponseCode.STOCK_OUT_MORE_THAN_STOCK, "出库数量大于库存数量"),
    DELIVERY_ERROR(ResponseCode.DELIVERY_ERROR, "发货异常"),

    ORDER_AUTHORIZATION_ERROR(ResponseCode.ORDER_AUTHORIZATION_ERROR,
            "有商家订单，暂不支持授信业务"),

    /*------------------------------------------------------微信相关-------------------------------------------------**/
    WX_OPENID_ERROR(ResponseCode.WX_OPENID_ERROR, "获取微信openId失败"),

    WX_ACCESS_ERROR(ResponseCode.WX_OPENID_ERROR, "获取微信ACCESS失败"),

    WX_PHONE_ERROR(ResponseCode.WX_OPENID_ERROR, "微信授权手机号码失败"),

    WX_PHONE_REDIS_EXCEPTION(ResponseCode.WX_OPENID_ERROR, "微信授权手机号码已过期，请重新授权"),

    WX_LOGIN_OTHER_PHONE_ERROR(ResponseCode.WX_LOGIN_OTHER_PHONE_ERROR,
            "登录失败，该微信已绑定其他手机号！"),

    WX_LOGIN_OTHER_WX_ERROR(ResponseCode.WX_LOGIN_OTHER_WX_ERROR,
            "登录失败，该手机号已绑定其他微信账号！"),

    NULL_ERROR(ResponseCode.DATA_NULL_ERROR, "数据不存在"),
    GOODS_DELETE_STATUS_STOTCK_ERROR(ResponseCode.GOODS_DELETE_STATUS_STOTCK_ERROR,"商品已删除、已下架或库存不足"),
    ;

    private String code;
    private String message;

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
