package is.hw.api.bukget;

public class CategoryListing extends BukGetData{
	
	public Category[] cats;
	
	public CategoryListing() {
		super("categories", true);
	}
	
	public class Category {
		public int count;
		public String name;
	}
}
