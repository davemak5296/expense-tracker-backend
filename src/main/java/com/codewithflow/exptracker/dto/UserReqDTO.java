package com.codewithflow.exptracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

//public record UserDTO(
//    String email,
//    String password,
//    String alias
//) { }

public class UserReqDTO {

    @NotBlank(message = "Invalid Email: Email cannot be blank")
    @NotNull(message = "Invalid Email: Email cannot be null")
    @Email(message = "Invalid Email")
    String email;


    @NotBlank(message = "Invalid Password: Password cannot be blank")
    @NotNull(message = "Invalid Password: Password cannot be null")
    String password;

    @NotBlank(message = "Invalid Alias: Alias cannot be blank")
    @NotNull(message = "Invalid Alias: Alias cannot be null")
    @Length(min = 1, max = 20, message = "Invalid Alias: Alias length must be between 1 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Invalid Alias: Alias must only contain letters and numbers")
    String alias;

    public UserReqDTO() { }
    public UserReqDTO(String email, String password, String alias) {
        this.email = email;
        this.password = password;
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
