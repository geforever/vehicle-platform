package org.platform.vehicle.utils.phone.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.platform.vehicle.utils.phone.http.Constants;
import org.platform.vehicle.utils.phone.http.HttpManager;
import org.platform.vehicle.utils.phone.model.BatchSubmitResp;
import org.platform.vehicle.utils.phone.model.DeliverResp;
import org.platform.vehicle.utils.phone.model.ReportResp;
import org.platform.vehicle.utils.phone.model.SMS;
import org.platform.vehicle.utils.phone.model.SubmitResp;

/**
 * http 提交客户端
 * Created by
 * xiashuai
 * on 2017/10/24
 * 当返回status为-1，-2，-3等负值时，表示请求有错误，数据均没有成功上传到我们平台
 */
public class SmsClient {

    private final String URL = "http://api.com/";

    private final String BATCH_URL = "http://api.com/";

    private final String userId = "****";

    private final String md5password = "****";


    /**
     * 一条内容多号码发送,同步发送
     *
     * @param phone   短信号码，多个请用”,”(英文逗号分隔)，数量<=2w
     * @param content 短信内容,内容包括签名加要发的信息，如【签名】信息；中括号里面的
     *                东西就是签名，签名也计入短信长度
     * @param extCode 扩展码 获取短信上行时会根据这个匹配
     * @param msgId   自定义msgId ,必须为8字节长整型，否则反错。默认不传此值，由我们
     *                平台生成一个msgId返回；如设置此值，平台将使用此msgId作为此次提
     *                交的唯一编号并返回此msgId
     * @return SubmitResp
     * @e.g. smsClient.submit(" 19900000000, 199000000001 ", " 【 窝里烧 】 不带扩展码号 ");
     * smsClient.submit("19900000000,199000000001","【窝里烧】不带扩展码号","");
     * smsClient.submit("19900000000,199000000001","【窝里烧】不带扩展码号，自定义MSGId",12312341234L);
     * smsClient.submit("19900000000,199000000001","【窝里烧】带扩展码号","666");
     * smsClient.submit("19900000000,199000000001","【窝里烧】带扩展码号，自定义MSGId","666",12312341235L);
     * <p>
     * SMS sms = new SMS("19900000000,199000000001","【窝里烧】不带扩展码号,对象发送");
     * sms.setExtCode("666");
     * smsClient.submit(sms);
     */
    public SubmitResp submit(String phone, String content, String extCode, Long msgId) {
        String[] phones = phone.split(",");
        if (phones.length > Constants.phoneLenLimit) {
            SubmitResp submitResp = new SubmitResp();
            submitResp.setStatus(Constants.Status.PHONE_LEN_LIMIT);
            submitResp.setMsg("phones must less than " + Constants.phoneLenLimit);
            return submitResp;
        }
        // api
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("action", "sendsms"));
        nvps.add(new BasicNameValuePair("userId", userId));
        nvps.add(new BasicNameValuePair("md5password", MD5Util.MD5(md5password)));
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("mobile", phone));
        //扩展码
        if (StringUtils.isNumeric(extCode) && extCode.length() > 1) {
            nvps.add(new BasicNameValuePair("extCode", extCode));
        }
        //自定义msgId
        if (msgId != null) {
            nvps.add(new BasicNameValuePair("msgId", msgId.toString()));
        }

        String ret = HttpManager.getInstance().post(URL, nvps);
        return JSON.parseObject(ret, SubmitResp.class);
    }

    public SubmitResp submit(String phone, String content, String extCode) {
        return submit(phone, content, extCode, null);
    }

    public SubmitResp submit(String phone, String content, Long msgId) {
        return submit(phone, content, null, msgId);
    }

    public SubmitResp submit(String phone, String content) {
        return submit(phone, content, null, null);
    }

    public SubmitResp submit(SMS sms) {
        return submit(sms.getMobile(), sms.getContent(), sms.getExtCode(), sms.getMsgId());
    }

    /**
     * 多内容打包发送,不同的内容对应不同的手机号，并且一个内容只能发一个手机号
     *
     * @param smsContents 短信内容（打包数组）。内容数组长度不超过500
     * @return
     */
    public BatchSubmitResp submit(List<SMS> smsContents) {
        //判断数组长度是否过长
        if (smsContents.size() > Constants.batchLenLimit) {
            BatchSubmitResp batchSmsResp = new BatchSubmitResp();
            batchSmsResp.setStatus(Constants.Status.BATCH_LEN_LIMIT);
            batchSmsResp.setMsg("contents must less than " + Constants.Status.BATCH_LEN_LIMIT);
            return batchSmsResp;
        }

        // api
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("action", "sendsms"));
        nvps.add(new BasicNameValuePair("userId", userId));
        nvps.add(new BasicNameValuePair("md5password", md5password));
        nvps.add(new BasicNameValuePair("contentArr", JSON.toJSONString(smsContents)));

        String ret = HttpManager.getInstance().post(BATCH_URL, nvps);
        return JSON.parseObject(ret, new TypeReference<BatchSubmitResp>() {
        });
    }

    /**
     * 获取短信状态反馈
     * 一次最多反馈500条，反馈状态只推一次，拿过的状态不在平台保存
     *
     * @return SendReportResp
     */
    public ReportResp getReport() {
        // api
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("action", "getsendreport"));
        nvps.add(new BasicNameValuePair("userId", userId));
        nvps.add(new BasicNameValuePair("md5password", md5password));

        String ret = HttpManager.getInstance().post(URL, nvps);
        return JSON.parseObject(ret, new TypeReference<ReportResp>() {
        });
    }

    /**
     * 获取短信上行
     *
     * @return DeliverResp
     */
    public DeliverResp getDeliver() {
        // api
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("action", "getdeliver"));
        nvps.add(new BasicNameValuePair("userId", userId));
        nvps.add(new BasicNameValuePair("md5password", md5password));

        String ret = HttpManager.getInstance().post(URL, nvps);
        return JSON.parseObject(ret, new TypeReference<DeliverResp>() {
        });
    }
}

