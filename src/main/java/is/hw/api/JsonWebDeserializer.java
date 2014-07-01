package is.hw.api;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.google.gson.*;

/**
 * Ein eigener JsonDeserializer für BukGetData-Objekte, der ein bestehendes Objekt mit Daten befüllt.
 * @author simon
 *
 */
public class JsonWebDeserializer implements JsonDeserializer<JsonWebData> {
	private JsonWebData dataObj;
	
	public JsonWebDeserializer(JsonWebData dataObject) {
		this.dataObj = dataObject;
	}
	
	@Override
	public JsonWebData deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		// Der Typ des Objektes in das gespeichert werden soll.
		Class<?> objectClass = dataObj.getClass();
		//
		// Über alle Felder im JSON iterieren, damit wir sie speichern können
		for (Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
			try {
				// Datentyp des entsprechenden Feldes in der Klasse abfragen
				Class<?> fieldClass;
				try {
					fieldClass = objectClass.getField(entry.getKey()).getType();
				} catch (NoSuchFieldException ex) {
					continue;
				}
				// Das JSON-Feld deserialisieren
				Object field = context.deserialize(entry.getValue(), fieldClass);
				// und in das passende Feld von dataObj reinschreiben.
				objectClass.getField(entry.getKey()).set(dataObj, field);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Sicherheitshalber wird nochmal das dataObj zurückgegeben
		return dataObj;
	}

}
