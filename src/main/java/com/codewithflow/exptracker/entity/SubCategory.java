package com.codewithflow.exptracker.entity;

import com.sun.tools.javac.Main;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Entity
@Table(
        name = "sub_categories",
        uniqueConstraints = @UniqueConstraint(name = "unique_sub_cat_name_type", columnNames = { "name", "main_cat_id", "user_id" })
)
public class SubCategory extends BaseEntity implements Serializable {

    @Column(name = "name")
    @NotNull
    @Length(min = 1, max = 40)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NotNull
    private EntryType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "main_cat_id")
    @NotNull
    private MainCategory mainCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public SubCategory () { super(); }

    public SubCategory (final String name, EntryType type, MainCategory mainCategory) {
        super();
        this.name = name;
        this.type = type;
        this.mainCategory = mainCategory;
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

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
