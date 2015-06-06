package org.mybpmbox.utils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.mybpmbox.utils.NullCallable;

public class JSEnabled {

	public JSEnabled() {
		super();
	}
	
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
	

}