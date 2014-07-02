package is.hw.get.remote;

import is.hw.get.settings.GetConfig;
import is.hw.get.util.HttpUtils;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.bukkit.Bukkit;

public class JsonRPC  implements ResponseHandler<JsonResponse> {
	public interface DataCallback {
		public void data(JsonResponse data);
	}
	
	private HttpClient httpclient;
	private int id = 0;
	
	private HttpPost currentMessagePost;
	private Thread polling_thread;
	
	public JsonRPC() {
		RequestConfig reqConfig = RequestConfig.custom().setConnectTimeout(0).setConnectionRequestTimeout(0).setSocketTimeout(0).build();
		httpclient = HttpUtils.getNewHttpClient(reqConfig);
	}
	
	public void startMessageListener(DataCallback callback, Object... params) {
		polling_thread = new MessageThread(callback, params);
		polling_thread.start();
	}
	
	public void stopMessageListener() {
		polling_thread.interrupt();
		currentMessagePost.abort();
	}
	
	public boolean register(String server_key, String server_name, String pubkey) throws IOException {
		JsonResponse resp = jsonCall("register", server_key, server_name, pubkey);
		if (resp.error != null) {
			throw new ClientProtocolException("There was an error during registration " + resp.error.message);
		}
		
		return resp.result.getAsJsonObject().get("result").getAsBoolean();
	}
	
	private JsonResponse jsonCall(String method, Object... params) throws IOException {
		String reqid = "req-" + Integer.toString(id++);
		
		JsonRequest reqOut = new JsonRequest(method, reqid, params);
		
		String jsonString = reqOut.toJson();
		
		HttpPost post = new HttpPost(GetConfig.Internal.remoteUrl);
		currentMessagePost = post;
		post.addHeader("Accept", "application/json-rpc");
		post.setEntity(new StringEntity(jsonString, ContentType.create("application/json-rpc")));
		
		return httpclient.execute(post, this);
	}

	@Override
	public JsonResponse handleResponse(HttpResponse response) throws IOException {
		StatusLine status = response.getStatusLine();
		HttpEntity entity = response.getEntity();
		//
		if (status.getStatusCode() >= 300) {
            throw new HttpResponseException(
                    status.getStatusCode(),
                    status.getReasonPhrase());
		}
		//
		if (entity == null) {
			throw new ClientProtocolException("JSON-RPC Response contains no content!");
		}
		//
		return JsonResponse.fromJson(new InputStreamReader(entity.getContent()));
	}
	
	private class MessageThread extends Thread {
		private Object params[];
		private DataCallback callback;
		private int backoff_time = 500;
		
		public MessageThread(DataCallback callback, Object params[]) {
			this.params = params;
			this.callback = callback;
		}
		
		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					JsonResponse response = jsonCall("message", params);
					if (response.error != null) {
						// JSON-RPC returned an error
						Bukkit.getServer().getLogger().warning("The server returned an error. Backing off.");
						Bukkit.getServer().getLogger().warning(response.toJson());
						backoff_time *= 2;
						Thread.sleep(backoff_time);
					} else {
						callback.data(response);
					}
				} catch (IOException | InterruptedException e) {
					if (isInterrupted() || e instanceof InterruptedException) {
						return;
					}
					//
					Bukkit.getServer().getLogger().warning("There was an error whilst waiting (or receiving) the response. Backing off.");
					e.printStackTrace();
					//
					backoff_time *= 2;
					try {
						Thread.sleep(backoff_time);
					} catch (InterruptedException e1) {
						
					}
				}
			}
		}
	}
}
