package org.platform.vehicle.utils.phone.http;

import com.alibaba.fastjson.JSON;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.platform.vehicle.utils.phone.model.Resp;
import org.platform.vehicle.utils.phone.util.HttpUtil;

/**
 * Created by
 * xiashuai
 * on 2017/10/24
 */
public class HttpManager {
    public static final HttpManager instance = new HttpManager();
    private CloseableHttpClient httpClient;

    public HttpManager(){
        PoolingHttpClientConnectionManager connectionManager = HttpUtil.getConnManager();
        httpClient = HttpUtil.getClient(connectionManager);
    }

    public static HttpManager getInstance() {
        return instance;
    }

    public String post(String URL, List<NameValuePair> nameValuePairs){
        String result = "";
        HttpPost httpPost = new HttpPost(URL);
        try {
            // 加浏览器头防止被过滤
//            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }else{
                Resp resp = new Resp(response.getStatusLine().getStatusCode(),response.getStatusLine().getReasonPhrase());
                result = JSON.toJSONString(resp);
            }
        }catch(HttpHostConnectException e){
            Resp resp = new Resp(Constants.Status.CONNECT_FAIL,e.getMessage());
            result = JSON.toJSONString(resp);
        } catch (Exception e) {
            Resp resp = new Resp(Constants.Status.EXCEPTION,e.getMessage());
            result = JSON.toJSONString(resp);
        }finally {
            //释放连接
            try{
                httpPost.releaseConnection();
            }catch (Exception e){

            }
        }
        return result;
    }

}
