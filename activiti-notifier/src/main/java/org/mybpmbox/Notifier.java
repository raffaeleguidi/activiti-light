package org.mybpmbox;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.List;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Notifier implements ActivitiEventListener {
	
	private final Logger log = LoggerFactory.getLogger(ActivitiEventListener.class);

	public void restNotify(ActivitiEventType eventType, String processDefinitionId, String processInstanceId, List<IdentityLink> involvedUsers) {
		log.info("this is a notification of type: " + eventType + " for process " + processInstanceId);
		try {
			String baseUrl = System.getenv("METEOR_BASE_URL") != null ? System.getenv("METEOR_BASE_URL") : "http://localhost:3000";
			String theUrl = baseUrl + "/api/notification?" +
					"eventType=" + eventType +
					"&processDefinitionId=" + processDefinitionId +
					"&processInstanceId=" + processInstanceId ;
			
			log.info("url is:" + theUrl);
			URLConnection con = new java.net.URL(theUrl).openConnection();
			BufferedReader inReader = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
		    String inputLine;
		    StringBuffer response = new java.lang.StringBuffer();

		    while ((inputLine = inReader.readLine()) != null) {
		           response.append(inputLine);
		    }
		    inReader.close();
			log.info(response.toString());
			log.info("notification sent");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void restNotify2(ActivitiEventType eventType, String processDefinitionId, String processInstanceId, List<IdentityLink> involvedUsers) {
		
//		Page page = restTemplate.getForObject("http://graph.facebook.com/pivotalsoftware", Page.class);
//		
		log.info("this is a post notification of type: " + eventType + " for process " + processInstanceId);
		try {
			String baseUrl = System.getenv("METEOR_BASE_URL") != null ? System.getenv("METEOR_BASE_URL") : "http://localhost:3000";
			String theUrl = baseUrl + "/api/notification";
			
			log.info("url is:" + theUrl);

			Notification notification = new Notification(eventType, processDefinitionId, processInstanceId, involvedUsers);
			String json = new ObjectMapper().writeValueAsString(notification);
			log.info(">>>" + json + "<<<");
			
			HttpURLConnection connection = (HttpURLConnection) new java.net.URL(theUrl).openConnection();
			connection.setDoInput(true);
		    connection.setDoOutput(true);

			connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", "application/json");
		    connection.setRequestProperty("Content-Length", "" + Integer.toString(json.getBytes().length-1));
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
		    wr.writeBytes (json);

			BufferedReader inReader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
		    String inputLine;
		    StringBuffer response = new java.lang.StringBuffer();

		    while ((inputLine = inReader.readLine()) != null) {
		           response.append(inputLine);
		    }
		    inReader.close();
			log.info(response.toString());
			log.info("notification sent");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onEvent(ActivitiEvent event) {
		switch (event.getType()) {
			case TASK_CREATED:
			case TASK_ASSIGNED:
			case TASK_COMPLETED:
				restNotify2(event.getType(),
						event.getProcessDefinitionId(),
						event.getProcessInstanceId(),
						event.getEngineServices().getRuntimeService().getIdentityLinksForProcessInstance(event.getProcessInstanceId())
				);
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
