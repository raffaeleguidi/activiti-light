package org.mybpmbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

public class JSHelper extends org.mybpmbox.utils.JSEnabled {
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
	
	public String httpGet(String theUrl) throws MalformedURLException, IOException {
		URLConnection con = new java.net.URL(theUrl).openConnection();
		return asResponse((HttpURLConnection)con);
	}
	public String asResponse(HttpURLConnection con) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder bldr = new StringBuilder();
		String decodedString;
		while ((decodedString = in.readLine()) != null) {
			bldr.append(decodedString);
		}
		in.close();
		NativeObject ret = parse("{statusCode: " + con.getResponseCode()  + "}");
		org.mozilla.javascript.ScriptableObject so = (org.mozilla.javascript.ScriptableObject)ret;
		so.put("data", so, bldr.toString());
		return bldr.toString();
	}
	
	/*
	 *
function httpGet(theUrl){
    var con = new java.net.URL(theUrl).openConnection();
    con.requestMethod = "GET";

    return asResponse(con);
}

function httpPost(theUrl, data, contentType){
    contentType = contentType || "application/json";
    var con = new java.net.URL(theUrl).openConnection();

    con.requestMethod = "POST";
    con.setRequestProperty("Content-Type", contentType);

    // Send post request
    con.doOutput=true;
    write(con.outputStream, data);

    return asResponse(con);
}

function asResponse(con){
    var d = read(con.inputStream);

    return {data : d, statusCode : con.responseCode};
}

function write(outputStream, data){
    var wr = new java.io.DataOutputStream(outputStream);
    wr.writeBytes(data);
    wr.flush();
    wr.close();
}

function read(inputStream){
    var inReader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
    var inputLine;
    var response = new java.lang.StringBuffer();

    while ((inputLine = inReader.readLine()) != null) {
           response.append(inputLine);
    }
    inReader.close();
    return response.toString();
}*/
}
