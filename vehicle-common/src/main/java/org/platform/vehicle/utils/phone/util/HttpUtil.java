package org.platform.vehicle.utils.phone.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpUtil {

	/**
	 * 初始化 连接管理器
	 * @return
	 */
	public static PoolingHttpClientConnectionManager getConnManager() {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setDefaultMaxPerRoute(30);
		connManager.setMaxTotal(120);
		return connManager;
	}

	public static CloseableHttpClient getClient(PoolingHttpClientConnectionManager connManager) {
		if (null == connManager) {
			return null;
		}
		// 设置连接参数
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
		// 创建自定义的httpclient对象
		CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig)
				.setConnectionManager(connManager).disableCookieManagement().build();
		return client;
	}

}
