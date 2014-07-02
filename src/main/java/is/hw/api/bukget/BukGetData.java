package is.hw.api.bukget;

import is.hw.api.JsonWebData;

public class BukGetData extends JsonWebData {
	public static transient String baseURL = "http://api.bukget.org/3/";
	
	public BukGetData(String url, boolean isAnnotated, boolean isPostRequest) {
		super(baseURL + url, isAnnotated, isPostRequest);
	}
	
	/**
	 * Wird von Subklassen aufgerufen, um die URL und ähnliches bekannt zu geben
	 * @param url Die API-URL dieses Typs
	 * @param isAnnotated Ist das Datenfeld in der Klasse mit einer Annotation versehen (Arrays)? Sonst ist es die ganze Klasse
	 */
	protected BukGetData(String url, boolean isAnnotated) {
		super(baseURL + url, isAnnotated);
	}
	
	protected BukGetData(String url) {
		super(baseURL + url);
	}
	
	protected BukGetData() {
		super();
	}
}
