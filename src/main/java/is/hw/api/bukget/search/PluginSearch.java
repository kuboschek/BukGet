package is.hw.api.bukget.search;

import is.hw.api.JsonWebContent;
import is.hw.api.JsonWebData;
import is.hw.api.bukget.BukGetData;
import is.hw.api.bukget.PluginInfo;

import com.google.gson.Gson;

public class PluginSearch extends BukGetData {
	private transient SearchParameter[] params;
	
	@JsonWebContent
	public PluginInfo[] plugins;
	
	public PluginSearch(SearchParameter... params) {
		super("search", true, true);
		this.params = params;
	}
	
	public JsonWebData execute() throws Exception {
		super.setPostData(buildRequestString());
		
		System.out.println("POST Request String: " + buildRequestString());
		
		return super.execute();
	}
	
	private String buildRequestString() {
		Gson gson = new Gson();
		return gson.toJson(params);
	}
}
