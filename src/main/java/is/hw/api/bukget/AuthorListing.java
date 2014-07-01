package is.hw.api.bukget;

import is.hw.api.JsonWebContent;

import com.google.gson.annotations.SerializedName;

public class AuthorListing extends BukGetData {
	
	@JsonWebContent
	public Author[] authors;
	
	public AuthorListing() {
		super("authors", true);
	}
	
	public class Author {
		@SerializedName("count")
		public int pluginCount;
		public String name;
	}
}
