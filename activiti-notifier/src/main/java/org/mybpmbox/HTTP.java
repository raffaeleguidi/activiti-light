package org.mybpmbox;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;

import org.mozilla.javascript.ScriptableObject;
import org.mybpmbox.utils.Response;


public class HTTP extends org.mybpmbox.utils.JSEnabled {
	public static Response get(String theUrl) throws MalformedURLException, IOException {
		URLConnection con = new java.net.URL(theUrl).openConnection();
		return asResponse((HttpURLConnection)con);
	}
	private static Response asResponse(HttpURLConnection con) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder bldr = new StringBuilder();
		String decodedString;
		while ((decodedString = in.readLine()) != null) {
			bldr.append(decodedString);
		}
		in.close();
		Response response = new Response();
		response.body = bldr.toString();
		response.statusCode = con.getResponseCode();
		return response;
	}
	private static Response post(String theUrl, String data, String contentType) throws IOException {
		HttpURLConnection con = (HttpURLConnection)new java.net.URL(theUrl).openConnection();
	    
	    con.setRequestMethod("POST");
	    con.setRequestProperty("Content-Type", contentType);

	    // Send post request
	    con.setDoOutput(true);
	    write(con.getOutputStream(), data);

	    return asResponse(con);
	}
	public static Response post(String theUrl, String data) throws IOException {
		return post(theUrl, data, "application/json");
	}
	
	public static Response post(String theUrl, ScriptableObject data) throws IOException {
		Response res = post(theUrl, jsonStringify(data), "application/json");
		res.content = jsonParse(res.body);
		return res;
	}
	
	
	private static void write(OutputStream outputStream, String data) throws IOException{
	    DataOutputStream wr = new java.io.DataOutputStream(outputStream);
	    wr.writeBytes(data);
	    wr.flush();
	    wr.close();
	}
}
