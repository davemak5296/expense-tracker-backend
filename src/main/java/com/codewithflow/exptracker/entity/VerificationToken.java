package com.codewithflow.exptracker.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken extends BaseEntity implements Serializable {

    private static final int EXPIRATION = 60 * 24;

    @NotNull
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_VERIFY_USER"))
    private User user;

    private Date expiryDate;

    public VerificationToken() {
        super();
    }

    public VerificationToken(final String token) {
        super();

        this.token = token;
        this.expiryDate = calculateExpiryDate();
    }

    public VerificationToken(final String token, final User user) {
        super();

        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(final Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    private Date calculateExpiryDate() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, VerificationToken.EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerificationToken that)) return false;
        return Objects.equals(getToken(), that.getToken()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getExpiryDate(), that.getExpiryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken(), getUser(), getExpiryDate());
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", expiryDate=" + expiryDate +
                ", id=" + id +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
