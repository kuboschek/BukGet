package is.hw.get.script;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class GetScript {
	Context cx;
	Scriptable scope;
	
	public GetScript(String content, File zip, CommandSender sender) {
		cx = Context.enter();
		cx.setClassShutter(new ClassShutter() {
			@Override
			public boolean visibleToScripts(String clazz) {
				if (clazz.equals(GetAPI.class.getName())) {
					return true;
				} else if (clazz.equals(ChatColor.class.getName())) {
					return true;
				} else if (clazz.startsWith("com.sun.proxy")) {
					// somehow that one's needed for callback passing...
					return true;
				}
				return false;
			}
		});
		
		scope = cx.initStandardObjects();
		ScriptableObject.putProperty(scope, "API", new GetAPI(cx, scope, zip, sender));
		ScriptableObject.putProperty(scope, "Get", scope);
		
		cx.evaluateString(scope, content, "GetScript", 0, null);
	}
	
	@Override
	public void finalize() {
		Context.exit();
	}
	
	public void interpretSection(String name) {
		Object functionObject = scope.get(name, scope);
		if (functionObject instanceof Function) {
			Function fct = (Function) functionObject;
			fct.call(cx, scope, scope, null);
		}
	}
}
