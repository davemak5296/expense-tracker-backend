package com.codewithflow.exptracker.springconfig;

import com.codewithflow.exptracker.entity.*;
import com.codewithflow.exptracker.repository.MainCategoryRepository;
import com.codewithflow.exptracker.repository.PrivilegeRepository;
import com.codewithflow.exptracker.repository.RoleRepository;
import com.codewithflow.exptracker.repository.SubCategoryRepository;
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
    private MainCategoryRepository mainCatRepository;
    private SubCategoryRepository subCatRepository;
    private final User user;

    public InitDataLoader(
            RoleRepository roleRepository,
            PrivilegeRepository privilegeRepository,
            MainCategoryRepository mainCatRepository,
            SubCategoryRepository subCatRepository
    ) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.mainCatRepository = mainCatRepository;
        this.subCatRepository = subCatRepository;
        this.user = new User();
        user.setId(4L);
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

        // expense
        MainCategory FandB = createMainCatIfNotFound("F&B", EntryType.EXPENSE);
        createIfSubCatNotFound("Dining out", EntryType.EXPENSE, FandB);
        createIfSubCatNotFound("Beverages", EntryType.EXPENSE, FandB);

        MainCategory shopping = createMainCatIfNotFound("Shopping", EntryType.EXPENSE);
        createIfSubCatNotFound("Groceries", EntryType.EXPENSE, shopping);
        createIfSubCatNotFound("Clothing", EntryType.EXPENSE, shopping);
        createIfSubCatNotFound("Electronics", EntryType.EXPENSE, shopping);

        MainCategory entertainment = createMainCatIfNotFound("Entertainment", EntryType.EXPENSE);
        createIfSubCatNotFound("Movies", EntryType.EXPENSE, entertainment);
        createIfSubCatNotFound("Concerts/Events", EntryType.EXPENSE, entertainment);
        createIfSubCatNotFound("Subscriptions", EntryType.EXPENSE, entertainment);
        createIfSubCatNotFound("Travel", EntryType.EXPENSE, entertainment);
        createIfSubCatNotFound("Gifts", EntryType.EXPENSE, entertainment);

        MainCategory traffic = createMainCatIfNotFound("Traffic", EntryType.EXPENSE);
        createIfSubCatNotFound("Traffic", EntryType.EXPENSE, traffic);

        MainCategory housing = createMainCatIfNotFound("Housing", EntryType.EXPENSE);
        createIfSubCatNotFound("Rent", EntryType.EXPENSE, housing);
        createIfSubCatNotFound("Mortgage", EntryType.EXPENSE, housing);

        MainCategory study = createMainCatIfNotFound("Study", EntryType.EXPENSE);
        createIfSubCatNotFound("Study", EntryType.EXPENSE, study);

        MainCategory medical = createMainCatIfNotFound("Medical", EntryType.EXPENSE);
        createIfSubCatNotFound("Medical", EntryType.EXPENSE, medical);

        MainCategory electronic = createMainCatIfNotFound("Electronic", EntryType.EXPENSE);
        createIfSubCatNotFound("Electronic", EntryType.EXPENSE, electronic);

        MainCategory otherExpense = createMainCatIfNotFound("Other", EntryType.EXPENSE);
        createIfSubCatNotFound("Other", EntryType.EXPENSE, otherExpense);

        // income
        MainCategory salary = createMainCatIfNotFound("Salary", EntryType.INCOME);
        createIfSubCatNotFound("Base Salary", EntryType.INCOME, salary);
        createIfSubCatNotFound("Bonus", EntryType.INCOME, salary);

        MainCategory investment = createMainCatIfNotFound("Investment", EntryType.INCOME);
        createIfSubCatNotFound("Stocks", EntryType.INCOME, investment);
        createIfSubCatNotFound("Bonds", EntryType.INCOME, investment);
        createIfSubCatNotFound("Dividends", EntryType.INCOME, investment);
        createIfSubCatNotFound("Interest", EntryType.INCOME, investment);

        MainCategory otherIncome = createMainCatIfNotFound("Other", EntryType.INCOME);
        createIfSubCatNotFound("Other", EntryType.INCOME, otherIncome);

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

    @Transactional
    public MainCategory createMainCatIfNotFound(final String name, EntryType type) {
        Optional<MainCategory> mainCat = mainCatRepository.findByNameAndType(name, type);
        if (mainCat.isEmpty()) {
            mainCat = Optional.of(mainCatRepository.save(new MainCategory(name, type)));
        }
        return mainCat.get();
    }

    @Transactional
    public void createIfSubCatNotFound(final String name, EntryType type, final MainCategory mainCat) {
        Optional<SubCategory> subCat = subCatRepository.findByNameAndType(name, type);

        if (subCat.isEmpty()) {
            SubCategory newSubCat = new SubCategory(name, type, mainCat);
            newSubCat.setUser(this.user);
            subCat = Optional.of(subCatRepository.save(newSubCat));
        }
    }
}
