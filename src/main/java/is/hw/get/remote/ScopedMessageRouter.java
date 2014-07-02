package is.hw.get.remote;

import is.hw.get.settings.GetConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * The ScopedMessageRouter receives callbacks from any JsonRPC instance it is attached to.
 * It then tries to route the message according to its scope parameter.
 * Scopes can be added and removed at runtime and are designed to allow third-party software the use of
 * Get Remote Messaging. If a message can not be delivered immediately, it is stored until either the maximum number
 * of stashed messages is reached, or a matching scope is registered.
 * @author leonhard
 *
 */
public class ScopedMessageRouter implements JsonRPC.DataCallback {
	Map<String, ScopedMessageCallback> scopes = new HashMap<>();
	List<ScopedMessage> stashedMessages = new ArrayList<>();
	
	public void registerScope(String scope, ScopedMessageCallback callback) {
		scopes.put(scope, callback);
		
		List<ScopedMessage> toRemove = new ArrayList<>();
		
		// Two step collection removal
		
		for(ScopedMessage msg : stashedMessages) {
			if(scope.equals(msg.scope)) {
				callback.message(scope, msg.data);
				toRemove.add(msg);
			}
		}
		
		stashedMessages.removeAll(toRemove);
	}
	
	public void removeScope(String scope) {
		scopes.remove(scope);
	}
	
	@Override
	public void data(JsonResponse data) {
		Gson gson = new Gson();
		
		if(!data.result.isJsonPrimitive())
			throw new JsonSyntaxException("Scoped message string expected. Got: " + data.result);
		
		String msgString = data.result.getAsString();
		
		ScopedMessage msg = gson.fromJson(msgString, ScopedMessage.class);
		
		if(msg.scope == null)
			// TODO Complain
			throw new JsonSyntaxException("Scoped messages must contain field scope.");
		
		if(scopes.containsKey(msg.scope)) {
			 scopes.get(msg.scope).message(msg.scope, msg.data);
		} else {
			if(stashedMessages.size() >= GetConfig.Internal.maxStashedMessages) {
				// TODO Complain
			} else {
				stashedMessages.add(msg);
			}
		}
		
		// Parse response into ScopedMessage format
	}
	
	
	/**
	 * Data structure class for Gson deserialization of scoped messages.
	 * @author leonhard
	 *
	 */
	private class ScopedMessage {
		public String scope;
		public String data;
	}
}
