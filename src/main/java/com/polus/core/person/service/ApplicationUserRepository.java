package com.polus.core.person.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polus.core.person.pojo.Person;

@Repository
public interface ApplicationUserRepository extends JpaRepository<Person, Long> {

	/**
	 * This method is used to validate user credentials.
	 * @param principalName - Username of the login user.
	 * @return PrincipalBo - user Object.
	 */
	public Person findByPrincipalName(String principalName);

}
