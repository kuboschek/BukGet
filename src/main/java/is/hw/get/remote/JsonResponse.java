package is.hw.get.remote;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonResponse {
	public static final String jsonrpc = "2.0";
	public JsonElement result;
	public JsonError error;
	public String id;
	
	public class JsonError {
		public int code;
		public String message;
		public JsonElement data;
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public static JsonResponse fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, JsonResponse.class);
	}

	public static JsonResponse fromJson(Reader reader) {
		Gson gson = new Gson();
		return gson.fromJson(reader, JsonResponse.class);
	}
}
