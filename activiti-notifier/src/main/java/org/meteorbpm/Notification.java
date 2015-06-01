package org.meteorbpm;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.task.IdentityLink;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification extends Notifier {
    private ActivitiEventType eventType;
    private String processDefinitionId;
    private String processInstanceId;
    private List<IdentityLink> involvedUsers;
    
    public Notification(ActivitiEventType eventType, String processDefinitionId, String processInstanceId, List<IdentityLink> involvedUsers) {
    	this.eventType = eventType;
    	this.processDefinitionId = processDefinitionId;
    	this.processInstanceId = processInstanceId;
    	this.involvedUsers = involvedUsers;
    }

    public String getEventType() {
        return eventType.name();
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public List<String> getInvolvedUsers() {
    	List<String> tmp = new ArrayList<String>();
    	for (IdentityLink idLink : involvedUsers) {
			tmp.add(idLink.getUserId());
		}
        return tmp;
    }
}
