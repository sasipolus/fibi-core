package com.polus.core.workflow.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORKFLOW_STATUS")
public class WorkflowStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APPROVAL_STATUS")
	private String approveStatusCode;
	
	@Column(name = "DESCRIPTION")
	private String approveStatus;
	
	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Timestamp getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getApproveStatusCode() {
		return approveStatusCode;
	}

	public void setApproveStatusCode(String approveStatusCode) {
		this.approveStatusCode = approveStatusCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

}

