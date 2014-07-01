package is.hw.get.remote;

import java.io.Reader;

import com.google.gson.Gson;

public class JsonRequest {
	public static final String jsonrpc = "2.0";
	public String method;
	public Object params[];
	public String id;
	
	public JsonRequest(String method, String id, Object... params) {
		this.method = method;
		this.id = id;
		this.params = params;
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public static JsonRequest fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, JsonRequest.class);
	}

	public static JsonRequest fromJson(Reader reader) {
		Gson gson = new Gson();
		return gson.fromJson(reader, JsonRequest.class);
	}
}
