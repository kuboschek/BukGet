package is.hw.api.bukget;

import is.hw.api.bukget.CategoryListing.Category;

public class CategoryPluginListing extends BukGetData {
	
	public PluginInfo[] plugins;
	
	public CategoryPluginListing(Category cat) {
		super("categories/" + cat.name, true);
	}
}
