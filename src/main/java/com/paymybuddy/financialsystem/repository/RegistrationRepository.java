package com.paymybuddy.financialsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.financialsystem.model.User;

@Repository
public interface RegistrationRepository extends CrudRepository<User, Integer> {

}
