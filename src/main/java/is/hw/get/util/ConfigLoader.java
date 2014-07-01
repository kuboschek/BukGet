package is.hw.get.util;
import is.hw.get.settings.GetConfig;
import is.hw.get.settings.SettingName;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map.Entry;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigLoader {
	private File configFolder;
	private String configName;
	
	public ConfigLoader(File configFolder, String configName) {
		GsonBuilder b = new GsonBuilder();
		b.setExclusionStrategies(new ExclusionStrategy() {
			
			@Override
			public boolean shouldSkipField(FieldAttributes arg0) {
				return true;
			}
			
			@Override
			public boolean shouldSkipClass(Class<?> arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		if(!configFolder.isDirectory()) {
			throw new IllegalArgumentException("Config folder must be folder.");
		}
		
		if(!configFolder.canRead()) {
			throw new IllegalArgumentException("Config folder must be readable.");
		}
		
		this.configFolder = configFolder;
		this.configName = configName;
	}
	
	public boolean loadConfig() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		File configFile = new File(configFolder, configName + ".json");
		
		if (!configFile.exists()) {
			throw new IOException("Config file " + configFile.getPath() + " not found!");
		}
		
		JsonParser parser = new JsonParser();
		String configContent = FileUtils.readFile(configFile);
		
		JsonObject toplevel = parser.parse(configContent).getAsJsonObject();
		
		for (Entry<String, JsonElement> categoryEntry : toplevel.entrySet()) {
			String catName = categoryEntry.getKey();
			JsonObject category = categoryEntry.getValue().getAsJsonObject();
			//
			for (Entry<String, JsonElement> fieldEntry : category.entrySet()) {
				String fieldName = fieldEntry.getKey();
				String fieldVal = fieldEntry.getValue().getAsString();
				//
				for (Class<?> catClass : GetConfig.class.getDeclaredClasses()) {
					if (catClass.getSimpleName().equals(catName)) {
						catClass.getField(fieldName).set(null, fieldVal);
					}
				}
			}
		}
		
		return true;
/*		this.configName = configName;
		
		boolean configFound = false;
		
		File[] confFiles = configFolder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if(filename.endsWith(".json")) return true;
				return false;
			}
		});
		
		for(File f : confFiles) {
			if(f.getName().equals(configName)) {
				configFound = true;
				configFile = f;
			}
		}
		
		if(!configFile.canRead()) {
			throw new IllegalArgumentException("Config file must be readable.");
		}
		
		if(configFound && configFile != null) {
			String json = FileUtils.readFile(configFile);
			gson.fromJson(json, GetConfig.class);
		} else {
			throw new IllegalArgumentException("The given configuration file does not exist.");
		}
		
		
		return false;*/
	}
	
	public boolean saveConfig() throws IOException {
		File configFile = new File(configFolder, configName + ".json");
		
		if(!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			configFile.createNewFile();
		}
		
		/*
		 * {
		 * 	General: {
		 * 		feld: "Wert"
		 * 	}
		 * }
		 */
		JsonObject toplevel = new JsonObject();
		//
		for (Class<?> categoryClass : GetConfig.class.getDeclaredClasses()) {
			if (categoryClass.isAnnotationPresent(SettingName.class)) {
				JsonObject catObj = new JsonObject();
				//
				for (Field setting : categoryClass.getDeclaredFields()) {
					try {
						catObj.addProperty(setting.getName(), (String) setting.get(null));
					} catch (IllegalAccessException | IllegalArgumentException e) {
						e.printStackTrace();
						continue;
					}
					//
					toplevel.add(categoryClass.getSimpleName(), catObj);
				}
			}
		}
		//
		Gson gson = new GsonBuilder()
						.setPrettyPrinting()
						.create();
		//
		FileUtils.writeFile(configFile, gson.toJson(toplevel));
		
		return true;
	}
}
