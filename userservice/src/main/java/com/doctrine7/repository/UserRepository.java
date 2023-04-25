package com.doctrine7.repository;

import com.doctrine7.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAllByEmployeesContains(String employee);
}
