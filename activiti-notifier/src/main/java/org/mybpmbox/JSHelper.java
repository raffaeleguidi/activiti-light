package org.mybpmbox;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

public class JSHelper {
	public String stringify(ScriptableObject json) {
		Context context = Context.enter();
		Object result = NativeJSON.stringify(context, json, json, null, null);
		String stringified = Context.toString(result);
		Context.exit();
		return stringified;
	}
	public NativeObject parse(String stringified) {
		Context context = Context.enter();
		ScriptableObject scope = context.initSafeStandardObjects();
		NativeObject parsed = (NativeObject)NativeJSON.parse(context, scope, stringified, new NullCallable());
		Context.exit();
		return parsed;
	}
	
	public String readAndReturnAString(String str) {
		return str;
	}
	public String readAnObjectReturnAStringProperty(ScriptableObject obj, String property) {
		return obj.get(property).toString();
	}
	public NativeObject readObjectWriteObject(ScriptableObject obj) {
		String stringified = stringify(obj);
		NativeObject parsed = parse(stringified);
		return parsed;
	}
	
	public Object readObjectWriteObjectOld(ScriptableObject obj) {
		Context context = Context.enter();
		Object result = NativeJSON.stringify(context, obj, obj, null, null);
		String stringified = Context.toString(result);
		ScriptableObject scope = context.initSafeStandardObjects();
		Object parsed = NativeJSON.parse(context, scope, stringified, new NullCallable());
		Context.exit();
		return parsed;
	}
}
