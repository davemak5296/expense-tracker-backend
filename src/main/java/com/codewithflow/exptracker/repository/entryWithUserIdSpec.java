package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.CashFlowEntry;
import com.codewithflow.exptracker.entity.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class entryWithUserIdSpec implements Specification<CashFlowEntry> {
    private final String userId;

    public entryWithUserIdSpec(String userId) {
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
        Join<CashFlowEntry, User> entryOwner = root.join("user");
        return cb.equal(entryOwner.get("id"), userId);
    }
}
