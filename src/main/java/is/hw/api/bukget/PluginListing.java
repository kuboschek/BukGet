package is.hw.api.bukget;

import is.hw.api.JsonWebContent;

public class PluginListing extends BukGetData{

	@JsonWebContent
	public PluginInfo[] plugins;
	
	public PluginListing() {
		super("plugins", true);
	}
	
	public PluginListing(String server) {
		super("plugins/" + server, true);
	}
	
	public PluginListing(String server, String fields) {
		super("plugins/" + server, true);
		addParam("fields", fields);
	}
	
	public PluginListing(String server, String fields, int start) {
		super("plugins/" + server, true);
		addParam("fields", fields);
		addParam("start", (new Integer(start).toString()));
	}
	
	public PluginListing(String server, String fields, int start, int size) {
		super("plugins/" + server, true);
		addParam("fields", fields);
		addParam("start", (new Integer(start).toString()));
		addParam("size", (new Integer(size).toString()));
	}
	
	public PluginListing(String server, String fields, int start, int size, String sort) {
		super("plugins/" + server, true);
		addParam("fields", fields);
		addParam("start", (new Integer(start).toString()));
		addParam("size", (new Integer(size).toString()));
		addParam("sort", sort);
	}

}
