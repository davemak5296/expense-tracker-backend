package com.codewithflow.exptracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(
        name = "main_categories",
        uniqueConstraints = @UniqueConstraint(name = "unique_main_cat_name_type", columnNames = { "name", "type" })
)
public class MainCategory extends BaseEntity implements Serializable {

    @Column(name = "name")
    @NotNull
    @Length(min = 1, max = 40)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NotNull
    private EntryType type;

    @OneToMany(mappedBy = "mainCategory", fetch = FetchType.LAZY)
    private List<SubCategory> subCategories;

    public MainCategory() { super(); }

    public MainCategory(final String name, EntryType type) {
        super();
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    @JsonIgnore
    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    @JsonIgnore
    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}