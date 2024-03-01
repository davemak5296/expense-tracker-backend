package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.Role;
import com.codewithflow.exptracker.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
