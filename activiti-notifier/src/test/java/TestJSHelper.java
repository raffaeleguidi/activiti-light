import static org.junit.Assert.*;

import org.junit.Test;
import org.mozilla.javascript.*;

public class TestJSHelper {

	@Test
	public void testPassObject() {
		String expected = "ciao";
		String script = "var obj = new org.mybpmbox.JSHelper; var ret = obj.readAnObjectReturnAStringProperty({test: 'ciao'}, 'test'); ret;";
		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();
			Object result = cx.evaluateString(scope, script, "", 1, null);
			String ret = Context.toString(result);
			assertEquals("unexpected return value", expected, ret);
		} finally {	
		  Context.exit();
		}
	}
	@Test
	public void testJSon() {
		String expected = "ciao";
		String script = "var obj = new org.mybpmbox.JSHelper; var obj2 = obj.readObjectWriteObject({aaa: 'ciao'}); obj2.aaa;";
		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();
			Object result = cx.evaluateString(scope, script, "TestScript", 1, null);
			String ret = Context.toString(result);
			//String ret = Context.toObject(value, scope, staticType)
			assertEquals("value is not expected", expected, ret);
		} finally {	
		  Context.exit();
		}
	}
	
	@Test
	public void testJSExecution() {
		String expected = "ciao";
		String script = "var s = 'ciao'; s;";
		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();
			Object result = cx.evaluateString(scope, script, "", 1, null);
			String ret = Context.toString(result);
			assertEquals("unexpected return value", expected, ret);
		} finally {	
		  Context.exit();
		}
	}
	
	@Test
	public void testStringsToAndFromJava() {
		String expected = "ciao";
		String script = "var obj = new org.mybpmbox.JSHelper; var s=obj.readAndReturnAString('ciao'); s;";
		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();
			Object result = cx.evaluateString(scope, script, "", 1, null);
			String ret = Context.toString(result);
			assertEquals("unexpected return value", expected, ret);
		} finally {	
		  Context.exit();
		}
	}

}
