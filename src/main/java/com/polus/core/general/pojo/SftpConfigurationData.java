package com.polus.core.general.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SFTP_CONFIGURATION_DATA")
public class SftpConfigurationData implements Serializable {
	
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name = "CONFIGURATION_KEY")
		private String configurationKey;

		@Column(name = "CONFIGURATION_VALUE")
		private String configurationValue;

		@Column(name = "DESCRIPTION")
		private String description;

		@Column(name = "UPDATE_TIMESTAMP")
		private Timestamp updateTimeStamp;

		@Column(name = "UPDATE_USER")
		private String updateUser;

		public String getConfigurationKey() {
			return configurationKey;
		}

		public void setConfigurationKey(String configurationKey) {
			this.configurationKey = configurationKey;
		}

		public String getConfigurationValue() {
			return configurationValue;
		}

		public void setConfigurationValue(String configurationValue) {
			this.configurationValue = configurationValue;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

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
		
		
}
