package is.hw.api.bukget.search;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class SimpleSearchParameter implements SearchParameter{
	@SuppressWarnings("unused")
	private String field;
	
	@SuppressWarnings("unused")
	private String action;
	
	@SuppressWarnings("unused")
	private String value;
	
	public SimpleSearchParameter(String field, SearchAction action, String value) {
		this.field = field;
		this.action = action.getAction();
		this.value = value;
	}
	
	@Override
	public JsonElement getRequestElement() {
		Gson gson = new Gson();
		
		JsonElement element = gson.toJsonTree(this);
		
		return element;
	}
	
}