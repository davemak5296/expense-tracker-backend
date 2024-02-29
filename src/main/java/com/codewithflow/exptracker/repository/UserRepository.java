package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
