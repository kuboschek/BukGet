package is.hw.api.get;

public class PackInfo extends GetApiData {
	
	public String name;
	public String creator;
	public PackPlugin[] plugins;

	public PackInfo(int id) {
		super(id + "/get");
	}
	
	public class PackPlugin {
		public String slug;
		public String version;
	}
}
