package com.codewithflow.exptracker.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;

@Entity
public class Privilege extends BaseEntity implements Serializable {

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege() {
        super();
    }

    public Privilege(final String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Privilege privilege)) return false;
        return Objects.equals(getName(), privilege.getName()) && Objects.equals(getRoles(), privilege.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getRoles());
    }

    @Override
    public String toString() {
        return "Privilege [name=" + name + "]" + "[id=" + id + "]";
    }
}
