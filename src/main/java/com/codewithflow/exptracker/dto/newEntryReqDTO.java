package com.codewithflow.exptracker.dto;

import com.codewithflow.exptracker.entity.EntryType;
import com.codewithflow.exptracker.util.exception.validation.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.time.LocalDate;

public class newEntryReqDTO {

    @NotNull(message = "Invalid type: Type cannot be null")
    @ValueOfEnum(enumClass = EntryType.class)
    private String type;

    @NotBlank(message = "Invalid description: Description cannot be blank")
    private String description;

    @NotNull(message = "Invalid amount: Amount cannot be empty")
    @Range(min = 0, message = "Invalid amount: Amount cannot be negative")
    private BigDecimal amount;

    @NotNull(message = "Invalid bookDate: Book date cannot be blank")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookDate;

    @NotNull(message = "Invalid mainCategory: subCategoryId cannot be null")
    @Range(min = 1, message = "Invalid subCategoryId: subCategoryId must be greater than 0")
    private Long subCategoryId;

    @NotNull(message = "Invalid mainCategory: subCategoryUserId cannot be null")
    @Range(min = 1, message = "Invalid subCategoryId: subCategoryUserId must be greater than 0")
    private Long subCategoryUserId;

    @Range(min = 1, message = "Invalid userId: userId must be greater than 0")
    private Long userId;

    public newEntryReqDTO(
            String type,
            String description,
            BigDecimal amount,
            LocalDate bookDate,
            Long subCategoryId,
            Long subCategoryUserId
        ) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.bookDate = bookDate;
        this.subCategoryId = subCategoryId;
        this.subCategoryUserId = subCategoryUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Long getSubCategoryUserId() {
        return subCategoryUserId;
    }

    public void setSubCategoryUserId(Long subCategoryUserId) {
        this.subCategoryUserId = subCategoryUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}