package com.codewithflow.exptracker.dto;

import com.codewithflow.exptracker.entity.EntryType;
import com.codewithflow.exptracker.entity.MainCategory;
import com.codewithflow.exptracker.entity.SubCategory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

public class CashFlowEntryRespDTO {
    private Long id;
    private EntryType type;
    private String description;
    private BigDecimal amount;
    private LocalDate bookDate;
    private String mainCategory;
    private Long subCategoryId;
    private String subCategory;
    private Timestamp lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(MainCategory mainCategory) {
        this.mainCategory = mainCategory.getName();
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory.getName();
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
