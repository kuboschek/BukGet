package is.hw.api.bukget;

import is.hw.api.JsonWebContent;

public class AuthorPluginListing extends BukGetData{

	@JsonWebContent
	public PluginInfo[] plugins;
	
	public AuthorPluginListing() {
		super("authors", true);
	}
	
	public AuthorPluginListing(String server) {
		super("authors/" + server, true);
	}
	
	public AuthorPluginListing(String server, String fields) {
		super("authors/" + server, true);
		addParam("fields", fields);
	}
	
	public AuthorPluginListing(String server, String fields, int start) {
		super("authors/" + server, true);
		addParam("fields", fields);
		addParam("start", (new Integer(start).toString()));
	}
	
	public AuthorPluginListing(String server, String fields, int start, int size) {
		super("authors/" + server, true);
		addParam("fields", fields);
		addParam("start", (new Integer(start).toString()));
		addParam("size", (new Integer(size).toString()));
	}
	
	public AuthorPluginListing(String server, String fields, int start, int size, String sort) {
		super("authors/" + server, true);
		addParam("fields", fields);
		addParam("start", (new Integer(start).toString()));
		addParam("size", (new Integer(size).toString()));
		addParam("sort", sort);
	}

}