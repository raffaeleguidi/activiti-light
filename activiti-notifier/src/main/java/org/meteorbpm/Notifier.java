package org.meteorbpm;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;


public class Notifier implements ActivitiEventListener {

	public void restNotify(ActivitiEventType eventType, String processDefinitionId, String processInstanceId) {
		System.out.println("this is a notification of type: " + eventType + " for process " + processInstanceId);
		try {
			String baseUrl = System.getenv("METEOR_BASE_URL") != null ? System.getenv("METEOR_BASE_URL") : "http://localhost:3000";
			String theUrl = baseUrl + "/api/notification?" +
					"eventType=" + eventType +
					"&processDefinitionId=" + processDefinitionId +
					"&processInstanceId=" + processInstanceId ;

			System.out.println("url is:" + theUrl);
			URLConnection con = new java.net.URL(theUrl).openConnection();
			BufferedReader inReader = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
		    String inputLine;
		    StringBuffer response = new java.lang.StringBuffer();

		    while ((inputLine = inReader.readLine()) != null) {
		           response.append(inputLine);
		    }
		    inReader.close();
			System.out.println(response.toString());
			System.out.println("notification done");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

/*
 *
 * function httpGet(theUrl){
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

	@Override
	public void onEvent(ActivitiEvent event) {
		switch (event.getType()) {
			case TASK_CREATED:
			case TASK_ASSIGNED:
			case TASK_COMPLETED:
				restNotify(event.getType(),
						event.getProcessDefinitionId(),
						event.getProcessInstanceId());
				break;

			default:
//				System.out.println("getType: " + event.getType());
//				System.out.println("getProcessDefinitionId: " + event.getProcessDefinitionId());
//				System.out.println("getExecutionId: " + event.getExecutionId());
		}
	}

	@Override
	public boolean isFailOnException() {
	    return false;
	}

}
