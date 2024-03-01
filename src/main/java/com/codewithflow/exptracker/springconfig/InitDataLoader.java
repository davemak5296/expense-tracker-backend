package com.codewithflow.exptracker.springconfig;

import com.codewithflow.exptracker.entity.Privilege;
import com.codewithflow.exptracker.entity.Role;
import com.codewithflow.exptracker.repository.PrivilegeRepository;
import com.codewithflow.exptracker.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class InitDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private RoleRepository roleRepository;

    private PrivilegeRepository privilegeRepository;

    public InitDataLoader(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, passwordPrivilege));
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", userPrivileges);

        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(final String name) {
        Optional<Privilege> privilege = privilegeRepository.findByName(name);
        if (privilege.isEmpty()) {
            privilege = Optional.of(privilegeRepository.save(new Privilege(name)));
        }
        return privilege.get();
    }

    @Transactional
    public void createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Optional<Role> role = roleRepository.findByName(name);

        if (role.isEmpty()) {
            Role newRole = new Role(name);
            newRole.setPrivileges(privileges);
            role = Optional.of(roleRepository.save(newRole));
        }
    }
}
