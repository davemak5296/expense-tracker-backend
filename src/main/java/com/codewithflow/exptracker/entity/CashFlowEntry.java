package com.codewithflow.exptracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cash_flow_entries")
public class CashFlowEntry extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "description")
    @NotNull
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NotNull
    private EntryType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_cat_id")
    @NotNull
    private SubCategory subCategory;

    @Column(name = "amount")
    @Min(value = 0, message = "Amount cannot be negative")
    @NotNull
    private BigDecimal amount;

    @Column(name = "book_date", columnDefinition = "DATE")
    @NotNull
    private LocalDate bookDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getBookDate() {
        return bookDate;
    }

    public void setBookDate(LocalDate bookDate) {
        this.bookDate = bookDate;
    }

    @Override
    public String toString() {
        return "CashFlowEntry{" +
                "user=" + user +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", subCategory=" + subCategory +
                ", amount=" + amount +
                ", bookDate=" + bookDate +
                ", id=" + id +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
