package com.polus.core.workflow.vo;

import java.util.List;

import com.polus.core.workflow.pojo.WorkflowAttachment;
import com.polus.core.workflow.pojo.WorkflowMapType;

public class WorkflowVO {

	private WorkflowAttachment newAttachment;

	private Integer workflowId;

	private String personId;
	
	private List<WorkflowMapType> workflowMapType;

	public WorkflowAttachment getNewAttachment() {
		return newAttachment;
	}

	public void setNewAttachment(WorkflowAttachment newAttachment) {
		this.newAttachment = newAttachment;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public List<WorkflowMapType> getWorkflowMapType() {
		return workflowMapType;
	}

	public void setWorkflowMapType(List<WorkflowMapType> workflowMapType) {
		this.workflowMapType = workflowMapType;
	}

}
