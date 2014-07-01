package is.hw.api;

import is.hw.get.util.HttpUtils;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonWebData {
	private transient Gson gson;
	private transient String url;	
	private transient boolean isAnnotated;
	private transient List<NameValuePair> params;
	public transient boolean executed = false;
	
	
	/**
	 * Wird von Subklassen aufgerufen um die URL und ähnliches bekannt zu geben
	 * @param url Die API-URL dieses Typs
	 * @param isAnnotated Ist das Datenfeld in der Klasse mit einer Annotation versehen (Arrays)? Sonst ist es die ganze Klasse
	 */
	protected JsonWebData(String url, boolean isAnnotated) {
		gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
		this.url = url;
		this.isAnnotated = isAnnotated;
		this.params = new ArrayList<NameValuePair>();
	}
	
	/**
	 * Vereinfachung des Standartkonstruktors, isAnnotated = false
	 * @param url Die API-URL dieses Typs
	 */
	protected JsonWebData(String url) {
		this(url, false);
	}
	
	protected JsonWebData() {
		this("", false);
	}
	
	public JsonWebData setURL(String url) {
		this.url = url;
		return this;
	}
	
	public JsonWebData addParam(String name, String value) {
		this.params.add(new BasicNameValuePair(name, value));
		return this;
	}
	
	/**
	 * Bef�llt dieses Data-Objekt mit Daten von BukGet
	 */
	public JsonWebData execute() throws Exception {
		// Die URL aufrufen und den von BukGet gelieferten Inhalt speichern
		String urlQuery = "";
		if (params != null) {
			urlQuery = URLEncodedUtils.format(params, "UTF-8");
		}
		HttpClient client = HttpUtils.getDefaultHttpClient();
		HttpGet get = new HttpGet(url + "?" + urlQuery);
		HttpResponse response = client.execute(get);
		InputStreamReader responseReader = new InputStreamReader(response.getEntity().getContent());
		//
		// verf�gt diese Klasse �ber ein (oder mehrere) annotierte Felder, in die die Daten sollen?
		if (isAnnotated) {
			// ja? Dann iteriere �ber diese Felder
			for (Field f : this.getClass().getDeclaredFields()) {
				if (f.isAnnotationPresent(JsonWebContent.class)) {
					// deserialisiere die Daten
					Object data = gson.fromJson(responseReader, f.getType());
					// und speichere sie da rein.
					f.set(this, data);
				}
			}
		} else {
			// ist das nicht der Fall, wird ein neues Gson erstellt, mit dem BukGetDeserializer.
			// Der sorgt daf�r, dass ein bestehendes Objekt mit Daten gef�llt wird, und kein neues erstellt
			// werden muss.
			Gson bukgetGson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT)
								.registerTypeAdapter(getClass(), new JsonWebDeserializer(this))
								.create();
			// das passiert hier.
			bukgetGson.fromJson(responseReader, this.getClass());
		}
		//
		
		//API-Hook aufrufen
		onParseComplete();
		executed = true;
		
		return this;
	}
	
	protected void onParseComplete() {
		return;
	}
	
	public String toJson() {
		return gson.toJson(this);
	}
	
	public String toPretty() {
		Gson pretty = new GsonBuilder()
							.setPrettyPrinting()
							.excludeFieldsWithModifiers(Modifier.TRANSIENT)
							.create();
		return pretty.toJson(this);
	}
}
