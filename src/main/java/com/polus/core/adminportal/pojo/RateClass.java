package com.polus.core.adminportal.pojo;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "RATE_CLASS")
public class RateClass implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "RATE_CLASS_CODE")
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "RATE_CLASS_TYPE")
	private String rateClassTypeCode;

	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(foreignKey = @ForeignKey(name = "RATE_CLASS_FK1"), name = "RATE_CLASS_TYPE", referencedColumnName = "RATE_CLASS_TYPE", insertable = false, updatable = false)
	private RateClassType rateClassType;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRateClassTypeCode() {
		return rateClassTypeCode;
	}

	public void setRateClassTypeCode(String rateClassTypeCode) {
		this.rateClassTypeCode = rateClassTypeCode;
	}

	public RateClassType getRateClassType() {
		return rateClassType;
	}

	public void setRateClassType(RateClassType rateClassType) {
		this.rateClassType = rateClassType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
