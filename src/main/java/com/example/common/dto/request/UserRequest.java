package com.example.common.dto.request;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Tran Minh Truyen
 */
@Data
public class UserRequest implements Serializable {

    @NotBlank(message = "account is mandatory")
    private String account;

    @NotBlank(message = "password is mandatory")
    private String password;

    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    @NotBlank(message = "lastName is mandatory")
    private String lastName;

    @NotBlank(message = "email is mandatory")
    private String email;

    @NotBlank(message = "email is mandatory")
    @Length(min = 10, max = 10, message = "phone is mandatory and contain 10 number")
    private String phoneNumber;

    private Date birthDay;

    @NotBlank(message = "address is mandatory")
    private String address;

    @NotBlank(message = "district is mandatory")
    private String district;

    @NotBlank(message = "city is mandatory")
    private String city;

    @NotBlank(message = "postCode is mandatory")
    private String postCode;

    @NotBlank(message = "citizenID is mandatory")
    private String citizenID;

    private String image;

    @NotBlank(message = "role is mandatory")
    private String role;

    private boolean isActive;
}
