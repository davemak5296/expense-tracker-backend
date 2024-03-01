package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.Privilege;
import com.codewithflow.exptracker.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
}
