package is.hw.get.util;

import is.hw.get.settings.GetConfig;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpUtils {
	private static HttpClient _httpclient = null;
	
	public static HttpClient getDefaultHttpClient() {
		if (_httpclient == null) {
			_httpclient = HttpClientBuilder.create().setUserAgent(GetConfig.Internal.http_user_agent).build();
		}
		return _httpclient;
	}
	
	public static HttpClient getNewHttpClient() {
		return HttpClientBuilder.create().setUserAgent(GetConfig.Internal.http_user_agent).build();
	}
	
	public static HttpClient getNewHttpClient(RequestConfig req_conf) {
		return HttpClientBuilder.create().setUserAgent(GetConfig.Internal.http_user_agent).setDefaultRequestConfig(req_conf).build();
	}
}
