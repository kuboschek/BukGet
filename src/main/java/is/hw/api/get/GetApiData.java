package is.hw.api.get;

import is.hw.api.JsonWebData;

public class GetApiData extends JsonWebData {

	private static final transient String base_url = "http://hw.is:8000/api/";
	
	protected GetApiData(String url) {
		super(base_url + url);
	}

	protected GetApiData(String url, boolean isAnnotated) {
		super(base_url + url, isAnnotated);
	}
}
