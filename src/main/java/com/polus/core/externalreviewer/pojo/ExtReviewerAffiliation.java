package com.polus.core.externalreviewer.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EXT_REVIEWER_AFFILIATION")
public class ExtReviewerAffiliation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AFFILATION_INSTITUITION_CODE")
	private String affiliationInstitutionCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public String getAffiliationInstitutionCode() {
		return affiliationInstitutionCode;
	}

	public void setAffiliationInstitutionCode(String affiliationInstitutionCode) {
		this.affiliationInstitutionCode = affiliationInstitutionCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
}